package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityAcrolepis;
import com.github.sniffity.panthalassa.server.entity.creature.EntityCeratodus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class ModelCeratodus extends AnimatedGeoModel<EntityCeratodus> {

    @Override
    public ResourceLocation getModelLocation(EntityCeratodus object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/ceratodus/ceratodus.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCeratodus object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/ceratodus/ceratodus.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityCeratodus animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/ceratodus/ceratodus.json");
    }

    @Override
    public void setLivingAnimations(EntityCeratodus entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            float setPitchValue = entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick();
            setPitchValue = Mth.clamp(setPitchValue, -0.785F,0.785F);
            (this.getAnimationProcessor().getBone("body1")).setRotationX(setPitchValue);
        }
        float setYawValue = entity.prevSetYaw+(entity.setYaw-entity.prevSetYaw)*customPredicate.getPartialTick();
        (this.getAnimationProcessor().getBone("body2")).setRotationY(setYawValue * 5.0F);
        (this.getAnimationProcessor().getBone("tail")).setRotationY(setYawValue * 5.0F);
        (this.getAnimationProcessor().getBone("head")).setRotationY(-setYawValue * 5.0F);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}
