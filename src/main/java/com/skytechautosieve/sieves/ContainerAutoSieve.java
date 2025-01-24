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

		// Add tile entity slots (6 rows, 8 columns divided in 2 parts so 4 by each)
		int startX = 8;
		int startY = -10;
		int slotSize = 18;

		for (int row = 0; row < 6; row++) {
			startX = 8;
			for (int col = 0; col < 8; col++) {
				if (col == 4) {
					startX = 26;
				}
				this.addSlotToContainer(
						new Slot(tileEntity, col + row * 6, startX + col * slotSize, startY + row * slotSize));
			}
		}

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

			if (index < tileInventory.getSizeInventory()) {
				if (!this.mergeItemStack(currentStack, tileInventory.getSizeInventory(), this.inventorySlots.size(),
						true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(currentStack, 0, tileInventory.getSizeInventory(), false)) {
				return ItemStack.EMPTY;
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
