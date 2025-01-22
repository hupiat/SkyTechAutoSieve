package com.skytechautosieve.management;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class GUIBlocksList extends GuiSlot {
	private final List<Block> blocks;
	private final Minecraft mc;
	private int selectedIndex = -1;

	public GUIBlocksList(Minecraft mc, List<Block> blocks, int x, int y, int width, int height) {
		super(mc, width, height, y, y + height, 20);
		this.mc = mc;
		this.blocks = blocks;
	}

	@Override
	protected int getSize() {
		return blocks.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick, int mouseX, int mouseY) {
		selectedIndex = index;
	}

	@Override
	protected boolean isSelected(int index) {
		return index == selectedIndex;
	}

	@Override
	protected void drawBackground() {
	}

	@Override
	protected void drawSlot(int slotIndex, int x, int y, int height, int mouseX, int mouseY, float partialTicks) {
		Block block = blocks.get(slotIndex);
		ItemStack stack = new ItemStack(block);

		// Render block icon
		GlStateManager.enableRescaleNormal();
		mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
		mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, stack, x, y, null);

		// Draw block name
		mc.fontRenderer.drawString(block.getLocalizedName(), x + 22, y + 5,
				isSelected(slotIndex) ? 0x00FF00 : 0xFFFFFF);
	}

	public Block getSelectedBlock() {
		return (selectedIndex >= 0 && selectedIndex < blocks.size()) ? blocks.get(selectedIndex) : null;
	}
}
