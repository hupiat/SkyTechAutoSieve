package com.skytechautosieve.sieves;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAutoSieve extends TileEntity implements ITickable, IInventory {

	private final ItemStackHandler inputInventory = new ItemStackHandler(1);
	private final ItemStackHandler outputInventory = new ItemStackHandler(9);
	private final EnergyStorage energyStorage = new EnergyStorage(10000, 200, 200);

	private static final int ENERGY_PER_TICK = 100;
	private int processTime = 0;
	private static final int PROCESS_TIME_REQUIRED = 100;

	@Override
	public void update() {
		if (!world.isRemote) {
			if (canSift() && energyStorage.getEnergyStored() >= ENERGY_PER_TICK) {
				processTime++;
				energyStorage.extractEnergy(ENERGY_PER_TICK, false);

				if (processTime >= PROCESS_TIME_REQUIRED) {
					processSieve();
					processTime = 0;
				}
			} else {
				processTime = 0;
			}
		}
	}

	private boolean canSift() {
		ItemStack stack = inputInventory.getStackInSlot(0);
		return !stack.isEmpty() && hasOutputSpace();
	}

	private boolean hasOutputSpace() {
		for (int i = 0; i < outputInventory.getSlots(); i++) {
			if (outputInventory.getStackInSlot(i).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private void processSieve() {
		inputInventory.extractItem(0, 1, false);
		ItemStack output = new ItemStack(Items.DIAMOND, 1);

		for (int i = 0; i < outputInventory.getSlots(); i++) {
			if (outputInventory.getStackInSlot(i).isEmpty()) {
				outputInventory.insertItem(i, output, false);
				break;
			}
		}
	}

	public int getEnergyStored() {
		return energyStorage.getEnergyStored();
	}

	public int getMaxEnergyStored() {
		return energyStorage.getMaxEnergyStored();
	}

	// Capability handling for Forge compatibility (energy and item inventory)

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
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
					.cast(facing == EnumFacing.UP ? inputInventory : outputInventory);
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		}
		return super.getCapability(capability, facing);
	}

	// Inventory management

	@Override
	public int getSizeInventory() {
		return inputInventory.getSlots() + outputInventory.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < inputInventory.getSlots(); i++) {
			if (!inputInventory.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		for (int i = 0; i < outputInventory.getSlots(); i++) {
			if (!outputInventory.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (index < inputInventory.getSlots()) {
			return inputInventory.getStackInSlot(index);
		} else {
			return outputInventory.getStackInSlot(index - inputInventory.getSlots());
		}
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (index < inputInventory.getSlots()) {
			return inputInventory.extractItem(index, count, false);
		} else {
			return outputInventory.extractItem(index - inputInventory.getSlots(), count, false);
		}
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (index < inputInventory.getSlots()) {
			return inputInventory.extractItem(index, inputInventory.getStackInSlot(index).getCount(), false);
		} else {
			return outputInventory.extractItem(index - inputInventory.getSlots(),
					outputInventory.getStackInSlot(index).getCount(), false);
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index < inputInventory.getSlots()) {
			inputInventory.setStackInSlot(index, stack);
		} else {
			outputInventory.setStackInSlot(index - inputInventory.getSlots(), stack);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return !isInvalid() && player.getDistanceSq(pos) <= 64.0;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return index < inputInventory.getSlots(); // Only input slot accepts items
	}

	@Override
	public void clear() {
		for (int i = 0; i < inputInventory.getSlots(); i++) {
			inputInventory.setStackInSlot(i, ItemStack.EMPTY);
		}
		for (int i = 0; i < outputInventory.getSlots(); i++) {
			outputInventory.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		inputInventory.deserializeNBT(compound.getCompoundTag("InputInventory"));
		outputInventory.deserializeNBT(compound.getCompoundTag("OutputInventory"));
		energyStorage.receiveEnergy(compound.getInteger("EnergyStored"), false);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("InputInventory", inputInventory.serializeNBT());
		compound.setTag("OutputInventory", outputInventory.serializeNBT());
		compound.setInteger("EnergyStored", energyStorage.getEnergyStored());
		return compound;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public String getName() {
		return TileEntityAutoSieve.class.getSimpleName();
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}
}
