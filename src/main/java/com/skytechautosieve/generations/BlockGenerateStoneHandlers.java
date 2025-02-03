package com.skytechautosieve.generations;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockGenerateStoneHandlers {

	public static void scheduleUpdate(World world, int delayTicks, BlockPos pos, Block instance) {
		world.scheduleUpdate(pos, instance, delayTicks);
	}

	public static void fillChest(World world, BlockPos pos) {
		List<BlockPos> positions = new ArrayList<>();
		positions.add(pos.up());
		positions.add(pos.down());
		positions.add(pos.east());
		positions.add(pos.north());
		positions.add(pos.south());
		positions.add(pos.west());

		for (BlockPos position : positions) {
			IBlockState state = world.getBlockState(position);
			if (state.getBlock() instanceof BlockChest) {
				TileEntity tileEntity = world.getTileEntity(position);
				if (tileEntity instanceof TileEntityChest) {
					TileEntityChest chest = (TileEntityChest) tileEntity;

					for (int i = 0; i < chest.getSizeInventory(); i++) {
						ItemStack stackInSlot = chest.getStackInSlot(i);
						if (stackInSlot.isEmpty()) {
							chest.setInventorySlotContents(i, new ItemStack(Item.getItemFromBlock(Blocks.COBBLESTONE), 64));
							break;
						}
					}
				}
			}
		}
	}
}
