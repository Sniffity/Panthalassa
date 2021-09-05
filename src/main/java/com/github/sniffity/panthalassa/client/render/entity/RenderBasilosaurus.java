package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.client.model.entity.ModelBasilosaurus;
import com.github.sniffity.panthalassa.server.entity.creature.EntityBasilosaurus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderBasilosaurus extends GeoEntityRenderer<EntityBasilosaurus> {

    public RenderBasilosaurus(EntityRendererManager renderManager) {
        super(renderManager, new ModelBasilosaurus());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void renderEarly(EntityBasilosaurus animatable, MatrixStack stackIn, float ticks,
                            IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
        stackIn.scale(1.0F, 1.0F, 1.0F);
    }
}