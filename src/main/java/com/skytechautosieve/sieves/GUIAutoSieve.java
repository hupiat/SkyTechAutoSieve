package com.skytechautosieve.sieves;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIAutoSieve extends GuiContainer {
	private static final ResourceLocation TEXTURE = new ResourceLocation("skytechautosieve",
			"textures/gui/auto_sieve.png");

	private int guiLeft;
	private int guiTop;
	private int xSize = 176;
	private int ySize = 221;

	private final InventoryPlayer playerInventory;
	private final TileEntityAutoSieve tileEntity;

	public GUIAutoSieve(InventoryPlayer playerInv, TileEntityAutoSieve tileEntity) {
		super(new ContainerAutoSieve(playerInv, tileEntity));
		this.playerInventory = playerInv;
		this.tileEntity = tileEntity;
	}

	@Override
	public void initGui() {
		this.guiTop = (this.height - this.ySize) / 2;
		this.guiLeft = (this.width - this.xSize) / 2;
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawInterface();
		drawEnergyBar();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRenderer.drawString("Sifting", 8, 6, 0x404040);
		this.fontRenderer.drawString("Inventory", 8, this.ySize - 94, 0x404040);
	}

	private void drawInterface() {
		this.mc.getTextureManager().bindTexture(TEXTURE);
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, xSize, ySize);
	}

	private void drawEnergyBar() {
		int maxEnergy = tileEntity.getMaxEnergyStored();
		int currentEnergy = tileEntity.getEnergyStored();
		int energyHeight = (int) ((currentEnergy / (float) maxEnergy) * 50);

		drawRect(guiLeft + 10, guiTop + 15 + (50 - energyHeight), guiLeft + 26, guiTop + 65, 0xFF00FF00);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
