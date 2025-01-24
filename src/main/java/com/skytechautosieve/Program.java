package com.skytechautosieve;

import com.skytechautosieve.sieves.PacketUpdateSieveData;
import com.skytechautosieve.sieves.TileEntityAutoSieve;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "skytechautosieve", name = "SkyTechAutoSieve", version = "1.0.0")
public class Program {

	public static Program instance = null;

	public static final SimpleNetworkWrapper NETWORK_CHANNEL = NetworkRegistry.INSTANCE
			.newSimpleChannel("skytechautosieve");

	public Program() {
		super();
		instance = this;
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new BlocksSuscriberHandler());
		MinecraftForge.EVENT_BUS.register(new EventsSuscriberHandler());
		GameRegistry.registerTileEntity(TileEntityAutoSieve.class, new ResourceLocation("skytechautosieve:autosieve"));
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		NETWORK_CHANNEL.registerMessage(PacketUpdateSieveData.Handler.class, PacketUpdateSieveData.class, 0,
				Side.SERVER);
		// NETWORK_CHANNEL.registerMessage(PacketSyncSieveData.Handler.class,
		// PacketSyncSieveData.class, 0, Side.CLIENT);
	}
}
