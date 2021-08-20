package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.client.model.entity.ModelKronosaurus;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderKronosaurus extends GeoEntityRenderer<EntityKronosaurus> {

    public RenderKronosaurus(EntityRendererManager renderManager) {
        super(renderManager, new ModelKronosaurus());
        this.shadowRadius = 1.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityKronosaurus entity) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/kronosaurus/kronosaurus.png");
    }
}