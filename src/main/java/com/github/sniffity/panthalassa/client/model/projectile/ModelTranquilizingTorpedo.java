package com.github.sniffity.panthalassa.client.model.projectile;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.projectile.ProjectileTranquilizingTorpedo;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class ModelTranquilizingTorpedo extends AnimatedGeoModel<ProjectileTranquilizingTorpedo>
{
    @Override
    public ResourceLocation getModelResource(ProjectileTranquilizingTorpedo object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/projectile/torpedo/torpedo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ProjectileTranquilizingTorpedo object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/projectile/torpedo/tranquilizing_torpedo.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ProjectileTranquilizingTorpedo animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/projectile/torpedo/torpedo.json");
    }
}