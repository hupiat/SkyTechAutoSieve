package com.skytechautosieve;

import com.skytechautosieve.hud.GUIAdminManagementHUD;
import com.skytechautosieve.sieves.GUIAutoSieve;
import com.skytechautosieve.sieves.TileEntityAutoSieve;
import com.skytechautosieve.utils.ServerUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
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
		displayAutoSieveScreen(event.player);
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

	private void displayAutoSieveScreen(EntityPlayer player) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			Minecraft.getMinecraft().displayGuiScreen(new GUIAutoSieve(player.inventory, new TileEntityAutoSieve()));
		});
	}
}
