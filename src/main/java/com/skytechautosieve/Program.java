package com.skytechautosieve;

import com.skytechautosieve.hud.GUIAdminHUDEventHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "skytechautosieve", name = "SkyTechAutoSieve", version = "1.0.0")
public class Program {

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new GUIAdminHUDEventHandler());
	}
}
