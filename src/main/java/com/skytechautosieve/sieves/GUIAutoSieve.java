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
		this.fontRenderer.drawString("Input", 8, -20, 0x404040);
		this.fontRenderer.drawString("Output", 98, -20, 0x404040);
	}

	private void drawInterface() {
		this.mc.getTextureManager().bindTexture(TEXTURE);
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, xSize, ySize);
	}

	private void drawEnergyBar() {
		int maxEnergy = tileEntity.getMaxEnergyStored();
		int currentEnergy = tileEntity.getEnergyStored();

		int energyHeight = maxEnergy > 0 ? (int) ((currentEnergy / (float) maxEnergy) * 50) : 0;

		int energyBarX = guiLeft + 82;
		int energyBarY = guiTop + 50 + (50 - energyHeight);
		int energyBarWidth = 11;
		int energyBarMaxHeight = 80;

		drawRect(energyBarX, guiTop + 20, energyBarX + energyBarWidth, guiTop + 20 + energyBarMaxHeight, 0xFF555555);

		if (energyHeight > 0) {
			drawRect(energyBarX, energyBarY, energyBarX + energyBarWidth, guiTop + 20 + energyBarMaxHeight, 0xFF00FF00);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
