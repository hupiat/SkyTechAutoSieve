package com.skytechautosieve.sieves.data;

import java.util.Objects;

import net.minecraft.item.ItemStack;

public class SieveDropData {
	private ItemStack item;
	private float dropRate;

	public SieveDropData(ItemStack item, float dropRate) {
		super();
		this.item = item;
		this.dropRate = dropRate;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public float getDropRate() {
		return dropRate;
	}

	public void setDropRate(float dropRate) {
		this.dropRate = dropRate;
	}

	@Override
	public String toString() {
		return "SieveDropData [item=" + item + ", dropRate=" + dropRate + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(dropRate, item);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SieveDropData other = (SieveDropData) obj;
		return Float.floatToIntBits(dropRate) == Float.floatToIntBits(other.dropRate)
				&& Objects.equals(item, other.item);
	}

}
