package com.github.sniffity.panthalassa.client.model.entity;


import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityGiantOrthocone;
import com.github.sniffity.panthalassa.server.entity.display.EntityGiantOrthoconeShell;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static java.lang.Math.PI;


public class ModelGiantOrthoconeShell extends AnimatedGeoModel<EntityGiantOrthoconeShell>
{
    @Override
    public ResourceLocation getModelLocation(EntityGiantOrthoconeShell object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/display/giant_orthocone_shell/giant_orthocone_shell.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGiantOrthoconeShell object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/display/giant_orthocone_shell/giant_orthocone_shell.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityGiantOrthoconeShell animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/display/giant_orthocone_shell/giant_orthocone_shell.json");
    }

    @Override
    public void setLivingAnimations(EntityGiantOrthoconeShell entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}