package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import javax.annotation.Nullable;
import static java.lang.Math.PI;

public class ModelKronosaurus extends AnimatedGeoModel<EntityKronosaurus>
{
    @Override
    public ResourceLocation getModelResource(EntityKronosaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/kronosaurus/kronosaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityKronosaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/kronosaurus/kronosaurus.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EntityKronosaurus animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/kronosaurus/kronosaurus.json");
    }

    @Override
    public void setLivingAnimations(EntityKronosaurus entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            float setPitchValue = entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick();
            setPitchValue = Mth.clamp(setPitchValue, -0.785F,0.785F);
            (this.getAnimationProcessor().getBone("torso")).setRotationX(setPitchValue);
        }
        float setYawValue = entity.prevSetYaw+(entity.setYaw-entity.prevSetYaw)*customPredicate.getPartialTick();
        (this.getAnimationProcessor().getBone("lower_torso")).setRotationY(setYawValue*5.0F);
        (this.getAnimationProcessor().getBone("lower_torso_tail")).setRotationY(setYawValue*5.0F);
        (this.getAnimationProcessor().getBone("tail_section_1")).setRotationY(setYawValue*5.0F);
        (this.getAnimationProcessor().getBone("tail_section_2")).setRotationY(setYawValue*5.0F);
        (this.getAnimationProcessor().getBone("tail_section_3")).setRotationY(setYawValue*5.0F);
        (this.getAnimationProcessor().getBone("tail_section_4")).setRotationY(setYawValue*5.0F);
        (this.getAnimationProcessor().getBone("neck")).setRotationY(-setYawValue*5.0F);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}