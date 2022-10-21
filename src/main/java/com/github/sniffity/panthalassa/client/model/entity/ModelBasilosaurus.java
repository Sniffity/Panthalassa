package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityBasilosaurus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class ModelBasilosaurus extends AnimatedGeoModel<EntityBasilosaurus>
{
    @Override
    public ResourceLocation getModelResource(EntityBasilosaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/basilosaurus/basilosaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityBasilosaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/basilosaurus/basilosaurus.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EntityBasilosaurus animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/basilosaurus/basilosaurus.json");
    }

    @Override
    public void setLivingAnimations(EntityBasilosaurus entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            float setPitchValue = entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick();
            setPitchValue = Mth.clamp(setPitchValue, -0.785F,0.785F);
            (this.getAnimationProcessor().getBone("body")).setRotationX(setPitchValue);
        }
        float setYawValue = entity.prevSetYaw+(entity.setYaw-entity.prevSetYaw)*customPredicate.getPartialTick();
        (this.getAnimationProcessor().getBone("body2")).setRotationY(setYawValue*3.5F);
        (this.getAnimationProcessor().getBone("tail")).setRotationY(setYawValue*3.5F);
        (this.getAnimationProcessor().getBone("tail2")).setRotationY(setYawValue*3.5F);
        (this.getAnimationProcessor().getBone("tail3")).setRotationY(setYawValue*3.5F);
        (this.getAnimationProcessor().getBone("tail4")).setRotationY(setYawValue*3.5F);
        (this.getAnimationProcessor().getBone("tail5")).setRotationY(setYawValue*3.5F);
        (this.getAnimationProcessor().getBone("tail6")).setRotationY(setYawValue*3.5F);
        (this.getAnimationProcessor().getBone("neck")).setRotationY(-setYawValue*3.5F);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}