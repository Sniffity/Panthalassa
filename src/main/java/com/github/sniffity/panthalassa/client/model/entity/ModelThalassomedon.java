package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityThalassomedon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;


public class ModelThalassomedon extends AnimatedGeoModel<EntityThalassomedon> {
    @Override
    public ResourceLocation getModelResource(EntityThalassomedon object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/thalassomedon/thalassomedon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityThalassomedon object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/thalassomedon/thalassomedon.png");

    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityThalassomedon animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/thalassomedon/thalassomedon.json");
    }

    @Override
    public void setLivingAnimations(EntityThalassomedon entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            float setPitchValue = entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick();
            setPitchValue = Mth.clamp(setPitchValue, -0.785F,0.785F);
            (this.getAnimationProcessor().getBone("body")).setRotationX(setPitchValue);
        }
        float setYawValue = entity.prevSetYaw+(entity.setYaw-entity.prevSetYaw)*customPredicate.getPartialTick();
        (this.getAnimationProcessor().getBone("tail")).setRotationY((setYawValue) * 4.0F);
        (this.getAnimationProcessor().getBone("tail2")).setRotationY((setYawValue) * 4.0F);
        (this.getAnimationProcessor().getBone("tail3")).setRotationY((setYawValue) * 4.0F);
        (this.getAnimationProcessor().getBone("tail4")).setRotationY((setYawValue) * 4.0F);
        (this.getAnimationProcessor().getBone("neck3")).setRotationY((-setYawValue) * 2.0F);
        (this.getAnimationProcessor().getBone("neck4")).setRotationY((-setYawValue) * 2.0F);
        (this.getAnimationProcessor().getBone("neck5")).setRotationY((-setYawValue) * 2.0F);
        (this.getAnimationProcessor().getBone("neck6")).setRotationY((-setYawValue) * 2.0F);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}