package com.skytechautosieve.sieves;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderItemAutoSieve extends TileEntityItemStackRenderer {
	private static final ResourceLocation TEXTURE = new ResourceLocation("skytechautosieve",
			"textures/blocks/auto_sieve.png");

	@Override
	public void renderByItem(ItemStack stack) {
		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

		System.out.println("TEST");
		textureManager.bindTexture(TEXTURE);

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		GlStateManager.scale(1.0F, 1.0F, 1.0F);
		GlStateManager.translate(0.0F, 0.0F, 0.0F);

		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(0, 0, 0, 0, 16, 16);

		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}
