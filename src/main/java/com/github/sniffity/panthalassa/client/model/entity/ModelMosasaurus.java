package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMosasaurus;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

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
    public void setLivingAnimations(EntityMosasaurus entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
        if (entity.isInWater() || !entity.isOnGround()) {
            (this.getAnimationProcessor().getBone("torso")).setRotationX( (float) (MathHelper.atan2((entity.getDeltaMovement().y),MathHelper.sqrt((entity.getDeltaMovement().x)*(entity.getDeltaMovement().x)+(entity.getDeltaMovement().z)*(entity.getDeltaMovement().z)))));
        }
        (this.getAnimationProcessor().getBone("lower_torso")).setRotationY((float)(entity.adjustRotation*(PI/180.0F))*5);
        (this.getAnimationProcessor().getBone("lower_torso_tail")).setRotationY((float)(entity.adjustRotation*(PI/180.0F))*5);
        (this.getAnimationProcessor().getBone("tail_section_1")).setRotationY((float)(entity.adjustRotation*(PI/180.0F))*5);
        (this.getAnimationProcessor().getBone("tail_section_2")).setRotationY((float)(entity.adjustRotation*(PI/180.0F))*5);
        (this.getAnimationProcessor().getBone("neck")).setRotationY((float)(-entity.adjustRotation*(PI/180.0F))*5);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

