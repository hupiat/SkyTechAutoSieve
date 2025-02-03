package com.skytechautosieve;

import com.skytechautosieve.sieves.packets.PacketOpenHUD;
import com.skytechautosieve.sieves.packets.PacketSyncEnergy;
import com.skytechautosieve.sieves.packets.PacketSyncSieveData;
import com.skytechautosieve.sieves.packets.PacketUpdateSieveData;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel("skytechautosieve");
	private static int packetId = 0;

	public static void registerMessages() {
		NETWORK.registerMessage(PacketSyncSieveData.Handler.class, PacketSyncSieveData.class, packetId++, Side.CLIENT);
		NETWORK.registerMessage(PacketSyncEnergy.Handler.class, PacketSyncEnergy.class, packetId++, Side.CLIENT);
		NETWORK.registerMessage(PacketOpenHUD.Handler.class, PacketOpenHUD.class, packetId++, Side.CLIENT);
		NETWORK.registerMessage(PacketUpdateSieveData.Handler.class, PacketUpdateSieveData.class, packetId++,
				Side.SERVER);
	}
}
