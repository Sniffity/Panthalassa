package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityDunkleosteus;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static java.lang.Math.PI;

public class ModelDunkleosteus extends AnimatedGeoModel<EntityDunkleosteus>
{
    @Override
    public ResourceLocation getModelLocation(EntityDunkleosteus object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/dunkleosteus/dunkleosteus.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDunkleosteus object) {
        if (object.getTextureVariant() == 0){
            return new ResourceLocation(Panthalassa.MODID,"textures/creature/dunkleosteus/dunkleosteus_blue.png");
        } else if (object.getTextureVariant() == 1) {
            return new ResourceLocation(Panthalassa.MODID,"textures/creature/dunkleosteus/dunkleosteus_brown.png");
        } else {
            return new ResourceLocation(Panthalassa.MODID,"textures/creature/dunkleosteus/dunkleosteus_green.png");
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDunkleosteus animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/dunkleosteus/dunkleosteus.json");
    }

    @Override
    public void setLivingAnimations(EntityDunkleosteus entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            (this.getAnimationProcessor().getBone("neck")).setRotationX(entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick());
        }
        float setYawValue = entity.prevSetYaw+(entity.setYaw-entity.prevSetYaw)*customPredicate.getPartialTick();
        (this.getAnimationProcessor().getBone("body")).setRotationY((setYawValue)*3.0F);
        (this.getAnimationProcessor().getBone("tail1")).setRotationY((setYawValue)*3.0F);
        (this.getAnimationProcessor().getBone("tail2")).setRotationY((setYawValue)*3.0F);
        (this.getAnimationProcessor().getBone("tail3")).setRotationY((setYawValue)*3.0F);
        (this.getAnimationProcessor().getBone("head")).setRotationY(-(setYawValue)*3.0F);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

