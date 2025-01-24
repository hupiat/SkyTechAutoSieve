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

	private final ItemStackHandler inventory = new ItemStackHandler(48);
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
		ItemStack stack = inventory.getStackInSlot(0);
		return !stack.isEmpty() && hasOutputSpace();
	}

	private boolean hasOutputSpace() {
		for (int i = 1; i < inventory.getSlots(); i++) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private void processSieve() {
		inventory.extractItem(0, 1, false);
		ItemStack output = new ItemStack(Items.DIAMOND, 1);

		for (int i = 1; i < inventory.getSlots(); i++) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				inventory.insertItem(i, output, false);
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
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inventory);
		}
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		}
		return super.getCapability(capability, facing);
	}

	// Inventory management

	@Override
	public int getSizeInventory() {
		return inventory.getSlots();
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < inventory.getSlots(); i++) {
			if (!inventory.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		if (index >= 0 && index < inventory.getSlots()) {
			return inventory.getStackInSlot(index);
		}
		throw new RuntimeException("Slot " + index + " not in valid range - [0," + inventory.getSlots() + ")");
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (index >= 0 && index < inventory.getSlots()) {
			return inventory.extractItem(index, count, false);
		}
		throw new RuntimeException("Slot " + index + " not in valid range - [0," + inventory.getSlots() + ")");
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		if (index >= 0 && index < inventory.getSlots()) {
			return inventory.extractItem(index, inventory.getStackInSlot(index).getCount(), false);
		}
		throw new RuntimeException("Slot " + index + " not in valid range - [0," + inventory.getSlots() + ")");
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if (index >= 0 && index < inventory.getSlots()) {
			inventory.setStackInSlot(index, stack);
		} else {
			throw new RuntimeException("Slot " + index + " not in valid range - [0," + inventory.getSlots() + ")");
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
		return index == 0;
	}

	@Override
	public void clear() {
		for (int i = 0; i < inventory.getSlots(); i++) {
			inventory.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
		energyStorage.receiveEnergy(compound.getInteger("EnergyStored"), false);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("Inventory", inventory.serializeNBT());
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
		return "container.auto_sieve";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}
}
