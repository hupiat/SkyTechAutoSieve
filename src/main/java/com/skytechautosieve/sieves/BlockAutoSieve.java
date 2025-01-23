package com.skytechautosieve.sieves;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAutoSieve extends Block {

	public BlockAutoSieve() {
		super(Material.GRASS);
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
			SieveDropDataRepository repository = SieveDropDataRepository.get(world);
			BlockPos targetPos = pos.offset(facing);
			IBlockState targetState = world.getBlockState(targetPos);
			Block targetBlock = targetState.getBlock();

			world.destroyBlock(targetPos, false);

			List<ItemStack> drops = repository.getDropData(targetBlock).stream().map(SieveDropData::getItem)
					.collect(Collectors.toList());

			for (ItemStack drop : drops) {
				player.inventory.addItemStackToInventory(drop);
				if (!player.inventory.addItemStackToInventory(drop)) {
					world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, drop));
				}
			}

			ItemStack heldItem = player.getHeldItem(hand);
			if (!player.isCreative()) {
				heldItem.damageItem(1, player);
				if (heldItem.getCount() <= 0) {
					player.setHeldItem(hand, ItemStack.EMPTY);
				}
			}

			return true;
		}
		return false;
	}
}