package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityArchelon;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelArchelon extends AnimatedGeoModel<EntityArchelon>
{
    @Override
    public ResourceLocation getModelLocation(EntityArchelon object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/archelon/archelon.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityArchelon object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/archelon/archelon.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityArchelon animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/archelon/archelon.json");
    }

    @Override
    public void setLivingAnimations(EntityArchelon entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

