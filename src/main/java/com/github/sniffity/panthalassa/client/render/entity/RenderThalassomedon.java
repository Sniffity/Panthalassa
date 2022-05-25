package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.client.model.entity.ModelThalassomedon;
import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.server.entity.creature.EntityThalassomedon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderThalassomedon extends GeoEntityRenderer<EntityThalassomedon> {

    public RenderThalassomedon(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelThalassomedon());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void renderEarly(EntityThalassomedon animatable, PoseStack stackIn, float ticks,
                            MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);

        stackIn.scale(1.5F* PanthalassaClientConfig.kronosaurusSizeMultiplier.get().floatValue(), 1.5F*PanthalassaClientConfig.kronosaurusSizeMultiplier.get().floatValue(), 1.5F*PanthalassaClientConfig.kronosaurusSizeMultiplier.get().floatValue());

    }
}