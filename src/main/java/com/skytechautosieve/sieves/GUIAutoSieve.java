package com.skytechautosieve.sieves;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;

public class GUIAutoSieve extends GuiScreen {
	private final TileEntityAutoSieve tileEntity;

	public GUIAutoSieve(InventoryPlayer playerInv, TileEntityAutoSieve tileEntity) {
		super();
		this.tileEntity = tileEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		fontRenderer.drawString("Auto Sieve", 8, 6, 0x404040);
		fontRenderer.drawString("Energy: " + tileEntity.getEnergyStored(), 8, 16, 0x00FF00);
	}
}
