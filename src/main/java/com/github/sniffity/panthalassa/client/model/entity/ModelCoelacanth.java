package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityArchelon;
import com.github.sniffity.panthalassa.server.entity.creature.EntityCoelacanth;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import java.util.Random;

import static java.lang.Math.PI;

public class ModelCoelacanth extends AnimatedGeoModel<EntityCoelacanth> {

    @Override
    public ResourceLocation getModelLocation(EntityCoelacanth object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/coelacanth/coelacanth.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCoelacanth object) {
        if (object.getIsLeader()) {
            return new ResourceLocation(Panthalassa.MODID, "textures/creature/coelacanth/coelacanth_0.png");
        } else {
            return new ResourceLocation(Panthalassa.MODID, "textures/creature/coelacanth/coelacanth_1.png");
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityCoelacanth animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/coelacanth/coelacanth.json");
    }

    @Override
    public void setLivingAnimations(EntityCoelacanth entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() || !entity.isOnGround()) {
            (this.getAnimationProcessor().getBone("main_body")).setRotationX(((float) MathHelper.atan2((entity.getDeltaMovement().y), MathHelper.sqrt((entity.getDeltaMovement().x) * (entity.getDeltaMovement().x) + (entity.getDeltaMovement().z) * (entity.getDeltaMovement().z)))));
        }
        (this.getAnimationProcessor().getBone("lower_body_1")).setRotationY((float) (entity.adjustYaw * (PI / 180.0F)) * 5.0F);
        (this.getAnimationProcessor().getBone("lower_body_2")).setRotationY((float) (entity.adjustYaw * (PI / 180.0F)) * 5.0F);
        (this.getAnimationProcessor().getBone("head_1")).setRotationY((float) (-entity.adjustYaw * (PI / 180.0F)) * 5.0F);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

