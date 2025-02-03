package com.skytechautosieve;

import com.skytechautosieve.sieves.data.SieveDropDataRepository;
import com.skytechautosieve.sieves.packets.PacketSyncSieveData;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = "skytechautosieve")
public class EventsSubscriberHandler {
	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event) {
		SieveDropDataRepository repository = SieveDropDataRepository.get(event.getWorld());
		if (repository != null) {
			repository.markDirty();
		}
	}

	@SubscribeEvent
	public void onClientConnectedToServer(PlayerEvent.PlayerLoggedInEvent event) {
		SieveDropDataRepository repo = SieveDropDataRepository.get(event.player.world);
		if (repo != null) {
			NetworkHandler.NETWORK.sendTo(new PacketSyncSieveData(repo.writeToNBT(new NBTTagCompound())),
					(EntityPlayerMP) event.player);
		}
	}
}
