package com.skytechautosieve.sieves;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class AutoSieveGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntityAutoSieve te = (TileEntityAutoSieve) world.getTileEntity(new BlockPos(x, y, z));
		return new ContainerAutoSieve(player.inventory, te);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntityAutoSieve te = (TileEntityAutoSieve) world.getTileEntity(new BlockPos(x, y, z));
		return new GUIAutoSieve(player.inventory, te);
	}
}
