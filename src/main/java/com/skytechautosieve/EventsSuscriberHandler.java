package com.skytechautosieve;

import org.lwjgl.input.Keyboard;

import com.skytechautosieve.hud.GUIAdminManagementHUD;
import com.skytechautosieve.sieves.PacketSyncSieveData;
import com.skytechautosieve.sieves.SieveDropDataRepository;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@Mod.EventBusSubscriber
public class EventsSuscriberHandler implements IMessageHandler<PacketSyncSieveData, IMessage> {

	private static final KeyBinding OPEN_SIEVE_GUI = new KeyBinding("key.open_sieve_gui", Keyboard.KEY_A,
			"key.categories.sieve");

	static {
		ClientRegistry.registerKeyBinding(OPEN_SIEVE_GUI);
	}

	@SubscribeEvent
	public static void onKeyInput(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getMinecraft();

		if (OPEN_SIEVE_GUI.isPressed() && mc.currentScreen == null) {
			if (mc.player != null && mc.world != null) {
				mc.addScheduledTask(() -> mc.displayGuiScreen(new GUIAdminManagementHUD()));
			}
		}
	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event) {
		SieveDropDataRepository repository = SieveDropDataRepository.get(event.getWorld());
		if (repository != null) {
			repository.markDirty();
		}
	}

	@SubscribeEvent
	public void onClientConnectedToServer(PlayerEvent.PlayerLoggedInEvent event) {
		if (!event.player.world.isRemote) {
			SieveDropDataRepository repo = SieveDropDataRepository.get(event.player.world);
			if (repo != null) {
				Program.NETWORK_CLIENT_CHANNEL_SIEVE_DATA.sendTo(
						new PacketSyncSieveData(repo.writeToNBT(new NBTTagCompound())), (EntityPlayerMP) event.player);
			}
		}
	}

	@Override
	public IMessage onMessage(PacketSyncSieveData message, MessageContext ctx) {
		if (Minecraft.getMinecraft().world != null) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				SieveDropDataRepository repository = SieveDropDataRepository.get(Minecraft.getMinecraft().world);
				if (repository != null) {
					repository.readFromNBT(message.getData());
				}
			});
		}
		return null;
	}
}
