package com.skytechautosieve.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemFortuneUpgrade extends Item {
	public static final String REGISTRY_NAME = "fortune_upgrade";

	public ItemFortuneUpgrade() {
		setRegistryName(REGISTRY_NAME);
		setTranslationKey("skytechautosieve.fortune_upgrade");
		setCreativeTab(CreativeTabs.MISC);
	}
}
