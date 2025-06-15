package com.github.sniffity.client.render.creature;

import com.github.sniffity.client.model.creature.ModelKronosaurus;
import com.github.sniffity.entity.creature.CreatureKronosaurus;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class RenderKronosaurus extends GeoEntityRenderer<CreatureKronosaurus> {

    public RenderKronosaurus(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelKronosaurus());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void preRender(PoseStack poseStack, CreatureKronosaurus animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.scale(1.5F, 1.5F, 1.5F);
    }
}
