package com.skytechautosieve.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class InternalTools {

	public static void waitForTicks(Runnable callback, int ticks) {
		for (int i = 0; i < ticks; i++) {
			if (i < ticks) {
				continue;
			} else {
				callback.run();
			}
		}
	}

	public static boolean isPlayerAdmin(EntityPlayer player) {
		if (Minecraft.getMinecraft().isSingleplayer()) {
			return true;
		}
		if (player == null) {
			return false;
		}
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (server != null) {
			return server.getPlayerList().canSendCommands(player.getGameProfile());
		}
		return false;
	}

}
