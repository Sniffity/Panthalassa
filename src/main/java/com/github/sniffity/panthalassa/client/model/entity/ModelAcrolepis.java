package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityAcrolepis;
import com.github.sniffity.panthalassa.server.entity.creature.EntityCoelacanth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

public class ModelAcrolepis extends AnimatedGeoModel<EntityAcrolepis> {

    @Override
    public ResourceLocation getModelLocation(EntityAcrolepis object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/acrolepis/acrolepis.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAcrolepis object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/acrolepis/acrolepis.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityAcrolepis animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/acrolepis/acrolepis.json");
    }

    @Override
    public void setLivingAnimations(EntityAcrolepis entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            float setPitchValue = entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick();
            setPitchValue = Mth.clamp(setPitchValue, -0.785F,0.785F);
            (this.getAnimationProcessor().getBone("body")).setRotationX(setPitchValue);
        }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

