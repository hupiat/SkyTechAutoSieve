package com.skytechautosieve;

import com.skytechautosieve.sieves.TileEntityAutoSieve;
import com.skytechautosieve.sieves.networking.PacketSyncEnergy;
import com.skytechautosieve.sieves.networking.PacketUpdateSieveData;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "skytechautosieve", name = "SkyTechAutoSieve", version = "1.0.4")
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

		GameRegistry.registerTileEntity(TileEntityAutoSieve.class, new ResourceLocation("skytechautosieve:autosieve"));

		NetworkHandler.NETWORK_SERVER_CHANNEL_SIEVE_DATA.registerMessage(PacketUpdateSieveData.Handler.class,
				PacketUpdateSieveData.class, 0, Side.SERVER);
		NetworkHandler.NETWORK_SERVER_CHANNEL_ENERGY.registerMessage(PacketSyncEnergy.Handler.class,
				PacketSyncEnergy.class, 1, Side.SERVER);

	}
}
