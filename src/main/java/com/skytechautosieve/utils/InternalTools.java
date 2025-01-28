package com.skytechautosieve.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

	private static final String CONFIG_FILE = "skytechautosieve_config.properties";

	public static Properties readConfig() {
		return loadConfig();
	}

	public static void eraseConfig(String key) {
		Properties config = loadConfig();

		if (config.containsKey(key)) {
			config.remove(key);
		}

		try (OutputStream os = new FileOutputStream(CONFIG_FILE, false)) {
			config.store(os, "Config file updated");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error while erasing config", e);
		}
	}

	public static void writeConfig(String key, String value) {
		Properties config = loadConfig();

		config.setProperty(key, value);

		try (OutputStream os = new FileOutputStream(CONFIG_FILE, false)) {
			config.store(os, "Config file updated");
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error while writing config", e);
		}
	}

	private static Properties loadConfig() {
		Properties config = new Properties();
		try (InputStream is = Program.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
			config.load(is);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error while loading config file");
		}
		return config;
	}
}
