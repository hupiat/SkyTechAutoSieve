package com.skytechautosieve.sieves;

import com.skytechautosieve.Program;
import com.skytechautosieve.sieves.networking.PacketSyncEnergy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAutoSieve extends Block {

	public BlockAutoSieve() {
		super(Material.IRON);
		setTranslationKey("auto_sieve");
		setRegistryName("auto_sieve");
		setCreativeTab(CreativeTabs.MISC);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityAutoSieve();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			player.openGui(Program.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock,
			BlockPos neighborPos) {
		boolean powered = world.isBlockPowered(pos);
		if (powered) {
			Program.NETWORK_CLIENT_CHANNEL_ENERGY.sendToAll(new PacketSyncEnergy(pos, TileEntityAutoSieve.MAX_ENERGY));
		}
	}
}