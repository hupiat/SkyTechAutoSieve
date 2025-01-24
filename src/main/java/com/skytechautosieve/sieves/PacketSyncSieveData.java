package com.skytechautosieve.sieves;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncSieveData implements IMessage {

	private NBTTagCompound data;

	public PacketSyncSieveData() {
	}

	public PacketSyncSieveData(NBTTagCompound data) {
		this.data = data;
	}

	public NBTTagCompound getData() {
		return data;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			CompressedStreamTools.writeCompressed(data, output);
			byte[] compressedData = output.toByteArray();

			int chunkSize = 1024; // Define chunk size
			int totalChunks = (compressedData.length + chunkSize - 1) / chunkSize; // Calculate total chunks

			buf.writeInt(totalChunks); // Write total number of chunks

			for (int i = 0; i < totalChunks; i++) {
				int start = i * chunkSize;
				int end = Math.min(compressedData.length, start + chunkSize);
				int length = end - start;

				buf.writeInt(length); // Write chunk length correctly
				buf.writeBytes(compressedData, start, length); // Write chunk data
			}

			System.out.println("Sent " + totalChunks + " chunks, total size: " + compressedData.length);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		try {
			int totalChunks = buf.readInt(); // Read total number of chunks
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			for (int i = 0; i < totalChunks; i++) {
				if (buf.readableBytes() < 4) {
					throw new IOException("Not enough bytes to read chunk length.");
				}

				int chunkLength = buf.readInt(); // Read chunk length

				if (chunkLength <= 0 || chunkLength > buf.readableBytes()) {
					throw new IOException("Invalid chunk length: " + chunkLength);
				}

				byte[] chunk = new byte[chunkLength];
				buf.readBytes(chunk); // Read chunk bytes
				output.write(chunk);
			}

			data = CompressedStreamTools.readCompressed(new ByteArrayInputStream(output.toByteArray()));
			System.out.println("Received and reconstructed " + totalChunks + " chunks.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class Handler implements IMessageHandler<PacketSyncSieveData, IMessage> {
		@Override
		public IMessage onMessage(PacketSyncSieveData message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				World world = Minecraft.getMinecraft().world;
				if (world != null) {
					SieveDropDataRepository repository = SieveDropDataRepository.get(world);
					if (repository != null) {
						repository.readFromNBT(message.getData());
						System.out.println("Data successfully loaded on client side.");
					}
				}
			});
			return null;
		}
	}

}
