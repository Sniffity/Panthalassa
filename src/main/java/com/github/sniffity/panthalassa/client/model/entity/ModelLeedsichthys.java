package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityLeedsichthys;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static java.lang.Math.PI;


public class ModelLeedsichthys extends AnimatedGeoModel<EntityLeedsichthys>
{
    @Override
    public ResourceLocation getModelLocation(EntityLeedsichthys object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/leedsichthys/leedsichthys.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityLeedsichthys object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/leedsichthys/leedsichthys.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityLeedsichthys animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/leedsichthys/leedsichthys.json");
    }

    @Override
    public void setLivingAnimations(EntityLeedsichthys entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            (this.getAnimationProcessor().getBone("body")).setRotationX(entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick());
        }
        (this.getAnimationProcessor().getBone("bodymid1")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5.0F);
        (this.getAnimationProcessor().getBone("bodymid2")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5.0F);
        (this.getAnimationProcessor().getBone("tail1")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5.0F);
        (this.getAnimationProcessor().getBone("tail2")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5.0F);
        (this.getAnimationProcessor().getBone("head")).setRotationY((float)(-entity.adjustYaw*(PI/180.0F))*5.0F);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}