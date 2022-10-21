package com.github.sniffity.panthalassa.client.model.display;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.display.DisplayGiantOrthoconeShell;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static java.lang.Math.PI;


public class ModelGiantOrthoconeShell extends AnimatedGeoModel<DisplayGiantOrthoconeShell>
{
    @Override
    public ResourceLocation getModelResource(DisplayGiantOrthoconeShell object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/display/giant_orthocone_shell/giant_orthocone_shell.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DisplayGiantOrthoconeShell object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/display/giant_orthocone_shell/giant_orthocone_shell.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DisplayGiantOrthoconeShell animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/display/giant_orthocone_shell/giant_orthocone_shell.json");
    }

    @Override
    public void setLivingAnimations(DisplayGiantOrthoconeShell entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        (this.getAnimationProcessor().getBone("shell1")).setRotationY((float) -(entity.yRot *(PI/180.0F) + PI/2));
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}