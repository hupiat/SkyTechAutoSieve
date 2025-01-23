package com.skytechautosieve;

import com.skytechautosieve.sieves.TileEntityAutoSieve;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "skytechautosieve", name = "SkyTechAutoSieve", version = "1.0.0")
public class Program {

	public static Program instance = null;

	public Program() {
		super();
		instance = this;
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ScreensEventHandler());
		MinecraftForge.EVENT_BUS.register(new BlocksSuscriberHandler());
		MinecraftForge.EVENT_BUS.register(new EventsSuscriberHandler());
		GameRegistry.registerTileEntity(TileEntityAutoSieve.class, new ResourceLocation("skytechautosieve:autosieve"));
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}
}
