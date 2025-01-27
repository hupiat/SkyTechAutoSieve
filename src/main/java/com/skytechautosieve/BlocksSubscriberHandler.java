package com.skytechautosieve;

import com.skytechautosieve.items.ItemFortuneUpgrade;
import com.skytechautosieve.items.ItemSpeedUpgrade;
import com.skytechautosieve.sieves.BakedModelAutoSieve;
import com.skytechautosieve.sieves.BlockAutoSieve;
import com.skytechautosieve.sieves.RenderBlockAutoSieve;
import com.skytechautosieve.sieves.RenderItemAutoSieve;
import com.skytechautosieve.sieves.TileEntityAutoSieve;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class BlocksSubscriberHandler {
	public static final BlockAutoSieve AUTO_SIEVE = new BlockAutoSieve();
	public static final Item AUTO_SIEVE_ITEM = new ItemBlock(AUTO_SIEVE).setRegistryName(AUTO_SIEVE.getRegistryName());
	public static final Item SPEED_UPGRADE = new ItemSpeedUpgrade();
	public static final Item FORTUNE_UPGRADE = new ItemFortuneUpgrade();

	static {
		AUTO_SIEVE_ITEM.setTileEntityItemStackRenderer(new RenderItemAutoSieve());
	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		event.getModelRegistry().putObject(new ModelResourceLocation("skytechautosieve:auto_sieve", "inventory"),
				new BakedModelAutoSieve());
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(AUTO_SIEVE);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(AUTO_SIEVE_ITEM, SPEED_UPGRADE, FORTUNE_UPGRADE);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoSieve.class, new RenderBlockAutoSieve());

		ModelLoader.setCustomModelResourceLocation(AUTO_SIEVE_ITEM, 0,
				new ModelResourceLocation("skytechautosieve:auto_sieve", "inventory"));
		ModelLoader.setCustomModelResourceLocation(SPEED_UPGRADE, 0,
				new ModelResourceLocation("skytechautosieve:speed_upgrade", "inventory"));
		ModelLoader.setCustomModelResourceLocation(FORTUNE_UPGRADE, 0,
				new ModelResourceLocation("skytechautosieve:fortune_upgrade", "inventory"));
	}
}
