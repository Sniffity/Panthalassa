package com.github.sniffity.panthalassa.client.render.display;

import com.github.sniffity.panthalassa.client.model.display.ModelKronosaurusSkull;
import com.github.sniffity.panthalassa.server.entity.display.DisplayKronosaurusSkull;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderKronosaurusSkull extends GeoEntityRenderer<DisplayKronosaurusSkull> {

    public RenderKronosaurusSkull(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelKronosaurusSkull());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void renderEarly(DisplayKronosaurusSkull animatable, PoseStack stackIn, float ticks,
                            MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
    }
}
