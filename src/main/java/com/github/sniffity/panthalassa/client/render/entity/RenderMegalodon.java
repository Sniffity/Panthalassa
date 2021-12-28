package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.client.model.entity.ModelMegalodon;
import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;


public class RenderMegalodon extends GeoEntityRenderer<EntityMegalodon> {

    public RenderMegalodon(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelMegalodon());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void render(EntityMegalodon entity, float entityYaw, float partialTicks, PoseStack stack,
                       MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public void renderEarly(EntityMegalodon animatable, PoseStack stackIn, float ticks,
                            MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
        stackIn.scale(1.0F* PanthalassaClientConfig.megalodonSizeMultiplier.get().floatValue(), 1.0F* PanthalassaClientConfig.megalodonSizeMultiplier.get().floatValue(), 1.0F* PanthalassaClientConfig.megalodonSizeMultiplier.get().floatValue());
    }

    @Override
    protected int getBlockLightLevel(EntityMegalodon entityIn, BlockPos partialTicks) {
        return 15;
    }
}