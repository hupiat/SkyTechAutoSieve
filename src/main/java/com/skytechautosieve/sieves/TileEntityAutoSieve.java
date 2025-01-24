package com.skytechautosieve.sieves;

import com.skytechautosieve.Program;
import com.skytechautosieve.sieves.networking.PacketSyncEnergy;

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
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAutoSieve extends TileEntity implements ITickable, IInventory {

	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_START = 23;
	private static final int OUTPUT_END = 47;
	private static final int TOTAL_SLOTS = 48;

	public static final int MAX_ENERGY = 10000;
	private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS);
	private final EnergyStorage energyStorage = new EnergyStorage(MAX_ENERGY, MAX_ENERGY, 500);

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
			if (world.getTotalWorldTime() % 20 == 0) { // Every second
				PacketSyncEnergy packet = new PacketSyncEnergy(pos, energyStorage.getEnergyStored());
				Program.NETWORK_CLIENT_CHANNEL_ENERGY.sendToAll(packet);
			}
			if (world.getTotalWorldTime() % 60 == 0) { // Every 3 seconds
				// Ensure a smoother effect for charging bar
				BlockAutoSieve.chargeEnergyThenSendToClient(world, pos);
			}
		}
	}

	private boolean canSift() {
		ItemStack inputStack = inventory.getStackInSlot(INPUT_SLOT);
		return !inputStack.isEmpty() && hasOutputSpace();
	}

	private boolean hasOutputSpace() {
		for (int i = OUTPUT_START; i <= OUTPUT_END; i++) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private void processSieve() {
		inventory.extractItem(INPUT_SLOT, 1, false);
		ItemStack output = new ItemStack(Items.DIAMOND, 1); // Example output

		for (int i = OUTPUT_START; i <= OUTPUT_END; i++) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				inventory.insertItem(i, output.copy(), false);
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

	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return this.processTime;
		case 1:
			return this.energyStorage.getEnergyStored();
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.processTime = value;
			break;
		case 1:
			this.energyStorage.receiveEnergy(value - this.energyStorage.getEnergyStored(), false);
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 2;
	}

	// Capability handling for Forge compatibility (energy and item inventory)

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(energyStorage);
		}
		return super.getCapability(capability, facing);
	}

	// Inventory management

	private int numPlayersUsing = 0;

	@Override
	public void openInventory(EntityPlayer player) {
		if (!player.isSpectator()) {
			numPlayersUsing++;
		}
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		if (!player.isSpectator() && numPlayersUsing > 0) {
			numPlayersUsing--;
		}
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return inventory.extractItem(index, count, false);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		inventory.setStackInSlot(index, stack);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < TOTAL_SLOTS; i++) {
			if (!inventory.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return inventory.extractItem(index, inventory.getStackInSlot(index).getCount(), false);
	}

	@Override
	public void clear() {
		for (int i = 0; i < TOTAL_SLOTS; i++) {
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
	public boolean isUsableByPlayer(EntityPlayer player) {
		return !isInvalid() && player.getDistanceSq(pos) <= 64.0;
	}

	@Override
	public int getSizeInventory() {
		return TOTAL_SLOTS;
	}

	@Override
	public String getName() {
		return "container.auto_sieve";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
}
