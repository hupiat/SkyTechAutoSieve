package com.skytechautosieve.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.skytechautosieve.Program;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public abstract class InternalTools {

	private static final Logger LOGGER = Logger.getLogger(InternalTools.class.getSimpleName());

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

	public static Properties readConfig() {
		Properties config = new Properties();
		try (InputStream is = Program.class.getClassLoader().getResourceAsStream("config.properties")) {
			config.load(is);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error while reading config file");
		}
		return config;
	}
}
