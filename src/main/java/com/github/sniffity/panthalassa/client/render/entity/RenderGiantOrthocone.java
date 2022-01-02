package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.client.model.entity.ModelGiantOrthocone;
import com.github.sniffity.panthalassa.client.model.entity.ModelKronosaurus;
import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.server.entity.creature.EntityGiantOrthocone;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderGiantOrthocone extends GeoEntityRenderer<EntityGiantOrthocone> {

    public RenderGiantOrthocone(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelGiantOrthocone());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void renderEarly(EntityGiantOrthocone animatable, PoseStack stackIn, float ticks,
                            MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
        stackIn.scale(1.0F*PanthalassaClientConfig.giantOrthoconeSizeMultiplier.get().floatValue(), 1.0F*PanthalassaClientConfig.giantOrthoconeSizeMultiplier.get().floatValue(), 1.0F*PanthalassaClientConfig.giantOrthoconeSizeMultiplier.get().floatValue());

    }

}