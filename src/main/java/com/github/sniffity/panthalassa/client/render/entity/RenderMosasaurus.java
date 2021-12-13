package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.client.model.entity.ModelArchelon;
import com.github.sniffity.panthalassa.client.model.entity.ModelMosasaurus;
import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.server.entity.creature.EntityArchelon;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMosasaurus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderMosasaurus extends GeoEntityRenderer<EntityMosasaurus> {

    public RenderMosasaurus(EntityRendererManager renderManager) {
        super(renderManager, new ModelMosasaurus());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void renderEarly(EntityMosasaurus animatable, MatrixStack stackIn, float ticks,
                            IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
        stackIn.scale(1.0F* PanthalassaClientConfig.mosasaurusSizeMultiplier.get().floatValue(), 1.0F* PanthalassaClientConfig.mosasaurusSizeMultiplier.get().floatValue(), 1.0F* PanthalassaClientConfig.mosasaurusSizeMultiplier.get().floatValue());
    }
}