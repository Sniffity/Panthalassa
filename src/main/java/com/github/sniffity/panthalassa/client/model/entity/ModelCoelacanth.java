package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityCoelacanth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import javax.annotation.Nullable;

import static java.lang.Math.PI;


public class ModelCoelacanth extends AnimatedGeoModel<EntityCoelacanth> {

    @Override
    public ResourceLocation getModelResource(EntityCoelacanth object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/coelacanth/coelacanth.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityCoelacanth object) {
        if (object.getTextureVariant() == 0){
            return new ResourceLocation(Panthalassa.MODID,"textures/creature/coelacanth/coelacanth_1.png");
        } else if (object.getTextureVariant() == 1) {
            return new ResourceLocation(Panthalassa.MODID,"textures/creature/coelacanth/coelacanth_2.png");
        } else {
            return new ResourceLocation(Panthalassa.MODID,"textures/creature/coelacanth/coelacanth_3.png");
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityCoelacanth animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/coelacanth/coelacanth.json");
    }

    @Override
    public void setLivingAnimations(EntityCoelacanth entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            float setPitchValue = entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick();
            setPitchValue = Mth.clamp(setPitchValue, -0.785F,0.785F);
            (this.getAnimationProcessor().getBone("main_body")).setRotationX(setPitchValue);
        }
        float setYawValue = entity.prevSetYaw+(entity.setYaw-entity.prevSetYaw)*customPredicate.getPartialTick();

        (this.getAnimationProcessor().getBone("lower_body_1")).setRotationY(setYawValue * 5.0F);
        (this.getAnimationProcessor().getBone("lower_body_2")).setRotationY(setYawValue * 5.0F);
        (this.getAnimationProcessor().getBone("head_1")).setRotationY(-setYawValue * 5.0F);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

