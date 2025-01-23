package com.skytechautosieve.sieves;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAutoSieve extends TileEntity implements ITickable, ICapabilityProvider {

	private final ItemStackHandler inputInventory = new ItemStackHandler(1);
	private final ItemStackHandler outputInventory = new ItemStackHandler(9);
	private final EnergyStorage energyStorage = new EnergyStorage(10000);

	@Override
	public void update() {
		if (!world.isRemote && canSift()) {
			processSieve();
		}
	}

	private boolean canSift() {
		ItemStack stack = inputInventory.getStackInSlot(0);
		return !stack.isEmpty();
	}

	private void processSieve() {
		if (energyStorage.extractEnergy(100, true) >= 100) {
			inputInventory.extractItem(0, 1, false);
			outputInventory.insertItem(0, new ItemStack(Items.DIAMOND), false);
			energyStorage.extractEnergy(100, false);
		}
	}

	public int getEnergyStored() {
		return energyStorage.getEnergyStored();
	}

	public int getMaxEnergyStored() {
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputInventory);
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		}
		return super.getCapability(capability, facing);
	}
}
