package com.skytechautosieve.sieves.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class SieveDropDataRepository extends WorldSavedData {
	private static final String DATA_NAME = "skytech_sieve_data";
	private Map<Block, List<SieveDropData>> sieveData = new HashMap<>();

	public SieveDropDataRepository() {
		super(DATA_NAME);
	}

	public SieveDropDataRepository(String name) {
		super(name);
	}

	public void setDropData(Block block, List<SieveDropData> drops, World world) {
		sieveData.put(block, drops);
		world.getMapStorage().setData(DATA_NAME, this);
		world.getMapStorage().saveAllData();
	}

	public List<SieveDropData> getDropData(Block block) {
		return sieveData.getOrDefault(block, new ArrayList<>());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		sieveData.clear();
		for (String key : nbt.getKeySet()) {
			NBTTagCompound blockData = nbt.getCompoundTag(key);
			List<SieveDropData> drops = new ArrayList<>();
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
		for (Map.Entry<Block, List<SieveDropData>> entry : sieveData.entrySet()) {
			NBTTagCompound blockData = new NBTTagCompound();
			for (int i = 0; i < entry.getValue().size(); i++) {
				SieveDropData drop = entry.getValue().get(i);
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
