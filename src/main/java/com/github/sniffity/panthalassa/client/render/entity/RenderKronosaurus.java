package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.client.model.entity.ModelKronosaurus;
import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleAGII;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderKronosaurus extends GeoEntityRenderer<EntityKronosaurus> {

    public RenderKronosaurus(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelKronosaurus());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void renderEarly(EntityKronosaurus animatable, PoseStack stackIn, float ticks,
                            MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
        if (animatable.getIsLeader()) {
            stackIn.scale(1.2F* PanthalassaClientConfig.kronosaurusSizeMultiplier.get().floatValue(), 1.2F*PanthalassaClientConfig.kronosaurusSizeMultiplier.get().floatValue(), 1.2F*PanthalassaClientConfig.kronosaurusSizeMultiplier.get().floatValue());
        } else {
            stackIn.scale(1.0F*PanthalassaClientConfig.kronosaurusSizeMultiplier.get().floatValue(), 1.0F*PanthalassaClientConfig.kronosaurusSizeMultiplier.get().floatValue(), 1.0F*PanthalassaClientConfig.kronosaurusSizeMultiplier.get().floatValue());

        }
    }

    @Override
    protected int getBlockLightLevel(EntityKronosaurus entityIn, BlockPos partialTicks) {
        return entityIn.getIsLeader() ? 15 : 1;
    }
}