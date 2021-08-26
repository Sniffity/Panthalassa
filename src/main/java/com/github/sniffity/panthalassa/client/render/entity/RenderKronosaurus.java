package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.client.model.entity.ModelKronosaurus;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderKronosaurus extends GeoEntityRenderer<EntityKronosaurus> {

    public RenderKronosaurus(EntityRendererManager renderManager) {
        super(renderManager, new ModelKronosaurus());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void renderEarly(EntityKronosaurus animatable, MatrixStack stackIn, float ticks,
                            IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
        stackIn.scale(2.0F, 2.0F, 2.0F);
    }

    @Override
    protected int getBlockLightLevel(EntityKronosaurus entityIn, BlockPos partialTicks) {
        return entityIn.getIsLeader() ? 15 : 1;
    }
}