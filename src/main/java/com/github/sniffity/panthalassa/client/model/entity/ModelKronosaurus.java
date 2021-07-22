package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.common.entity.EntityKronosaurus;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelKronosaurus extends AnimatedGeoModel<EntityKronosaurus>
{
    @Override
    public ResourceLocation getModelLocation(EntityKronosaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/kronosaurus/kronosaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityKronosaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/entity/kronosaurus/kronosaurus.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityKronosaurus animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/kronosaurus/kronosaurus.json");
    }

    @Override
    public void setLivingAnimations(EntityKronosaurus entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

