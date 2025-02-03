package com.skytechautosieve.generations;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGenerateStoneTier1 extends Block {

	public BlockGenerateStoneTier1() {
		super(Material.ROCK);
		setTranslationKey("generate_stone_tier1");
		setRegistryName("generate_stone_tier1");
		setCreativeTab(CreativeTabs.MISC);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		BlockGenerateStoneHandlers.scheduleUpdate(world, 20, pos, this);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		BlockGenerateStoneHandlers.fillChest(world, pos);
		BlockGenerateStoneHandlers.scheduleUpdate(world, 20, pos, this);
	}
}
