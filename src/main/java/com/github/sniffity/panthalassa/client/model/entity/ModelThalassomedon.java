package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityThalassomedon;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;


public class ModelThalassomedon extends AnimatedGeoModel<EntityThalassomedon>
{
    @Override
    public ResourceLocation getModelLocation(EntityThalassomedon object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/thalassomedon/thalassomedon.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityThalassomedon object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/thalassomedon/thalassomedon.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityThalassomedon animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/thalassomedon/thalassomedon.json");
    }

    @Override
    public void setLivingAnimations(EntityThalassomedon entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

