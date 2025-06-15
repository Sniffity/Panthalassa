package com.github.sniffity.client.model.creature;

import com.github.sniffity.Panthalassa;
import com.github.sniffity.entity.creature.CreatureKronosaurus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import javax.annotation.Nullable;
import java.util.Optional;

public class ModelKronosaurus extends GeoModel<CreatureKronosaurus> {

    @Override
    public ResourceLocation getModelResource(CreatureKronosaurus animatable) {
        return ResourceLocation.fromNamespaceAndPath(Panthalassa.MODID,"geo/creature/kronosaurus/kronosaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CreatureKronosaurus animatable) {
        return ResourceLocation.fromNamespaceAndPath(Panthalassa.MODID,"textures/creature/kronosaurus/kronosaurus.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CreatureKronosaurus animatable) {
        return ResourceLocation.fromNamespaceAndPath(Panthalassa.MODID,"animations/creature/kronosaurus/kronosaurus.json");
    }


    @Override
    public void setCustomAnimations(CreatureKronosaurus entity, long instanceID, @Nullable AnimationState<CreatureKronosaurus> animationState) {
        dynamicYaw1(entity, animationState);
        super.setCustomAnimations(entity, instanceID,animationState);

    }

    public void dynamicYaw1(CreatureKronosaurus entity, AnimationState<CreatureKronosaurus> animationState){
        float multiplier = 5.0F;
        float setYawValue = entity.prevSetYaw+(entity.setYaw-entity.prevSetYaw)*animationState.getPartialTick();
        (this.getAnimationProcessor().getBone("lower_torso")).setRotY(setYawValue*multiplier);
        (this.getAnimationProcessor().getBone("lower_torso_tail")).setRotY(setYawValue*multiplier);
        (this.getAnimationProcessor().getBone("tail_section_1")).setRotY(setYawValue*4.0F);
        (this.getAnimationProcessor().getBone("tail_section_2")).setRotY(setYawValue*3.0F);
        (this.getAnimationProcessor().getBone("tail_section_3")).setRotY(setYawValue*2.0F);
        (this.getAnimationProcessor().getBone("tail_section_4")).setRotY(setYawValue);
        (this.getAnimationProcessor().getBone("neck")).setRotY(-setYawValue*10F);
    }

    @Override
    public Optional<GeoBone> getBone(String boneName) {
        return super.getBone(boneName);
    }
}