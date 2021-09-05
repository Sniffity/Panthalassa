package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityBasilosaurus;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static java.lang.Math.PI;


public class ModelBasilosaurus extends AnimatedGeoModel<EntityBasilosaurus>
{
    @Override
    public ResourceLocation getModelLocation(EntityBasilosaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/basilosaurus/basilosaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBasilosaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/basilosaurus/basilosaurus.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityBasilosaurus animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/basilosaurus/basilosaurus.json");
    }

    @Override
    public void setLivingAnimations(EntityBasilosaurus entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() || !entity.isOnGround()) {
            (this.getAnimationProcessor().getBone("body")).setRotationX( (float) (MathHelper.atan2((entity.getDeltaMovement().y),MathHelper.sqrt((entity.getDeltaMovement().x)*(entity.getDeltaMovement().x)+(entity.getDeltaMovement().z)*(entity.getDeltaMovement().z))*5)));
        }
        (this.getAnimationProcessor().getBone("tail")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*4.0F);
        (this.getAnimationProcessor().getBone("tail2")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*4.0F);
        (this.getAnimationProcessor().getBone("tail3")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*4.0F);
        (this.getAnimationProcessor().getBone("tail4")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*4.0F);
        (this.getAnimationProcessor().getBone("tail5")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*4.0F);
        (this.getAnimationProcessor().getBone("neck")).setRotationY((float)(-entity.adjustYaw*(PI/180.0F))*4.0F);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

