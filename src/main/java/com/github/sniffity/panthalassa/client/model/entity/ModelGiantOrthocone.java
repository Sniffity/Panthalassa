package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityGiantOrthocone;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static java.lang.Math.PI;


public class ModelGiantOrthocone extends AnimatedGeoModel<EntityGiantOrthocone>
{
    @Override
    public ResourceLocation getModelResource(EntityGiantOrthocone object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/giant_orthocone/giant_orthocone.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EntityGiantOrthocone object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/giant_orthocone/giant_orthocone.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EntityGiantOrthocone animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/giant_orthocone/giant_orthocone.json");
    }

    @Override
    public void setLivingAnimations(EntityGiantOrthocone entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            (this.getAnimationProcessor().getBone("shell1")).setRotationX(-(float) (entity.xRot*(PI/180.0F)));
        }
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}