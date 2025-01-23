package com.skytechautosieve;

import org.lwjgl.input.Keyboard;

import com.skytechautosieve.hud.GUIAdminManagementHUD;
import com.skytechautosieve.sieves.SieveDropDataRepository;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

@Mod.EventBusSubscriber
public class EventsSuscriberHandler {

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

}
