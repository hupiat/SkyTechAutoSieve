package com.skytechautosieve.sieves.renders;

import org.lwjgl.opengl.GL11;

import com.skytechautosieve.sieves.TileEntityAutoSieve;
import com.skytechautosieve.sieves.models.ModelAutoSieve;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderBlockAutoSieve extends TileEntitySpecialRenderer<TileEntityAutoSieve> {

	private final ModelAutoSieve model = new ModelAutoSieve();

	private static final ResourceLocation TEXTURE = new ResourceLocation(
			"skytechautosieve:textures/blocks/auto_sieve.png");

	@Override
	public void render(TileEntityAutoSieve te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glScalef(-1F, -1F, 1F);

		this.bindTexture(TEXTURE);
		model.simpleRender(0.0625F);

		GL11.glPopMatrix();
	}
}
