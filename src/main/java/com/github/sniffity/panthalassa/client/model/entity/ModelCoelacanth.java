package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityArchelon;
import com.github.sniffity.panthalassa.server.entity.creature.EntityCoelacanth;
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
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

