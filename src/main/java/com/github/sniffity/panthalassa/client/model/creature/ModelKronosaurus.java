package com.github.sniffity.panthalassa.client.model.creature;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.entity.creature.CreatureKronosaurus;
import net.minecraft.resources.ResourceLocation;
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
        dynamicYaw(entity, animationState);
        super.setCustomAnimations(entity, instanceID,animationState);

    }

    public void dynamicYaw(CreatureKronosaurus entity, AnimationState<CreatureKronosaurus> animationState){
        float multiplier = 5.175F / entity.adjustYaw;

        float setYawValue = entity.prevSetYaw+(entity.setYaw-entity.prevSetYaw)*animationState.getPartialTick();
        (this.getAnimationProcessor().getBone("lower_torso")).setRotY(setYawValue*multiplier);
        (this.getAnimationProcessor().getBone("lower_torso_tail")).setRotY(setYawValue*multiplier);
        (this.getAnimationProcessor().getBone("tail_section_1")).setRotY(setYawValue*multiplier);
        (this.getAnimationProcessor().getBone("tail_section_2")).setRotY(setYawValue*multiplier);
        (this.getAnimationProcessor().getBone("tail_section_3")).setRotY(setYawValue*multiplier);
        (this.getAnimationProcessor().getBone("tail_section_4")).setRotY(setYawValue*multiplier);
        (this.getAnimationProcessor().getBone("neck")).setRotY(-setYawValue*multiplier);

        float rollMultiplier = 30.0F;
        (this.getAnimationProcessor().getBone("torso")).setRotZ(-setYawValue*rollMultiplier);
    }



    @Override
    public Optional<GeoBone> getBone(String boneName) {
        return super.getBone(boneName);
    }
}