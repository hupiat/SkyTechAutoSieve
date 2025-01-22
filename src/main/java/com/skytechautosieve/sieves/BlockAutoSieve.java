package com.skytechautosieve.sieves;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAutoSieve extends BlockContainer {

	public BlockAutoSieve() {
		super(Material.GRASS);
		setTranslationKey("auto_sieve");
		setRegistryName("auto_sieve");
		setCreativeTab(CreativeTabs.MISC);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityAutoSieve();
	}
}