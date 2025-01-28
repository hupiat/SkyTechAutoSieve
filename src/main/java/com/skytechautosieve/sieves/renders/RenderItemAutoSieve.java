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

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_ALPHA_TEST);

		GL11.glTranslatef(0.5F, 1.1F, 0.5F);
		GL11.glScalef(0.6F, 0.6F, 0.6F);

		GL11.glRotatef(180, 0F, 0F, 1F);

		model.simpleRender(0.0625F);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);

		GL11.glPopMatrix();
	}
}
