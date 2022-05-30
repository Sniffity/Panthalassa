package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityAnomalocaris;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class ModelAnomalocaris extends AnimatedGeoModel<EntityAnomalocaris> {

    @Override
    public ResourceLocation getModelLocation(EntityAnomalocaris object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/anomalocaris/anomalocaris.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAnomalocaris object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/anomalocaris/anomalocaris.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityAnomalocaris animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/anomalocaris/anomalocaris.json");
    }

    @Override
    public void setLivingAnimations(EntityAnomalocaris entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            float setPitchValue = entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick();
            setPitchValue = Mth.clamp(setPitchValue, -0.785F,0.785F);
            (this.getAnimationProcessor().getBone("head")).setRotationX(setPitchValue);
        }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
