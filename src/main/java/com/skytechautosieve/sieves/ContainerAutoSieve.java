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

		this.addSlotToContainer(new Slot(tileEntity, 0, 56, 35));

		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(tileEntity, i + 1, 116 + (i % 3) * 18, 17 + (i / 3) * 18));
		}

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				this.addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
			}
		}

		for (int col = 0; col < 9; col++) {
			this.addSlotToContainer(new Slot(playerInv, col, 8 + col * 18, 142));
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
