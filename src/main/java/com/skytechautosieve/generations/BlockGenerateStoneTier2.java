package com.skytechautosieve.generations;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockGenerateStoneTier2 extends Block {

	public BlockGenerateStoneTier2() {
		super(Material.ROCK);
		setTranslationKey("generate_stone_tier2");
		setRegistryName("generate_stone_tier2");
		setCreativeTab(CreativeTabs.MISC);
	}
}
