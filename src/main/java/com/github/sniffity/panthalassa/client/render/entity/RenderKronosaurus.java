package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.client.model.entity.ModelKronosaurus;
import com.github.sniffity.panthalassa.common.entity.EntityKronosaurus;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;


public class RenderKronosaurus extends GeoEntityRenderer<EntityKronosaurus> {

    public RenderKronosaurus(EntityRendererManager renderManager) {
        super(renderManager, new ModelKronosaurus());
        this.shadowSize = 1.0F;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityKronosaurus entity) {
        return new ResourceLocation(Panthalassa.MODID,"textures/entity/kronosaurus/kronosaurus.png");
    }

}
