package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.client.model.entity.ModelMosasaurus;
import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMosasaurus;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderMosasaurus extends GeoEntityRenderer<EntityMosasaurus> {

    public RenderMosasaurus(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelMosasaurus());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void renderEarly(EntityMosasaurus animatable, PoseStack stackIn, float ticks,
                            MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
        stackIn.scale(1.0F* PanthalassaClientConfig.mosasaurusSizeMultiplier.get().floatValue(), 1.0F* PanthalassaClientConfig.mosasaurusSizeMultiplier.get().floatValue(), 1.0F* PanthalassaClientConfig.mosasaurusSizeMultiplier.get().floatValue());
    }
}