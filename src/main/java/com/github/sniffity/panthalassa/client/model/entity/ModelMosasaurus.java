package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMosasaurus;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import javax.annotation.Nullable;
import static java.lang.Math.PI;


public class ModelMosasaurus extends AnimatedGeoModel<EntityMosasaurus>
{
    @Override
    public ResourceLocation getModelLocation(EntityMosasaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/mosasaurus/mosasaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMosasaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/mosasaurus/mosasaurus.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityMosasaurus animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/mosasaurus/mosasaurus.json");
    }

    @Override
    public void setLivingAnimations(EntityMosasaurus entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if ((entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) || entity.getBreaching()) {
            (this.getAnimationProcessor().getBone("torso")).setRotationX(entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick());
        }
        float setYawValue = entity.prevSetYaw+(entity.setYaw-entity.prevSetYaw)*customPredicate.getPartialTick();
        (this.getAnimationProcessor().getBone("body1")).setRotationY(setYawValue*4.0F);
        (this.getAnimationProcessor().getBone("body2")).setRotationY(setYawValue*4.0F);
        (this.getAnimationProcessor().getBone("tail")).setRotationY(setYawValue*4.0F);
        (this.getAnimationProcessor().getBone("tail2")).setRotationY(setYawValue*4.0F);
        (this.getAnimationProcessor().getBone("tail3")).setRotationY(setYawValue*4.0F);
        (this.getAnimationProcessor().getBone("head")).setRotationY(-setYawValue*4.0F);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

