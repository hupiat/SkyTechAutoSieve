package com.skytechautosieve;

import com.skytechautosieve.sieves.RenderBlockAutoSieve;
import com.skytechautosieve.sieves.TileEntityAutoSieve;
import com.skytechautosieve.sieves.networking.PacketSyncEnergy;
import com.skytechautosieve.sieves.networking.PacketSyncSieveData;
import com.skytechautosieve.sieves.networking.PacketUpdateSieveData;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "skytechautosieve", name = "SkyTechAutoSieve", version = "1.0.1")
public class Program {

	public static Program instance = null;

	public static final SimpleNetworkWrapper NETWORK_SERVER_CHANNEL_SIEVE_DATA = NetworkRegistry.INSTANCE
			.newSimpleChannel("skytechautosieve:server:sieve_data");
	public static final SimpleNetworkWrapper NETWORK_CLIENT_CHANNEL_SIEVE_DATA = NetworkRegistry.INSTANCE
			.newSimpleChannel("skytechautosieve:client:sieve_data");
	public static final SimpleNetworkWrapper NETWORK_CLIENT_CHANNEL_ENERGY = NetworkRegistry.INSTANCE
			.newSimpleChannel("skytechautosieve:client:energy");

	public Program() {
		super();
		instance = this;
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new BlocksSubscriberHandler());
		MinecraftForge.EVENT_BUS.register(new EventsSubscriberHandler());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoSieve.class, new RenderBlockAutoSieve());
		GameRegistry.registerTileEntity(TileEntityAutoSieve.class, new ResourceLocation("skytechautosieve:autosieve"));

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		NETWORK_SERVER_CHANNEL_SIEVE_DATA.registerMessage(PacketUpdateSieveData.Handler.class,
				PacketUpdateSieveData.class, 0, Side.SERVER);
		NETWORK_CLIENT_CHANNEL_SIEVE_DATA.registerMessage(PacketSyncSieveData.Handler.class, PacketSyncSieveData.class,
				1, Side.CLIENT);
		NETWORK_CLIENT_CHANNEL_ENERGY.registerMessage(PacketSyncEnergy.Handler.class, PacketSyncEnergy.class, 2,
				Side.CLIENT);

	}
}
