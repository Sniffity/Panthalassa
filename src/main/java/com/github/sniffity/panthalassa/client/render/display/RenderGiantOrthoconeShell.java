package com.github.sniffity.panthalassa.client.render.display;

import com.github.sniffity.panthalassa.client.model.entity.ModelArchelon;
import com.github.sniffity.panthalassa.client.model.entity.ModelGiantOrthoconeShell;
import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.server.entity.creature.EntityArchelon;
import com.github.sniffity.panthalassa.server.entity.display.EntityGiantOrthoconeShell;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderGiantOrthoconeShell extends GeoEntityRenderer<EntityGiantOrthoconeShell> {

    public RenderGiantOrthoconeShell(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelGiantOrthoconeShell());
        this.shadowRadius = 1.0F;
    }

    @Override
    public void renderEarly(EntityGiantOrthoconeShell animatable, PoseStack stackIn, float ticks,
                            MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
                            float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                red, green, blue, partialTicks);
    }
}