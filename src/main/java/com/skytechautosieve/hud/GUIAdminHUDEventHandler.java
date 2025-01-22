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
		if (event.player.world.isRemote) { // Ensure its client side
			if (ServerUtils.isPlayerAdmin(event.player)) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					Minecraft.getMinecraft().displayGuiScreen(new GUIAdminManagementHUD());
					guiOpened = true;
				});
			}
		}
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			if (ServerUtils.isPlayerAdmin(Minecraft.getMinecraft().player)) {
				if (Minecraft.getMinecraft().player != null && !guiOpened) {
					Minecraft.getMinecraft().displayGuiScreen(new GUIAdminManagementHUD());
					guiOpened = true;
				}
			}
		}
	}
}
