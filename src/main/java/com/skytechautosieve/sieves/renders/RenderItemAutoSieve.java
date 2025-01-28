package com.skytechautosieve.sieves.renders;

import org.lwjgl.opengl.GL11;

import com.skytechautosieve.sieves.models.ModelAutoSieve;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderItemAutoSieve extends TileEntityItemStackRenderer {
	private static final ResourceLocation TEXTURE = new ResourceLocation("skytechautosieve",
			"textures/blocks/auto_sieve.png");

	private final ModelAutoSieve model = new ModelAutoSieve();

	@Override
	public void renderByItem(ItemStack stack) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

		GL11.glPushMatrix();

		GL11.glTranslatef(0.5F, 1.1F, 0.5F);
		GL11.glScalef(0.6F, 0.6F, 0.6F);

		GL11.glRotatef(180, 0F, 0F, 1F);

		model.simpleRender(0.0625F);

		GL11.glPopMatrix();
	}
}
