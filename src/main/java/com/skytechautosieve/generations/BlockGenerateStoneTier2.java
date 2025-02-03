package com.skytechautosieve.generations;

import java.util.Random;

import com.skytechautosieve.utils.InternalTools;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGenerateStoneTier2 extends Block {
	public static int PROCESS_TIME_REQUIRED = 10;
	static {
		Object processTime = InternalTools.readConfig().get(InternalTools.CONFIG_PROCESS_TIME_STONE_TIER2);
		if (processTime != null) {
			PROCESS_TIME_REQUIRED = (int) Double.parseDouble(processTime.toString()) * 20;
		}
	}

	public BlockGenerateStoneTier2() {
		super(Material.ROCK);
		setTranslationKey("generate_stone_tier2");
		setRegistryName("generate_stone_tier2");
		setCreativeTab(CreativeTabs.MISC);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		BlockGenerateStoneHandlers.scheduleUpdate(world, PROCESS_TIME_REQUIRED, pos, this);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		BlockGenerateStoneHandlers.fillChest(world, pos);
		BlockGenerateStoneHandlers.scheduleUpdate(world, PROCESS_TIME_REQUIRED, pos, this);
	}
}
