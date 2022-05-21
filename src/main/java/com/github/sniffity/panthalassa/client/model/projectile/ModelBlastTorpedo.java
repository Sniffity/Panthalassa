package com.github.sniffity.panthalassa.client.model.projectile;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.projectile.ProjectileBlastTorpedo;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class ModelBlastTorpedo extends AnimatedGeoModel<ProjectileBlastTorpedo>
{
    @Override
    public ResourceLocation getModelLocation(ProjectileBlastTorpedo object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/projectile/torpedo/torpedo.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ProjectileBlastTorpedo object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/projectile/torpedo/blast_torpedo.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ProjectileBlastTorpedo animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/projectile/torpedo/torpedo.json");
    }
}