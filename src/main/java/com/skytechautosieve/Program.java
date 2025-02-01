package com.skytechautosieve;

import com.skytechautosieve.sieves.AutoSieveGuiHandler;
import com.skytechautosieve.sieves.CommandOpenSieveHUD;
import com.skytechautosieve.sieves.TileEntityAutoSieve;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "skytechautosieve", name = "SkyTechAutoSieve", version = "1.0.6")
public class Program {

	public static Program instance = null;

	public Program() {
		super();
		instance = this;
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new BlocksSubscriberHandler());
		MinecraftForge.EVENT_BUS.register(new EventsSubscriberHandler());

		NetworkRegistry.INSTANCE.registerGuiHandler(Program.instance, new AutoSieveGuiHandler());
		NetworkHandler.registerMessages();
		GameRegistry.registerTileEntity(TileEntityAutoSieve.class, new ResourceLocation("skytechautosieve:autosieve"));
	}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandOpenSieveHUD());
	}
}
