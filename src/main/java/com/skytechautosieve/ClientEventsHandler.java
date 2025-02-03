package com.skytechautosieve;

import com.skytechautosieve.sieves.TileEntityAutoSieve;
import com.skytechautosieve.sieves.models.BakedModelAutoSieve;
import com.skytechautosieve.sieves.renders.RenderBlockAutoSieve;
import com.skytechautosieve.sieves.renders.RenderItemAutoSieve;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = "skytechautosieve", value = Side.CLIENT)
public class ClientEventsHandler {
	static {
		BlocksSubscriberHandler.AUTO_SIEVE_ITEM.setTileEntityItemStackRenderer(new RenderItemAutoSieve());
	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		event.getModelRegistry().putObject(new ModelResourceLocation("skytechautosieve:auto_sieve", "inventory"),
				new BakedModelAutoSieve());
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoSieve.class, new RenderBlockAutoSieve());

		ModelLoader.setCustomModelResourceLocation(BlocksSubscriberHandler.AUTO_SIEVE_ITEM, 0,
				new ModelResourceLocation("skytechautosieve:auto_sieve", "inventory"));
		ModelLoader.setCustomModelResourceLocation(BlocksSubscriberHandler.GENERATE_STONE_TIER1_ITEM, 0,
				new ModelResourceLocation("skytechautosieve:generate_stone_tier1", "inventory"));
		ModelLoader.setCustomModelResourceLocation(BlocksSubscriberHandler.GENERATE_STONE_TIER2_ITEM, 0,
				new ModelResourceLocation("skytechautosieve:generate_stone_tier2", "inventory"));
		ModelLoader.setCustomModelResourceLocation(BlocksSubscriberHandler.SPEED_UPGRADE, 0,
				new ModelResourceLocation("skytechautosieve:speed_upgrade", "inventory"));
		ModelLoader.setCustomModelResourceLocation(BlocksSubscriberHandler.FORTUNE_UPGRADE, 0,
				new ModelResourceLocation("skytechautosieve:fortune_upgrade", "inventory"));
	}
}