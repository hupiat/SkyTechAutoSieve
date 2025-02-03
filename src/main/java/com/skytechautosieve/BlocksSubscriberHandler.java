package com.skytechautosieve;

import com.skytechautosieve.generations.BlockGenerateStoneTier1;
import com.skytechautosieve.generations.BlockGenerateStoneTier2;
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

	public static final BlockGenerateStoneTier1 GENERATE_STONE_TIER1 = new BlockGenerateStoneTier1();
	public static final Item GENERATE_STONE_TIER1_ITEM = new ItemBlock(GENERATE_STONE_TIER1)
			.setRegistryName(GENERATE_STONE_TIER1.getRegistryName());

	public static final BlockGenerateStoneTier2 GENERATE_STONE_TIER2 = new BlockGenerateStoneTier2();
	public static final Item GENERATE_STONE_TIER2_ITEM = new ItemBlock(GENERATE_STONE_TIER2)
			.setRegistryName(GENERATE_STONE_TIER2.getRegistryName());

	public static final Item SPEED_UPGRADE = new ItemSpeedUpgrade();
	public static final Item FORTUNE_UPGRADE = new ItemFortuneUpgrade();

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(AUTO_SIEVE, GENERATE_STONE_TIER1, GENERATE_STONE_TIER2);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(AUTO_SIEVE_ITEM, GENERATE_STONE_TIER1_ITEM, GENERATE_STONE_TIER2_ITEM,
				SPEED_UPGRADE, FORTUNE_UPGRADE);
	}
}
