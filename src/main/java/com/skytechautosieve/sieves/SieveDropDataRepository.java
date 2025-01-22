package com.skytechautosieve.sieves;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
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

	public void setDropData(Block block, List<SieveDropData> drops) {
		sieveData.put(block, drops);
		markDirty();
	}

	public List<SieveDropData> getDropData(Block block) {
		return sieveData.getOrDefault(block, new ArrayList<>());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		return nbt;
	}

	public static SieveDropDataRepository get(World world) {
		MapStorage storage = world.getPerWorldStorage();
		SieveDropDataRepository instance = (SieveDropDataRepository) storage
				.getOrLoadData(SieveDropDataRepository.class, DATA_NAME);
		if (instance == null) {
			instance = new SieveDropDataRepository();
			storage.setData(DATA_NAME, instance);
		}
		return instance;
	}
}
