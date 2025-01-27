package com.skytechautosieve.hud;

import com.skytechautosieve.management.GUIAdminManagement;
import com.skytechautosieve.utils.InternalTools;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GUIAdminManagementHUD extends GuiScreen {

	private GuiButton adminMenuButton;

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.clear();

		int buttonWidth = 200;
		int buttonHeight = 20;
		int xPos = (this.width - buttonWidth) / 2;
		int yPos = (this.height - buttonHeight) / 2;

		adminMenuButton = new GuiButton(0, xPos, yPos, buttonWidth, buttonHeight, "Open SkyTechAutoSieve");
		this.buttonList.add(adminMenuButton);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button == adminMenuButton && InternalTools.isPlayerAdmin(this.mc.player)) {
			mc.displayGuiScreen(new GUIAdminManagement());
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(this.fontRenderer, "Admin Management", this.width / 2, 20, 0xFFFFFF);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
