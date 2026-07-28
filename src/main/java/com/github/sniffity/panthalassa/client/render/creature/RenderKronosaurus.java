package com.github.sniffity.panthalassa.client.render.creature;

import com.github.sniffity.panthalassa.client.debug.PanthalassaDebugRenderer;
import com.github.sniffity.panthalassa.client.model.creature.ModelKronosaurus;
import com.github.sniffity.panthalassa.entity.creature.CreatureKronosaurus;
import com.github.sniffity.panthalassa.entity.creature.PanthalassaCreature;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
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
        poseStack.scale(2.0F, 2.0F, 2.0F);
    }

    @Override
    public void render(
            CreatureKronosaurus entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        PanthalassaDebugRenderer.render(
                entity,
                poseStack,
                buffer
        );
    }
}
