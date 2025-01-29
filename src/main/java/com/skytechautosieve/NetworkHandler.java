package com.skytechautosieve;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetworkHandler {

	public static final SimpleNetworkWrapper NETWORK_CLIENT_CHANNEL_SIEVE_DATA = NetworkRegistry.INSTANCE
			.newSimpleChannel("skytechautosieve:client:sieve_data");
	public static final SimpleNetworkWrapper NETWORK_CLIENT_CHANNEL_ENERGY = NetworkRegistry.INSTANCE
			.newSimpleChannel("skytechautosieve:client:energy");
	public static final SimpleNetworkWrapper NETWORK_SERVER_CHANNEL_SIEVE_DATA = NetworkRegistry.INSTANCE
			.newSimpleChannel("skytechautosieve:server:sieve_data");
	public static final SimpleNetworkWrapper NETWORK_SERVER_CHANNEL_ENERGY = NetworkRegistry.INSTANCE
			.newSimpleChannel("skytechautosieve:server:energy");
}
