package com.skytechautosieve;

import org.lwjgl.input.Keyboard;

import com.skytechautosieve.hud.GUIAdminManagementHUD;
import com.skytechautosieve.sieves.AutoSieveGuiHandler;
import com.skytechautosieve.sieves.TileEntityAutoSieve;
import com.skytechautosieve.sieves.models.BakedModelAutoSieve;
import com.skytechautosieve.sieves.networking.PacketSyncEnergy;
import com.skytechautosieve.sieves.networking.PacketSyncSieveData;
import com.skytechautosieve.sieves.renders.RenderBlockAutoSieve;
import com.skytechautosieve.sieves.renders.RenderItemAutoSieve;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = "skytechautosieve", value = Side.CLIENT)
public class ClientEventsHandler {
	public static final SimpleNetworkWrapper NETWORK_CLIENT_CHANNEL_SIEVE_DATA = NetworkRegistry.INSTANCE
			.newSimpleChannel("skytechautosieve:client:sieve_data");
	public static final SimpleNetworkWrapper NETWORK_CLIENT_CHANNEL_ENERGY = NetworkRegistry.INSTANCE
			.newSimpleChannel("skytechautosieve:client:energy");
	private static final KeyBinding OPEN_SIEVE_GUI = new KeyBinding("key.open_sieve_gui", Keyboard.KEY_A,
			"key.categories.sieve");

	static {
		BlocksSubscriberHandler.AUTO_SIEVE_ITEM.setTileEntityItemStackRenderer(new RenderItemAutoSieve());
		ClientRegistry.registerKeyBinding(OPEN_SIEVE_GUI);
		NetworkRegistry.INSTANCE.registerGuiHandler(Program.instance, new AutoSieveGuiHandler());
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
	public static void onModelBake(ModelBakeEvent event) {
		event.getModelRegistry().putObject(new ModelResourceLocation("skytechautosieve:auto_sieve", "inventory"),
				new BakedModelAutoSieve());
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoSieve.class, new RenderBlockAutoSieve());

		NETWORK_CLIENT_CHANNEL_SIEVE_DATA.registerMessage(PacketSyncSieveData.Handler.class, PacketSyncSieveData.class,
				0, Side.CLIENT);
		NETWORK_CLIENT_CHANNEL_ENERGY.registerMessage(PacketSyncEnergy.Handler.class, PacketSyncEnergy.class, 1,
				Side.CLIENT);

		ModelLoader.setCustomModelResourceLocation(BlocksSubscriberHandler.AUTO_SIEVE_ITEM, 0,
				new ModelResourceLocation("skytechautosieve:auto_sieve", "inventory"));
		ModelLoader.setCustomModelResourceLocation(BlocksSubscriberHandler.SPEED_UPGRADE, 0,
				new ModelResourceLocation("skytechautosieve:speed_upgrade", "inventory"));
		ModelLoader.setCustomModelResourceLocation(BlocksSubscriberHandler.FORTUNE_UPGRADE, 0,
				new ModelResourceLocation("skytechautosieve:fortune_upgrade", "inventory"));
	}
}