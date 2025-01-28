package com.skytechautosieve.sieves;

import com.skytechautosieve.Program;
import com.skytechautosieve.sieves.networking.PacketSyncEnergy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

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

	// Making the default render invisible as we are using a custom model renderer

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	// Callbacks

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);

		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);

			if (!stack.isEmpty()) {
				spawnAsEntity(worldIn, pos, stack);
			}
		}

		inventory.clear();
		super.breakBlock(worldIn, pos, state);
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
		chargeEnergyThenSendToClient(world, pos);
	}

	public static void chargeEnergyThenSendToClient(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileEntityAutoSieve) {
			TileEntityAutoSieve sieve = (TileEntityAutoSieve) tile;
			int currentEnergy = sieve.getEnergyStored();
			int maxEnergy = TileEntityAutoSieve.MAX_ENERGY;

			boolean poweredByRedstone = world.isBlockPowered(pos);
			boolean receivedEnergy = false;

			for (EnumFacing facing : EnumFacing.values()) {
				TileEntity neighbor = world.getTileEntity(pos.offset(facing));

				if (neighbor != null && neighbor.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
					IEnergyStorage energyStorage = neighbor.getCapability(CapabilityEnergy.ENERGY,
							facing.getOpposite());
					if (energyStorage != null) {
						int energyNeeded = maxEnergy - currentEnergy;

						if (energyNeeded > 0) {
							int extractedEnergy = energyStorage.extractEnergy(energyNeeded, false);
							if (extractedEnergy > 0) {
								sieve.setField(1, currentEnergy + extractedEnergy);
								receivedEnergy = true;
								break;
							}
						}
					}
				}
			}

			if (poweredByRedstone && !receivedEnergy && currentEnergy < maxEnergy) {
				sieve.setField(1, maxEnergy);
				receivedEnergy = true;
				System.out.println("Fully charged by redstone signal.");
			}

			if (receivedEnergy && sieve.getEnergyStored() != currentEnergy) {
				Program.NETWORK_CLIENT_CHANNEL_ENERGY.sendToAll(new PacketSyncEnergy(pos, sieve.getEnergyStored()));
			}
		}
	}
}