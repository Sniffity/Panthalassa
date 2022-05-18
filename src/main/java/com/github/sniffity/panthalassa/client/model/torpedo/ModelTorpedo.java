package com.github.sniffity.panthalassa.client.model.torpedo;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.projectile.ProjectileTorpedo;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class ModelTorpedo extends AnimatedGeoModel<ProjectileTorpedo>
{
    @Override
    public ResourceLocation getModelLocation(ProjectileTorpedo object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/projectile/torpedo/torpedo.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ProjectileTorpedo object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/projectile/torpedo/torpedo.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ProjectileTorpedo animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/projectile/torpedo/torpedo.json");
    }
}
