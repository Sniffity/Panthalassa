package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityAcrolepis;
import com.github.sniffity.panthalassa.server.entity.creature.EntityAnglerfish;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class ModelAnglerfish extends AnimatedGeoModel<EntityAnglerfish> {

    @Override
    public ResourceLocation getModelLocation(EntityAnglerfish object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/anglerfish/anglerfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAnglerfish object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/anglerfish/anglerfish.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityAnglerfish animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/anglerfish/anglerfish.json");
    }

    @Override
    public void setLivingAnimations(EntityAnglerfish entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            float setPitchValue = entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick();
            setPitchValue = Mth.clamp(setPitchValue, -0.785F,0.785F);
            (this.getAnimationProcessor().getBone("body1")).setRotationX(setPitchValue);
        }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

