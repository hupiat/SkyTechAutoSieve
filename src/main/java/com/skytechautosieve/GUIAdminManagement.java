package com.skytechautosieve;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GUIAdminManagement extends GuiScreen {

	private final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("modid:textures/gui/example_gui.png");
	private GuiButton exampleButton;

	@Override
	public void initGui() {
		super.initGui();
		System.out.println("TEST GUI");
		this.buttonList.clear();

		int buttonWidth = 100;
		int buttonHeight = 20;
		int xPos = (this.width - buttonWidth) / 2;
		int yPos = (this.height / 2);

		this.buttonList.add(new GuiButton(0, xPos, yPos, buttonWidth, buttonHeight, "Open Interface"));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button == exampleButton) {
			mc.displayGuiScreen(new GUIAdminManagement());
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
