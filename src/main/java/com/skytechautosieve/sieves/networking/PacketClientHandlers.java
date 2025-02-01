package com.skytechautosieve.sieves.networking;

import com.skytechautosieve.hud.GUIAdminManagementHUD;
import com.skytechautosieve.sieves.TileEntityAutoSieve;
import com.skytechautosieve.sieves.data.SieveDropDataRepository;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class PacketClientHandlers {

	@SideOnly(Side.CLIENT)
	public static void handleSyncEnergy(PacketSyncEnergy message) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			if (Minecraft.getMinecraft().world != null) {
				TileEntityAutoSieve tile = (TileEntityAutoSieve) Minecraft.getMinecraft().world
						.getTileEntity(message.getPos());
				if (tile != null) {
					tile.setField(1, message.getEnergyStored());
				}
			}
		});
	}

	@SideOnly(Side.CLIENT)
	public static void handleSyncSieveData(PacketSyncSieveData message) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			World world = Minecraft.getMinecraft().world;
			if (world != null) {
				SieveDropDataRepository repository = SieveDropDataRepository.get(world);
				if (repository != null) {
					repository.readFromNBT(message.getData());
				}
			}
		});
	}

	@SideOnly(Side.CLIENT)
	public static void handleOpenHUD() {
		Minecraft.getMinecraft().addScheduledTask(new Runnable() {
			@Override
			public void run() {
				Minecraft.getMinecraft().displayGuiScreen(new GUIAdminManagementHUD());
			}
		});
	}
}
