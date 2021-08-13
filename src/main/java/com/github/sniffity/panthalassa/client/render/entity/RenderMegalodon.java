package com.github.sniffity.panthalassa.client.render.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.client.model.entity.ModelKronosaurus;
import com.github.sniffity.panthalassa.client.model.entity.ModelMegalodon;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;


public class RenderMegalodon extends GeoEntityRenderer<EntityMegalodon> {

    public RenderMegalodon(EntityRendererManager renderManager) {
        super(renderManager, new ModelMegalodon());
        this.shadowRadius = 1.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMegalodon entity) {
        return new ResourceLocation(Panthalassa.MODID,"textures/entity/megalodon/megalodon.png");
    }

}
