package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static java.lang.Math.PI;

public class ModelMegalodon extends AnimatedGeoModel<EntityMegalodon> {
    @Override
    public ResourceLocation getModelLocation(EntityMegalodon object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/megalodon/megalodon.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMegalodon object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/megalodon/megalodon.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityMegalodon animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/megalodon/megalodon.json");
    }

    @Override
    public void setLivingAnimations(EntityMegalodon entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() || !entity.isOnGround()) {
            (this.getAnimationProcessor().getBone("main")).setRotationX((float) (MathHelper.atan2((entity.getDeltaMovement().y), MathHelper.sqrt((entity.getDeltaMovement().x) * (entity.getDeltaMovement().x) + (entity.getDeltaMovement().z) * (entity.getDeltaMovement().z)))));
        }
        if (entity.getTarget() != null) {
            (this.getAnimationProcessor().getBone("lower_body_1")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*30.0F);
            (this.getAnimationProcessor().getBone("lower_body_3")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*30.0F);
            (this.getAnimationProcessor().getBone("lower_body_6")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*30.0F);
            (this.getAnimationProcessor().getBone("head")).setRotationY((float)(-entity.adjustYaw*(PI/180.0F))*30.0F);
        } else {
            (this.getAnimationProcessor().getBone("lower_body_1")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*30.0F);
            (this.getAnimationProcessor().getBone("lower_body_3")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*30.0F);
            (this.getAnimationProcessor().getBone("lower_body_6")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*30.0F);
            (this.getAnimationProcessor().getBone("head")).setRotationY((float)(-entity.adjustYaw*(PI/180.0F))*30.0F);
        }

    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

