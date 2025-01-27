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

		// Activation des fonctionnalités de rendu pour assurer la transparence
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_ALPHA_TEST);

		// Facteur de réduction ajusté
		float scaleFactor = 0.1F; // Réduction à 10% de la taille d'origine (128x128 -> 12.8x12.8)
		GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);

		// Ajustement fin du centrage (en tenant compte du facteur de réduction)
		float translateX = (16.0F / scaleFactor - 16.0F) / 2.0F;
		float translateY = (16.0F / scaleFactor - 16.0F) / 2.0F;
		GL11.glTranslatef(-translateX + 74.0f, -translateY + 64.0f, 0.0F);

		// Dessiner la texture 16x16 pixels
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

		// Désactivation des fonctionnalités de rendu pour éviter les conflits
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);

		GL11.glPopMatrix();
	}
}
