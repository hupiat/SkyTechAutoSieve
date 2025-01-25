package com.skytechautosieve.sieves;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAutoSieve extends Container {

	private final IInventory tileInventory;

	public ContainerAutoSieve(InventoryPlayer playerInv, TileEntityAutoSieve tileEntity) {
		this.tileInventory = tileEntity;

		// Add tile entity slots (5 rows, 8 columns divided in 2 parts so 4 by each)
		int startX = 8;
		int startY = -10;
		int slotSize = 18;

		int counterLeft = 0;
		int counterRight = 20;
		for (int row = 0; row < 5; row++) {
			startX = 8;
			for (int col = 0; col < 8; col++) {
				if (col == 4) {
					startX = 26;
				}
				this.addSlotToContainer(new Slot(tileEntity, col >= 4 ? counterRight : counterLeft,
						startX + col * slotSize, startY + row * slotSize));
				if (col >= 4) {
					counterRight++;
				} else {
					counterLeft++;
				}
			}
		}

		this.addSlotToContainer(new Slot(tileEntity, 40, 80, 86));

		// Add player inventory slots (3 rows, 9 columns)
		startX = 8;
		startY = 111;

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlotToContainer(
						new Slot(playerInv, col + row * 9 + 9, startX + col * slotSize, startY + row * slotSize));
			}
		}

		// Add player hotbar slots (1 row, 9 columns)
		startY = 169;
		for (int col = 0; col < 9; col++) {
			this.addSlotToContainer(new Slot(playerInv, col, startX + col * slotSize, startY));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return this.tileInventory.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack currentStack = slot.getStack();
			stack = currentStack.copy();

			int tileInventoryStart = 0;
			int tileInventoryEnd = TileEntityAutoSieve.TOTAL_SLOTS;
			int playerInventoryStart = tileInventoryEnd;
			int playerInventoryEnd = tileInventoryEnd + 36;

			if (index < tileInventoryEnd) {
				if (!this.mergeItemStack(currentStack, playerInventoryStart, playerInventoryEnd, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!this.mergeItemStack(currentStack, tileInventoryStart, tileInventoryEnd, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (currentStack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return stack;
	}
}
