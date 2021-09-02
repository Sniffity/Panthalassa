package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityDunkleosteus;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/dunkleosteus/dunkleosteus.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityDunkleosteus animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/dunkleosteus/dunkleosteus.json");
    }

    @Override
    public void setLivingAnimations(EntityDunkleosteus entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() || !entity.isOnGround()) {
            (this.getAnimationProcessor().getBone("main")).setRotationX( (float) (MathHelper.atan2((entity.getDeltaMovement().y),MathHelper.sqrt((entity.getDeltaMovement().x)*(entity.getDeltaMovement().x)+(entity.getDeltaMovement().z)*(entity.getDeltaMovement().z)))));
        }
        (this.getAnimationProcessor().getBone("lower_body_1")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5.0F);
        (this.getAnimationProcessor().getBone("lower_body_3")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5.0F);
        (this.getAnimationProcessor().getBone("upper_body_1")).setRotationY((float)(-entity.adjustYaw*(PI/180.0F))*5.0F);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

