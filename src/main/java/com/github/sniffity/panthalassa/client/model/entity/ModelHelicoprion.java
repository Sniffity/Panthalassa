package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityHelicoprion;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;


public class ModelHelicoprion extends AnimatedGeoModel<EntityHelicoprion> {
    @Override
    public ResourceLocation getModelLocation(EntityHelicoprion object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/helicoprion/helicoprion.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHelicoprion object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/helicoprion/helicoprion.png");

    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityHelicoprion animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/helicoprion/helicoprion.json");
    }

    @Override
    public void setLivingAnimations(EntityHelicoprion entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        float setPitchValue = entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick();
        if (entity.getBreaching()) {
            (this.getAnimationProcessor().getBone("neck")).setRotationX(setPitchValue);
        }
        else if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            setPitchValue = Mth.clamp(setPitchValue, -0.785F,0.785F);
            (this.getAnimationProcessor().getBone("neck")).setRotationX(setPitchValue);
        }
        float setYawValue = entity.prevSetYaw+(entity.setYaw-entity.prevSetYaw)*customPredicate.getPartialTick();
        (this.getAnimationProcessor().getBone("tail1")).setRotationY((setYawValue) * 4.0F);
        (this.getAnimationProcessor().getBone("tail2")).setRotationY((setYawValue) * 4.0F);
        (this.getAnimationProcessor().getBone("tail3")).setRotationY((setYawValue) * 4.0F);
        (this.getAnimationProcessor().getBone("head")).setRotationY((-setYawValue) * 2.0F);
    }


    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}