package com.skytechautosieve.management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.skytechautosieve.NetworkHandler;
import com.skytechautosieve.sieves.data.SieveDropData;
import com.skytechautosieve.sieves.data.SieveDropDataRepository;
import com.skytechautosieve.sieves.networking.PacketUpdateSieveData;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.client.config.GuiSlider;

public class GUIAdminManagement extends GuiScreen {

	private SieveDropDataRepository repository = null;

	private GuiButton addOrRemoveButton;
	private GuiSlider rateSlider;
	private GuiTextField searchBlocksField;
	private GuiTextField searchDropsField;

	private List<Block> availableBlocks;
	private List<ItemStack> availableDrops;

	private List<Block> filteredBlocks;
	private List<ItemStack> filteredDrops;

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
		repository = SieveDropDataRepository.get(this.mc.world);

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

		this.rateSlider = new GuiSlider(1, this.width / 2 - 50, this.height - 90, 100, 20, "Drop Rate: ", "", 0.0F,
				1.0F, dropRate, true, true, (slider) -> {
					dropRate = (float) slider.getValue();
				});

		this.searchBlocksField = new GuiTextField(2, fontRenderer, 0 + 20, 10, 100, 20);
		this.searchDropsField = new GuiTextField(3, fontRenderer, this.width - 100 - 20, 10, 100, 20);
	}

	private int scrollOffset = 0;
	private int visibleItems = Minecraft.getMinecraft().gameSettings.guiScale == 1
			|| Minecraft.getMinecraft().gameSettings.guiScale == 2 ? 20 : 10;
	private int itemHeight = 20;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, "Select Block and Drop", this.width / 2, 20, 0xFFFFFF);
		this.searchBlocksField.drawTextBox();
		this.searchDropsField.drawTextBox();
		this.searchBlocksField.updateCursorCounter();
		this.searchDropsField.updateCursorCounter();
		drawBlocks();
		if (selectedBlockIndex >= 0) {
			drawDrops();
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void drawBlocks() {
		filteredBlocks = availableBlocks.stream()
				.filter(block -> isMatchingSearchQuery(block.getLocalizedName(), searchBlocksField))
				.collect(Collectors.toList());

		for (int i = scrollOffset; i < Math.min(scrollOffset + visibleItems, filteredBlocks.size()); i++) {
			Block block = filteredBlocks.get(i);
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
		filteredDrops = this.availableDrops.stream()
				.filter(drop -> isMatchingSearchQuery(drop.getDisplayName(), searchDropsField))
				.collect(Collectors.toList());

		for (int i = scrollOffset; i < Math.min(scrollOffset + visibleItems, filteredDrops.size()); i++) {
			ItemStack item = filteredDrops.get(i);
			String name = item.getDisplayName();
			int drawY = 50 + (i - scrollOffset) * itemHeight;

			if (i == selectedDropIndex) {
				drawRect(this.width - 30 - 110, drawY - 5, this.width, drawY + 15, 0x80FFFFFF);
			}

			System.out.println(repository.getDropData(filteredBlocks.get(selectedBlockIndex)));
			if (repository.getDropData(filteredBlocks.get(selectedBlockIndex)).stream()
					.anyMatch(data -> ItemStack.areItemsEqual(item, data.getItem()))) {
				drawRect(this.width - 30 - 110, drawY - 5, this.width, drawY + 15, 0x8000FF00);
			}

			this.mc.getRenderItem().renderItemAndEffectIntoGUI(item, this.width - 30, drawY - 5);
			this.fontRenderer.drawSplitString(name, this.width - 30 - 110, drawY, 110, 0XFFFFFF);
		}
	}

	private boolean isMatchingSearchQuery(String name, GuiTextField searchField) {
		String query = searchField.getText().trim().toLowerCase();
		if (StringUtils.isNullOrEmpty(query)) {
			return true;
		}
		return name.toLowerCase().contains(query);
	}

	// Callbacks

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.equals(addOrRemoveButton)) {
			Block block = filteredBlocks.get(selectedBlockIndex);
			Set<SieveDropData> dropData = repository.getDropData(block);
			ItemStack item = filteredDrops.get(selectedDropIndex);
			boolean existing = dropData.stream().anyMatch(data -> ItemStack.areItemsEqual(data.getItem(), item));
			NetworkHandler.NETWORK.sendToServer(new PacketUpdateSieveData(block.getRegistryName().toString(),
					item.getItem().getRegistryName().toString(), !existing, dropRate));
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.searchBlocksField.isFocused()) {
			this.searchBlocksField.textboxKeyTyped(typedChar, keyCode);

			if (!StringUtils.isNullOrEmpty(searchBlocksField.getText())) {
				selectedBlockIndex = -1;
				this.buttonList.clear();
			}
		} else if (this.searchDropsField.isFocused()) {
			this.searchDropsField.textboxKeyTyped(typedChar, keyCode);

			if (!StringUtils.isNullOrEmpty(searchDropsField.getText())) {
				selectedDropIndex = -1;
				this.buttonList.clear();
			}
		}
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		searchBlocksField.mouseClicked(mouseX, mouseY, mouseButton);
		searchDropsField.mouseClicked(mouseX, mouseY, mouseButton);

		int blockStartY = 50;
		int dropStartY = 50;
		int blockStartX = 0;
		int dropStartX = this.width - 140;

		int newSelectedBlockIndex = -1, newSelectedDropIndex = 1;
		for (int i = 0; i < visibleItems; i++) {
			int drawY = blockStartY + i * itemHeight;

			if (mouseX >= blockStartX && mouseX <= blockStartX + 140 && mouseY >= drawY
					&& mouseY <= drawY + itemHeight) {
				newSelectedBlockIndex = scrollOffset + i;
				if (newSelectedBlockIndex == selectedBlockIndex) {
					selectedBlockIndex = -1;
				} else {
					selectedBlockIndex = newSelectedBlockIndex;
				}
				selectedDropIndex = -1;
				this.buttonList.clear();

			}

			drawY = dropStartY + i * itemHeight;
			if (mouseX >= dropStartX && mouseX <= dropStartX + 140 && mouseY >= drawY && mouseY <= drawY + itemHeight) {
				newSelectedDropIndex = scrollOffset + i;
				if (newSelectedDropIndex == selectedDropIndex) {
					selectedDropIndex = -1;
					this.buttonList.clear();
				} else {
					selectedDropIndex = newSelectedDropIndex;
					if (this.buttonList.isEmpty()) {
						this.buttonList.add(addOrRemoveButton);
						this.buttonList.add(rateSlider);
					}
					Optional<SieveDropData> existing = repository.getDropData(filteredBlocks.get(selectedBlockIndex))
							.stream().filter(data -> ItemStack.areItemsEqual(filteredDrops.get(selectedDropIndex),
									data.getItem()))
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
