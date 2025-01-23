package com.skytechautosieve;

import com.skytechautosieve.hud.GUIAdminManagementHUD;
import com.skytechautosieve.utils.ServerUtils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class ScreensEventHandler {

	private boolean guiOpened = false;

	@SubscribeEvent
	public void onClientConnectedToServer(PlayerEvent.PlayerLoggedInEvent event) {
		guiOpened = false;
		if (event.player.world.isRemote && ServerUtils.isPlayerAdmin(event.player)) {
			displayAdminHUDScreen();
		}
	}

	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (!guiOpened && Minecraft.getMinecraft().player != null) {
			if (ServerUtils.isPlayerAdmin(Minecraft.getMinecraft().player)) {
				displayAdminHUDScreen();
			}
		}
	}

	private void displayAdminHUDScreen() {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			Minecraft.getMinecraft().displayGuiScreen(new GUIAdminManagementHUD());
			guiOpened = true;
		});
	}
}
