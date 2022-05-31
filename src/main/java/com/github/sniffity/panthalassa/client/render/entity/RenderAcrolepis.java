package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.client.model.entity.ModelAcrolepis;
import com.github.sniffity.panthalassa.client.model.entity.ModelArchelon;
import com.github.sniffity.panthalassa.client.model.entity.ModelThalassomedon;
import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.server.entity.creature.EntityAcrolepis;
import com.github.sniffity.panthalassa.server.entity.creature.EntityArchelon;
import com.github.sniffity.panthalassa.server.entity.creature.EntityThalassomedon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderAcrolepis extends GeoEntityRenderer<EntityAcrolepis> {

    public RenderAcrolepis(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelAcrolepis());
        this.shadowRadius = 0.5F;
    }

    @Override
    public void renderEarly(EntityAcrolepis animatable, PoseStack stackIn, float ticks,
                            MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
        stackIn.scale(2.0F*PanthalassaClientConfig.acrolepisSizeMultiplier.get().floatValue(), 2.0F*PanthalassaClientConfig.acrolepisSizeMultiplier.get().floatValue(), 2.0F*PanthalassaClientConfig.acrolepisSizeMultiplier.get().floatValue());
    }
}