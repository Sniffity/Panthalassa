package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.client.model.entity.ModelCoelacanth;
import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.server.entity.creature.EntityCoelacanth;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderCoelacanth extends GeoEntityRenderer<EntityCoelacanth> {

    public RenderCoelacanth(EntityRendererManager renderManager) {
        super(renderManager, new ModelCoelacanth());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void renderEarly(EntityCoelacanth animatable, MatrixStack stackIn, float ticks,
                            IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
        stackIn.scale(1.0F*PanthalassaClientConfig.coealacanthSizeMultiplier.get().floatValue(), 1.0F*PanthalassaClientConfig.coealacanthSizeMultiplier.get().floatValue(), 1.0F*PanthalassaClientConfig.coealacanthSizeMultiplier.get().floatValue());
    }
}