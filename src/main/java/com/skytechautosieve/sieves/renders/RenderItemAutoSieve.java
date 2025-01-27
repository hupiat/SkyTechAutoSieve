package com.skytechautosieve.sieves.renders;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderItemAutoSieve extends TileEntityItemStackRenderer {
	private static final ResourceLocation TEXTURE = new ResourceLocation("skytechautosieve",
			"textures/blocks/auto_sieve.png");

	@Override
	public void renderByItem(ItemStack stack) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

		GL11.glPushMatrix();

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_ALPHA_TEST);

		float scaleFactor = 0.1F;
		GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

		float translateX = (16.0F / scaleFactor - 16.0F) / 2.0F;
		float translateY = (16.0F / scaleFactor - 16.0F) / 2.0F;
		GL11.glTranslatef(-translateX + 74.0f, -translateY + 64.0f, 0.0F);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(0.0F, 1.0F); // Haut gauche (inversé)
		GL11.glVertex2f(0, 0);

		GL11.glTexCoord2f(1.0F, 1.0F); // Haut droit (inversé)
		GL11.glVertex2f(16, 0);

		GL11.glTexCoord2f(1.0F, 0.0F); // Bas droit (inversé)
		GL11.glVertex2f(16, 16);

		GL11.glTexCoord2f(0.0F, 0.0F); // Bas gauche (inversé)
		GL11.glVertex2f(0, 16);

		GL11.glEnd();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);

		GL11.glPopMatrix();
	}
}
