package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.client.model.entity.ModelArchelon;
import com.github.sniffity.panthalassa.server.entity.creature.EntityArchelon;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderArchelon extends GeoEntityRenderer<EntityArchelon> {

    public RenderArchelon(EntityRendererManager renderManager) {
        super(renderManager, new ModelArchelon());
        this.shadowRadius = 1.0F;
    }
    @Override
    public ResourceLocation getTextureLocation(EntityArchelon entity) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/archelon/archelon.png");
    }

}