package com.skytechautosieve.sieves.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.skytechautosieve.utils.InternalTools;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class SieveDropDataRepository extends WorldSavedData {
	private static final String DATA_NAME = "skytech_sieve_data";
	private Map<Block, Set<SieveDropData>> sieveData = new HashMap<>();

	private static final Set<String> CONFIG_KEYS_TO_EXCLUDE = new HashSet<String>() {{
		add(InternalTools.CONFIG_PROCESS_TIME);
		add(InternalTools.CONFIG_PROCESS_TIME_STONE_TIER1);
		add(InternalTools.CONFIG_PROCESS_TIME_STONE_TIER2);
	}};

	public SieveDropDataRepository() {
		super(DATA_NAME);
	}

	public SieveDropDataRepository(String name) {
		super(name);
	}

	public void setDropData(Block block, Set<SieveDropData> drops, World world, boolean writeInConfig) {
		sieveData.put(block, drops);
		if (writeInConfig) {
			String key = block.getRegistryName().toString() + "," + block.getMetaFromState(block.getDefaultState());
			InternalTools.eraseConfig(key);
			StringBuilder dropsBuilder = new StringBuilder();
			for (SieveDropData dropData : drops) {
				if (dropsBuilder.length() != 0) {
					dropsBuilder.append(";");
				}
				dropsBuilder.append(dropData.getItem().getItem().getRegistryName() + "," + dropData.getDropRate() + ","
						+ dropData.getItem().getItem().getMetadata(dropData.getItem()));
			}
			InternalTools.writeConfig(key, dropsBuilder.toString());
		}
		world.getMapStorage().setData(DATA_NAME, this);
		world.getMapStorage().saveAllData();
		markDirty();
	}

	public Set<SieveDropData> getDropData(Block block) {
		return sieveData.getOrDefault(block, new HashSet<>());
	}

	public void syncDropData(World world) {
		Properties properties = InternalTools.readConfig();
		Map<Block, Set<SieveDropData>> syncMap = new HashMap<>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			if (CONFIG_KEYS_TO_EXCLUDE.contains(entry.getKey().toString())) {
				continue;
			}
			String[] blockParts = entry.getKey().toString().split(",");
			Block block = Block.getBlockFromName(blockParts[0]);
			if (block != null) {
				block = InternalTools.getStateFromMeta(blockParts[0], Integer.parseInt(blockParts[1])).getBlock();
			}
			String[] values = entry.getValue().toString().split(";");
			syncMap.putIfAbsent(block, new HashSet<>());
			for (String value : values) {
				String[] parts = value.split(",");
				if (parts.length > 1) {
					int meta = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
					syncMap.get(block).add(new SieveDropData(new ItemStack(Item.getByNameOrId(parts[0]), 1, meta),
							Float.parseFloat(parts[1])));
				}
			}
		}
		sieveData.clear();
		for (Entry<Block, Set<SieveDropData>> entry : syncMap.entrySet()) {
			get(world).setDropData(entry.getKey(), entry.getValue(), world, false);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		sieveData.clear();
		for (String key : nbt.getKeySet()) {
			NBTTagCompound blockData = nbt.getCompoundTag(key);
			Set<SieveDropData> drops = new HashSet<>();
			for (String dropKey : blockData.getKeySet()) {
				NBTTagCompound dropNBT = blockData.getCompoundTag(dropKey);
				ItemStack stack = new ItemStack(Item.getByNameOrId(dropNBT.getString("Item")));
				float dropRate = dropNBT.getFloat("DropRate");
				drops.add(new SieveDropData(stack, dropRate));
			}
			sieveData.put(Block.getBlockFromName(key), drops);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		for (Map.Entry<Block, Set<SieveDropData>> entry : sieveData.entrySet()) {
			NBTTagCompound blockData = new NBTTagCompound();
			for (int i = 0; i < entry.getValue().size(); i++) {
				SieveDropData drop = new ArrayList<>(entry.getValue()).get(i);
				NBTTagCompound dropNBT = new NBTTagCompound();
				dropNBT.setString("Item", drop.getItem().getItem().getRegistryName().toString());
				dropNBT.setFloat("DropRate", drop.getDropRate());
				blockData.setTag("Drop" + i, dropNBT);
			}
			nbt.setTag(entry.getKey().getRegistryName().toString(), blockData);
		}
		return nbt;
	}

	@Nullable
	public static SieveDropDataRepository get(World world) {
		SieveDropDataRepository instance = null;
		MapStorage storage = null;
		storage = world.getMapStorage();
		if (storage != null) {
			instance = (SieveDropDataRepository) storage.getOrLoadData(SieveDropDataRepository.class, DATA_NAME);
			if (instance == null) {
				instance = new SieveDropDataRepository();
				storage.setData(DATA_NAME, instance);
			}
		}
		return instance;
	}
}
