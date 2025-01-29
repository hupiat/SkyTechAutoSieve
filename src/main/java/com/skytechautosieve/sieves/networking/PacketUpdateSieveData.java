package com.skytechautosieve.sieves.networking;

import java.util.Set;

import com.skytechautosieve.NetworkHandler;
import com.skytechautosieve.sieves.data.SieveDropData;
import com.skytechautosieve.sieves.data.SieveDropDataRepository;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateSieveData implements IMessage {
	private String blockName;
	private String itemName;
	private boolean isAdding;
	private float dropRate;

	public PacketUpdateSieveData() {
	}

	public PacketUpdateSieveData(String blockName, String itemName, boolean isAdding, float dropRate) {
		this.blockName = blockName;
		this.itemName = itemName;
		this.isAdding = isAdding;
		this.dropRate = dropRate;
	}

	public String getBlockName() {
		return blockName;
	}

	public String getItemName() {
		return itemName;
	}

	public float getDropRate() {
		return dropRate;
	}

	public boolean isAdding() {
		return isAdding;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		blockName = ByteBufUtils.readUTF8String(buf);
		itemName = ByteBufUtils.readUTF8String(buf);
		dropRate = buf.readFloat();
		isAdding = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, blockName);
		ByteBufUtils.writeUTF8String(buf, itemName);
		buf.writeFloat(dropRate);
		buf.writeBoolean(isAdding);
	}

	public static class Handler implements IMessageHandler<PacketUpdateSieveData, IMessage> {
		@Override
		public IMessage onMessage(PacketUpdateSieveData message, MessageContext ctx) {
			ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
				World world = ctx.getServerHandler().player.getServerWorld();
				Block block = Block.getBlockFromName(message.blockName);
				if (block != null) {
					SieveDropDataRepository repo = SieveDropDataRepository.get(world);
					Set<SieveDropData> drops = repo.getDropData(block);
					ItemStack item = new ItemStack(Item.getByNameOrId(message.itemName));
					if (message.isAdding) {
						drops.add(new SieveDropData(item, message.dropRate));
					} else {
						drops.stream().filter(data -> ItemStack.areItemsEqual(item, data.getItem())).findAny()
								.ifPresent(drops::remove);
					}
					repo.setDropData(block, drops, world, true);
					NetworkHandler.NETWORK.sendTo(new PacketSyncSieveData(repo.writeToNBT(new NBTTagCompound())),
							ctx.getServerHandler().player);

				}
			});
			return null;
		}
	}
}
