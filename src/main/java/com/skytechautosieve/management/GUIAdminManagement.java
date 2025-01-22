package com.skytechautosieve.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GUIAdminManagement extends GuiScreen {

	private GuiButton confirmButton;
	private GuiButton cancelButton;
	private GuiSlider rateSlider;

	private List<Block> availableBlocks;
	private List<ItemStack> availableDrops;

	private Block selectedBlock;
	private ItemStack selectedDrop;

	private float dropRate = 0.5f;

	@Override
	public void initGui() {
		super.initGui();
		availableBlocks = new ArrayList<>();
		for (ResourceLocation blockId : Block.REGISTRY.getKeys()) {
			Block block = Block.REGISTRY.getObject(blockId);
			if (block != null) {
				availableBlocks.add(block);
			}
		}

		availableDrops = new ArrayList<>();
		for (ItemStack item : Minecraft.getMinecraft().player.inventory.mainInventory) {
			availableDrops.add(item);
		}

		this.confirmButton = new GuiButton(0, this.width / 2 - 50, this.height - 30, 100, 20, "Confirm");
		this.cancelButton = new GuiButton(1, this.width / 2 - 50, this.height - 60, 100, 20, "Cancel");

		this.buttonList.add(confirmButton);
		this.buttonList.add(cancelButton);

		this.rateSlider = new GuiSlider(2, this.width / 2 - 50, this.height - 90, 100, 20, "Drop Rate: ", "", 0.0F,
				1.0F, dropRate, true, true, (slider) -> {
					dropRate = (float) slider.getValue();
				});

		this.buttonList.add(rateSlider);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button == confirmButton) {
			// selectedBlock = availableBlocks.get(blockList.getSelectedIndex());
			// selectedDrop = availableDrops.get(dropList.getSelectedIndex());
			Minecraft.getMinecraft().player
					.sendMessage(new TextComponentString("Selected: " + selectedBlock.getLocalizedName() + " -> "
							+ selectedDrop.getDisplayName() + " at rate " + dropRate));
			this.mc.displayGuiScreen(null);
		} else if (button == cancelButton) {
			this.mc.displayGuiScreen(null);
		}
	}

	private int scrollOffset = 0;
	private int visibleItems = 10;
	private int itemHeight = 20;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		for (int i = scrollOffset; i < Math.min(scrollOffset + visibleItems, availableBlocks.size()); i++) {
			Block block = availableBlocks.get(i);
			ItemStack stack = new ItemStack(block);
			String name = block.getLocalizedName();
			int drawY = 50 + (i - scrollOffset) * itemHeight;

			this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, drawY - 5);
			this.fontRenderer.drawSplitString(name, 30, drawY, 150, 0XFFFFFF);
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRenderer, "Select Block and Drop", this.width / 2, 20, 0xFFFFFF);
	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int delta = Integer.signum(org.lwjgl.input.Mouse.getDWheel());

		if (delta < 0) {
			if (scrollOffset + visibleItems < availableBlocks.size()) {
				scrollOffset++;
			}
		} else if (delta > 0) {
			if (scrollOffset > 0) {
				scrollOffset--;
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
