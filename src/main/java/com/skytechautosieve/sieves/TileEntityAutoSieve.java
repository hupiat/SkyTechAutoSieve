package com.skytechautosieve.sieves;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAutoSieve extends TileEntity implements ITickable, IEnergyStorage {

	private ItemStackHandler inputInventory = new ItemStackHandler(1);
	private ItemStackHandler outputInventory = new ItemStackHandler(9);
	private EnergyStorage energyStorage = new EnergyStorage(10000);
	private SieveDropDataRepository repository;

	@Override
	public void update() {
		if (!world.isRemote && canSift()) {
			processSieve();
		}
	}

	private boolean canSift() {
		ItemStack stack = inputInventory.getStackInSlot(0);
		return stack != null && repository.getDropData(Block.getBlockFromItem(stack.getItem())) != null;
	}

	private void processSieve() {
		if (energyStorage.getEnergyStored() >= 100) {
			ItemStack stack = inputInventory.extractItem(0, 1, false);
			List<SieveDropData> drops = repository.getDropData(Block.getBlockFromItem(stack.getItem()));

			for (SieveDropData drop : drops) {
				if (world.rand.nextFloat() < drop.getDropRate()) {
					outputInventory.insertItem(0, drop.getItem().copy(), false);
				}
			}
			energyStorage.extractEnergy(100, false);
		}
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return energyStorage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return energyStorage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored() {
		return energyStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public boolean canExtract() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canReceive() {
		// TODO Auto-generated method stub
		return false;
	}
}
