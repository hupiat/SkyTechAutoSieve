package com.skytechautosieve.sieves;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.skytechautosieve.BlocksSubscriberHandler;
import com.skytechautosieve.Program;
import com.skytechautosieve.sieves.data.SieveDropData;
import com.skytechautosieve.sieves.data.SieveDropDataRepository;
import com.skytechautosieve.sieves.networking.PacketSyncEnergy;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
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

	public static final int INPUT_SLOT = 0;
	public static final int OUTPUT_START = 20;
	public static final int OUTPUT_END = 40;
	public static final int UPGRADE_SLOT_START = 41;
	public static final int UPGRADE_SLOT_END = 42;
	public static final int TOTAL_SLOTS = 42;

	public static final int MAX_FORTUNE_UPGRADES = 5;

	public static final int MAX_ENERGY = 10000;
	private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS);
	private final EnergyStorage energyStorage = new EnergyStorage(MAX_ENERGY, MAX_ENERGY, 500);

	private static final int ENERGY_PER_TICK = 100;
	private int processTime = 0;
	private static final int PROCESS_TIME_REQUIRED = 100;

	private SieveDropDataRepository repository = null;

	private int fortuneUpgrades = -1;
	private int speedUpgrades = -1;

	@Override
	public void onLoad() {
		repository = SieveDropDataRepository.get(world);
		super.onLoad();
	}

	@Override
	public void update() {
		boolean canSift = canSift() && energyStorage.getEnergyStored() >= ENERGY_PER_TICK;
		if (!world.isRemote) {
			if (canSift) {
				processTime++;
				energyStorage.extractEnergy(ENERGY_PER_TICK, false);
				if (processTime >= (speedUpgrades > 0 ? PROCESS_TIME_REQUIRED / speedUpgrades
						: PROCESS_TIME_REQUIRED)) {
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
				// Ensure a smoother effect for charging bar than a shorter delay
				BlockAutoSieve.chargeEnergyThenSendToClient(world, pos);

				// Performing a left shifting to process all items
				for (int i = INPUT_SLOT; i < OUTPUT_START - 1; i++) {
					if (inventory.getStackInSlot(i).isEmpty()) {
						ItemStack nextStack = inventory.getStackInSlot(i + 1);
						if (!nextStack.isEmpty()) {
							inventory.setStackInSlot(i, nextStack);
							inventory.setStackInSlot(i + 1, ItemStack.EMPTY);
						}
					}
				}

				// Processing upgrades slots
				this.fortuneUpgrades = -1;
				this.speedUpgrades = -1;
				if (canSift) {
					for (int i = UPGRADE_SLOT_START; i <= UPGRADE_SLOT_END; i++) {
						if (!inventory.getStackInSlot(i - 1).isEmpty()) {
							int count = inventory.getStackInSlot(i - 1).getCount();
							ItemStack upgrade = inventory.extractItem(i - 1, 1, false);
							if (upgrade.getItem() == BlocksSubscriberHandler.FORTUNE_UPGRADE) {
								this.fortuneUpgrades = count > MAX_FORTUNE_UPGRADES ? MAX_FORTUNE_UPGRADES : count;
							}
							if (upgrade.getItem() == BlocksSubscriberHandler.SPEED_UPGRADE) {
								this.speedUpgrades = count;
							}
						}
					}
				}
			}
		}
	}

	private boolean canSift() {
		ItemStack inputStack = inventory.getStackInSlot(INPUT_SLOT);
		return !inputStack.isEmpty() && hasOutputSpace();
	}

	private boolean hasOutputSpace() {
		for (int i = OUTPUT_START; i < OUTPUT_END; i++) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private void processSieve() {
		ItemStack consumed = inventory.extractItem(INPUT_SLOT, 1, false);

		List<ItemStack> outputs = new ArrayList<>();
		for (SieveDropData dropData : repository.getDropData(Block.getBlockFromItem(consumed.getItem()))) {
			Random rand = new Random();
			int rate = rand.nextInt(100);
			if (rate < (fortuneUpgrades > 0 ? dropData.getDropRate() * 100 * fortuneUpgrades
					: dropData.getDropRate() * 100)) {
				outputs.add(dropData.getItem());
			}
		}
		for (ItemStack output : outputs) {
			boolean placed = false;
			for (int i = OUTPUT_START; i < OUTPUT_END && !placed; i++) {
				if (inventory.getStackInSlot(i).isEmpty()) {
					inventory.insertItem(i, output, false);
					placed = true;
				}
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

	public ItemStackHandler getInventory() {
		return inventory;
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
		if (index == UPGRADE_SLOT_START - 1 || index == UPGRADE_SLOT_END) {
			if (stack.getItem() != BlocksSubscriberHandler.FORTUNE_UPGRADE
					&& stack.getItem() != BlocksSubscriberHandler.SPEED_UPGRADE) {
				inventory.setStackInSlot(index, ItemStack.EMPTY);
				return;
			}
		}

		inventory.setStackInSlot(index, stack);
		markDirty();
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
