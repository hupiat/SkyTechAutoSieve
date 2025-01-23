package com.skytechautosieve.sieves;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIAutoSieve extends GuiScreen {
	private static final ResourceLocation TEXTURE = new ResourceLocation("skytechautosieve",
			"textures/gui/auto_sieve.png");

	private int guiLeft;
	private int guiTop;
	private int xSize = 176;
	private int ySize = 166;

	private final TileEntityAutoSieve tileEntity;

	public GUIAutoSieve(InventoryPlayer playerInv, TileEntityAutoSieve tileEntity) {
		super();
		this.tileEntity = tileEntity;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.guiTop = (this.height - this.ySize) / 2;
		this.guiLeft = (this.width - this.xSize) / 2;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		drawInterface();
		drawEnergyBar();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void drawInterface() {
		GL11.glPushMatrix();
		GL11.glScalef(0.75f, 0.75f, 0.75f);
		this.mc.getTextureManager().bindTexture(TEXTURE);
		this.drawTexturedModalRect(guiLeft + 40, guiTop, 0, 0, xSize + 80, ySize + 90);
		GL11.glPopMatrix();

	}

	private void drawEnergyBar() {
		int energyHeight = (int) ((tileEntity.getEnergyStored() / (float) tileEntity.getMaxEnergyStored()) * 50);
		drawRect(guiLeft + 10, guiTop + 15 + (50 - energyHeight), guiLeft + 26, guiTop + 65, 0xFF00FF00);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
