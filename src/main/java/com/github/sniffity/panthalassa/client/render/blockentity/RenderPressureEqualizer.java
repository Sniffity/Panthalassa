package com.github.sniffity.panthalassa.client.render.blockentity;

import com.github.sniffity.panthalassa.client.model.blockentity.ModelHydrothermalVent;
import com.github.sniffity.panthalassa.client.model.blockentity.ModelPressureEqualizer;
import com.github.sniffity.panthalassa.server.block.BlockHydrothermalVentBlockEntity;
import com.github.sniffity.panthalassa.server.block.BlockPressureEqualizerBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;


public class RenderPressureEqualizer extends GeoBlockRenderer<BlockPressureEqualizerBlockEntity> {
    public RenderPressureEqualizer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new ModelPressureEqualizer());
    }

    @Override
    public RenderType getRenderType(BlockPressureEqualizerBlockEntity animatable, float partialTicks, PoseStack stack,
                                    MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}