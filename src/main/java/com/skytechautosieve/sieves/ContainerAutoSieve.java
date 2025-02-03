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
		int startY = -9;
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

		// Add upgrade slots

		this.addSlotToContainer(new Slot(tileEntity, 40, 68, 88));
		this.addSlotToContainer(new Slot(tileEntity, 41, 92, 88));

		// Add player inventory slots (3 rows, 9 columns)
		startX = 8;
		startY = 112;

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlotToContainer(
						new Slot(playerInv, col + row * 9 + 9, startX + col * slotSize, startY + row * slotSize));
			}
		}

		// Add player hotbar slots (1 row, 9 columns)
		startY = 170;
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
		Slot slot = this.inventorySlots.get(index);
		if (slot == null || !slot.getHasStack()) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getStack();
		ItemStack copy = stack.copy();

		int tileInventorySize = 42;
		int playerInventoryStart = tileInventorySize;
		int playerHotbarStart = playerInventoryStart + 27;
		int playerHotbarEnd = playerHotbarStart + 9;

		if (index < tileInventorySize) {
			if (!this.mergeItemStack(stack, playerInventoryStart, playerHotbarEnd, true)) {
				return ItemStack.EMPTY;
			}
		} else {
			if (!this.mergeItemStack(stack, 0, tileInventorySize, false)) {
				return ItemStack.EMPTY;
			}
		}

		if (stack.isEmpty()) {
			slot.putStack(ItemStack.EMPTY);
		} else {
			slot.onSlotChanged();
		}

		slot.onTake(playerIn, stack);
		return copy;
	}
}
