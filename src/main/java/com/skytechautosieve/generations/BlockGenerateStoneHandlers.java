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

public abstract class BlockGenerateStoneHandlers {

	public static void scheduleUpdate(World world, int delayTicks, BlockPos pos, Block instance) {
		world.scheduleUpdate(pos, instance, delayTicks);
	}

	public static void fillChest(World world, BlockPos pos) {
		BlockPos blockBelow = pos.down();
		IBlockState blockStateBelow = world.getBlockState(blockBelow);

		if (blockStateBelow.getBlock() instanceof BlockChest) {
			TileEntity tileEntity = world.getTileEntity(blockBelow);
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
