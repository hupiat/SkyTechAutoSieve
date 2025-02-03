package com.skytechautosieve.generations;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockGenerateStoneTier1 extends Block {

	public BlockGenerateStoneTier1() {
		super(Material.ROCK);
		setTranslationKey("generate_stone_tier1");
		setRegistryName("generate_stone_tier1");
		setCreativeTab(CreativeTabs.MISC);
	}

}
