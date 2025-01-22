package com.skytechautosieve.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GUIAdminManagement extends GuiScreen {

	private SieveDropDataRepository repository = new SieveDropDataRepository();

	private GuiButton addOrRemoveButton;
	private GuiSlider rateSlider;

	private List<Block> availableBlocks;
	private List<ItemStack> availableDrops;

	private int selectedBlockIndex = -1;
	private int selectedDropIndex = -1;

	private float dropRate = 0.5f;

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	// Render

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
		for (ResourceLocation itemId : Item.REGISTRY.getKeys()) {
			Item item = Item.REGISTRY.getObject(itemId);
			if (item != null) {
				availableDrops.add(new ItemStack(item));
			}
		}

		this.addOrRemoveButton = new GuiButton(0, this.width / 2 - 50, this.height - 30, 100, 20, "Add or Remove");

		this.rateSlider = new GuiSlider(2, this.width / 2 - 50, this.height - 90, 100, 20, "Drop Rate: ", "", 0.0F,
				1.0F, dropRate, true, true, (slider) -> {
					dropRate = (float) slider.getValue();
				});
	}

	private int scrollOffset = 0;
	private int visibleItems = 10;
	private int itemHeight = 20;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		drawBlocks();
		if (selectedBlockIndex >= 0) {
			drawDrops();
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRenderer, "Select Block and Drop", this.width / 2, 20, 0xFFFFFF);
	}

	private void drawBlocks() {
		for (int i = scrollOffset; i < Math.min(scrollOffset + visibleItems, availableBlocks.size()); i++) {
			Block block = availableBlocks.get(i);
			ItemStack stack = new ItemStack(block);
			String name = block.getLocalizedName();
			int drawY = 50 + (i - scrollOffset) * itemHeight;

			if (i == selectedBlockIndex) {
				drawRect(0, drawY - 5, 140, drawY + 15, 0x80FFFFFF);
			}

			this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, drawY - 5);
			this.fontRenderer.drawSplitString(name, 30, drawY, 110, 0XFFFFFF);
		}

	}

	private void drawDrops() {
		for (int i = scrollOffset; i < Math.min(scrollOffset + visibleItems, availableBlocks.size()); i++) {
			ItemStack stack = availableDrops.get(i);
			String name = stack.getDisplayName();
			int drawY = 50 + (i - scrollOffset) * itemHeight;

			if (i == selectedDropIndex) {
				drawRect(this.width / 2 + 75, drawY - 5, this.width / 2 + 225, drawY + 15, 0x80FFFFFF);
			}

			if (repository.getDropData(availableBlocks.get(selectedBlockIndex)).stream()
					.anyMatch(data -> data.getItem().equals(stack))) {
				drawRect(this.width / 2 + 75, drawY - 5, this.width / 2 + 225, drawY + 15, 0x8000FF00);
			}

			this.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, this.width / 2 + 75, drawY - 5);
			this.fontRenderer.drawSplitString(name, this.width / 2 + 105, drawY, 110, 0XFFFFFF);
		}

	}

	// Callbacks

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.equals(addOrRemoveButton)) {
			Block block = availableBlocks.get(selectedBlockIndex);
			List<SieveDropData> dropData = repository.getDropData(block);
			ItemStack dropStack = availableDrops.get(selectedDropIndex);
			Optional<SieveDropData> existing = dropData.stream().filter(data -> data.getItem().equals(dropStack))
					.findAny();
			if (existing.isPresent()) {
				dropData.remove(existing.get());
			} else {
				dropData.add(new SieveDropData(dropStack, (float) rateSlider.getValue()));
			}
			repository.setDropData(block, dropData);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		int blockStartY = 50;
		int dropStartY = 50;
		int blockStartX = 30;
		int dropStartX = this.width / 2 + 75;

		int newSelectedBlockIndex = -1, newSelectedDropIndex = 1;
		for (int i = 0; i < visibleItems; i++) {
			int drawY = blockStartY + i * itemHeight;

			if (mouseX >= blockStartX && mouseX <= blockStartX + 140 && mouseY >= drawY
					&& mouseY <= drawY + itemHeight) {
				newSelectedBlockIndex = scrollOffset + i;
				if (newSelectedBlockIndex == selectedBlockIndex) {
					selectedBlockIndex = -1;
					this.buttonList.clear();
				} else {
					selectedBlockIndex = newSelectedBlockIndex;
				}

			}

			drawY = dropStartY + i * itemHeight;
			if (mouseX >= dropStartX && mouseX <= dropStartX + 140 && mouseY >= drawY && mouseY <= drawY + itemHeight) {
				newSelectedDropIndex = scrollOffset + i;
				if (newSelectedDropIndex == selectedDropIndex) {
					selectedDropIndex = -1;
					this.buttonList.clear();
				} else {
					selectedDropIndex = newSelectedDropIndex;
					this.buttonList.add(addOrRemoveButton);
					this.buttonList.add(rateSlider);
					Optional<SieveDropData> existing = repository.getDropData(availableBlocks.get(selectedBlockIndex))
							.stream().filter(data -> data.getItem().equals(availableDrops.get(selectedDropIndex)))
							.findAny();
					if (existing.isPresent()) {
						this.rateSlider.setValue(existing.get().getDropRate());
						this.rateSlider.displayString = "Drop Rate: "
								+ String.format("%.1f", existing.get().getDropRate());
					}
				}

			}
		}
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
}
