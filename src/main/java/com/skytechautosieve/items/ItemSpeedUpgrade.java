package com.skytechautosieve.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemSpeedUpgrade extends Item {
	public static final String REGISTRY_NAME = "speed_upgrade";

	public ItemSpeedUpgrade() {
		setRegistryName(REGISTRY_NAME);
		setTranslationKey("skytechautosieve.speed_upgrade");
		setCreativeTab(CreativeTabs.MISC);
	}
}
