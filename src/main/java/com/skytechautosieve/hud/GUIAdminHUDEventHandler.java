package com.skytechautosieve.hud;

import com.skytechautosieve.utils.ServerUtils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GUIAdminHUDEventHandler {

	private boolean guiOpened = false;

	@SubscribeEvent
	public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		if (ServerUtils.isPlayerAdmin(event.player)) {
			guiOpened = false;
			Minecraft.getMinecraft().addScheduledTask(() -> {
				Minecraft.getMinecraft().displayGuiScreen(new GUIAdminManagementHUD());
				guiOpened = true;
			});
		}
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (!guiOpened && Minecraft.getMinecraft().player != null) {
			if (ServerUtils.isPlayerAdmin(Minecraft.getMinecraft().player)) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					Minecraft.getMinecraft().displayGuiScreen(new GUIAdminManagementHUD());
				});
				guiOpened = true;
			}
		}
	}
}
