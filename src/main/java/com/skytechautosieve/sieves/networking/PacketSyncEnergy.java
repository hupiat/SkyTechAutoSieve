package com.skytechautosieve.sieves.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PacketSyncEnergy implements IMessage {

	private BlockPos pos;
	private int energyStored;

	public BlockPos getPos() {
		return pos;
	}

	public int getEnergyStored() {
		return energyStored;
	}

	public PacketSyncEnergy() {
	}

	public PacketSyncEnergy(BlockPos pos, int energyStored) {
		this.pos = pos;
		this.energyStored = energyStored;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		this.energyStored = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(energyStored);
	}

	public static class Handler implements IMessageHandler<PacketSyncEnergy, IMessage> {
		@Override
		public IMessage onMessage(PacketSyncEnergy message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				PacketClientHandlers.handleSyncEnergy(message);
			}
			return null;
		}
	}
}
