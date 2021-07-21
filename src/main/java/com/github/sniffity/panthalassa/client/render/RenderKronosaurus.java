package com.github.sniffity.panthalassa.client.render;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.client.model.entity.ModelKronosaurus;
import com.github.sniffity.panthalassa.common.entity.EntityKronosaurus;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;


public class RenderKronosaurus extends MobRenderer<EntityKronosaurus, ModelKronosaurus<EntityKronosaurus>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Panthalassa.MODID, "textures/entity/kronosaurus/kronosaurus.png");

    public RenderKronosaurus(EntityRendererManager mgr) {
        super(mgr, new ModelKronosaurus<>(), 1.0F);
    }


    @Override
    public ResourceLocation getEntityTexture(EntityKronosaurus entity) {
        return TEXTURE;
    }
}