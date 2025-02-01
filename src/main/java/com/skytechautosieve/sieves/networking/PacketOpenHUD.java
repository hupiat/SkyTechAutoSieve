package com.skytechautosieve.sieves.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketOpenHUD implements IMessage {

	public PacketOpenHUD() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<PacketOpenHUD, IMessage> {
		@Override
		public IMessage onMessage(PacketOpenHUD message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				PacketClientHandlers.handleOpenHUD();
			}
			return null;
		}
	}
}
