package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityThalassomedon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static java.lang.Math.PI;

public class ModelThalassomedon extends AnimatedGeoModel<EntityThalassomedon> {
    @Override
    public ResourceLocation getModelLocation(EntityThalassomedon object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/thalassomedon/thalassomedon.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityThalassomedon object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/thalassomedon/thalassomedon.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityThalassomedon animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/thalassomedon/thalassomedon.json");
    }

    @Override
    public void setLivingAnimations(EntityThalassomedon entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() || !entity.isOnGround()) {
            (this.getAnimationProcessor().getBone("main")).setRotationX( (float) (MathHelper.atan2((entity.getDeltaMovement().y),MathHelper.sqrt((entity.getDeltaMovement().x)*(entity.getDeltaMovement().x)+(entity.getDeltaMovement().z)*(entity.getDeltaMovement().z))*5)));
        }
        (this.getAnimationProcessor().getBone("lower_body_1")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5);
        (this.getAnimationProcessor().getBone("lower_body_3")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5);
        (this.getAnimationProcessor().getBone("lower_body_6")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5);
        (this.getAnimationProcessor().getBone("head")).setRotationY((float)(-entity.adjustYaw*(PI/180.0F))*5);

        /*
        (this.getAnimationProcessor().getBone("lower_body_1")).setRotationZ((float)(entity.adjustRotation*(PI/180.0F))*5);
        (this.getAnimationProcessor().getBone("lower_body_3")).setRotationZ((float)(entity.adjustRotation*(PI/180.0F))*5);
        (this.getAnimationProcessor().getBone("lower_body_6")).setRotationZ((float)(entity.adjustRotation*(PI/180.0F))*5);
        (this.getAnimationProcessor().getBone("head")).setRotationZ((float)(-entity.adjustRotation*(PI/180.0F))*5);
        */
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

