package com.skytechautosieve.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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

	public static IBlockState getStateFromMeta(String blockName, int meta) {
		Block block = Block.getBlockFromName(blockName);

		Collection<IBlockState> validStates = block.getBlockState().getValidStates();

		for (IBlockState state : validStates) {
			int stateMeta = block.getMetaFromState(state);
			if (stateMeta == meta) {
				return state;
			}
		}

		return block.getDefaultState();
	}

	public static final String CONFIG_PROCESS_TIME = "process_time_seconds";
	public static final String CONFIG_PROCESS_TIME_STONE_TIER1 = "process_time_stone_tier1_seconds";
	public static final String CONFIG_PROCESS_TIME_STONE_TIER2 = "process_time_stone_tier2_seconds";
	private static final String CONFIG_FILE = "config/skytechautosieve_config.properties";

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

	public static Properties loadConfig() {
		Properties config = new Properties();
		File file = new File(CONFIG_FILE);

		try {
			if (!file.exists()) {
				LOGGER.info("Config file not found, creating a new one...");
				if (!file.createNewFile()) {
					LOGGER.severe("Error while creating config file");
				}
			}

			try (InputStream is = new FileInputStream(file)) {
				config.load(is);
				LOGGER.info("Config file loaded successfully.");
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error while loading config file", e);
		}

		return config;
	}

}
