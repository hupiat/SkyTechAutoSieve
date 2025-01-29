package com.skytechautosieve;

import com.skytechautosieve.items.ItemFortuneUpgrade;
import com.skytechautosieve.items.ItemSpeedUpgrade;
import com.skytechautosieve.sieves.BlockAutoSieve;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = "skytechautosieve")
public class BlocksSubscriberHandler {
	public static final BlockAutoSieve AUTO_SIEVE = new BlockAutoSieve();
	public static final Item AUTO_SIEVE_ITEM = new ItemBlock(AUTO_SIEVE).setRegistryName(AUTO_SIEVE.getRegistryName());
	public static final Item SPEED_UPGRADE = new ItemSpeedUpgrade();
	public static final Item FORTUNE_UPGRADE = new ItemFortuneUpgrade();

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(AUTO_SIEVE);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(AUTO_SIEVE_ITEM, SPEED_UPGRADE, FORTUNE_UPGRADE);
	}
}
