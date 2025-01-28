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

	public SieveDropDataRepository() {
		super(DATA_NAME);
	}

	public SieveDropDataRepository(String name) {
		super(name);
	}

	public void setDropData(Block block, Set<SieveDropData> drops, World world, boolean writeInConfig) {
		sieveData.put(block, drops);
		if (writeInConfig) {
			InternalTools.eraseConfig(block.getLocalizedName().toString());
			StringBuilder dropsBuilder = new StringBuilder();
			for (SieveDropData dropData : drops) {
				if (dropsBuilder.length() != 0) {
					dropsBuilder.append(";");
				}
				dropsBuilder.append(dropData.getItem().getItem().getRegistryName() + "," + dropData.getDropRate());
			}
			InternalTools.writeConfig(block.getLocalizedName().toString(), dropsBuilder.toString());
		}
		world.getMapStorage().setData(DATA_NAME, this);
		world.getMapStorage().saveAllData();
	}

	public Set<SieveDropData> getDropData(Block block) {
		return sieveData.getOrDefault(block, new HashSet<>());
	}

	public void syncDropData(World world) {
		Properties properties = InternalTools.readConfig();
		Map<Block, Set<SieveDropData>> syncMap = new HashMap<>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			if (StringUtils.equals(entry.getKey().toString(), "process_time_seconds")) {
				continue;
			}
			Block block = Block.getBlockFromName(entry.getKey().toString());
			String[] values = entry.getValue().toString().split(";");
			syncMap.putIfAbsent(block, new HashSet<>());
			for (String value : values) {
				syncMap.get(block).add(new SieveDropData(new ItemStack(Item.getByNameOrId(value.split(",")[0])),
						Float.parseFloat(value.split(",")[1])));
			}
		}
		for (Entry<Block, Set<SieveDropData>> entry : syncMap.entrySet()) {
			if (!sieveData.containsKey(entry.getKey())
					|| !(new HashSet<>(entry.getValue())).equals(new HashSet<>(sieveData.get(entry.getKey())))) {
				get(world).setDropData(entry.getKey(), entry.getValue(), world, false);
			}
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
