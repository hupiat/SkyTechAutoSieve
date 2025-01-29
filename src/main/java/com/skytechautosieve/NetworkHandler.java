package com.skytechautosieve;

import com.skytechautosieve.sieves.networking.PacketSyncEnergy;
import com.skytechautosieve.sieves.networking.PacketSyncSieveData;
import com.skytechautosieve.sieves.networking.PacketUpdateSieveData;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("skytechautosieve");
	private static int packetId = 0;

	public static void registerMessages() {
		NETWORK.registerMessage(PacketSyncSieveData.Handler.class, PacketSyncSieveData.class, packetId++, Side.CLIENT);
		NETWORK.registerMessage(PacketSyncEnergy.Handler.class, PacketSyncEnergy.class, packetId++, Side.CLIENT);
		NETWORK.registerMessage(PacketUpdateSieveData.Handler.class, PacketUpdateSieveData.class, packetId++,
				Side.SERVER);
	}
}
