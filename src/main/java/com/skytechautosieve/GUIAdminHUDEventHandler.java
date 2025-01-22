package com.skytechautosieve;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GUIAdminHUDEventHandler {

	private boolean guiOpened = false;

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			Minecraft.getMinecraft().displayGuiScreen(new GUIAdminManagementHUD());
		});
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (Minecraft.getMinecraft().player != null && !guiOpened) {
			Minecraft.getMinecraft().displayGuiScreen(new GUIAdminManagementHUD());
			guiOpened = true;
		}
	}
}
