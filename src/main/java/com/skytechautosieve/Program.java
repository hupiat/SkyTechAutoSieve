package com.skytechautosieve;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = "skytechautosieve", name = "SkyTechAutoSieve", version = "1.0.0")
public class Program {
	@Mod.Instance
	public static Program instance;

	public static final Logger LOGGER = LogManager.getLogger(Program.class.getSimpleName());

	/**
	 * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
	 * Take a look at how many FMLStateEvents you can listen to via
	 * the @Mod.EventHandler annotation here </a>
	 */
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER.log(Level.INFO, "Hello from {} !", "SkyTechAutoSieve");
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		LOGGER.info("Initializing GUI handler...");
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIHandler());
	}
}
