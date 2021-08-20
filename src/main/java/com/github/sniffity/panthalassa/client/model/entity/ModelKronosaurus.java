package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelKronosaurus extends AnimatedGeoModel<EntityKronosaurus>
{
    @Override
    public ResourceLocation getModelLocation(EntityKronosaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/kronosaurus/kronosaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityKronosaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/kronosaurus/kronosaurus.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityKronosaurus animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/kronosaurus/kronosaurus.json");
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

