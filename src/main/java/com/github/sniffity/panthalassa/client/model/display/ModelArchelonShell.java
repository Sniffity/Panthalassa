package com.github.sniffity.panthalassa.client.model.display;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.display.DisplayArchelonShell;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static java.lang.Math.PI;

public class ModelArchelonShell extends AnimatedGeoModel<DisplayArchelonShell>
{
    @Override
    public ResourceLocation getModelLocation(DisplayArchelonShell object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/display/archelon_shell/archelon_shell.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DisplayArchelonShell object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/display/archelon_shell/archelon_shell.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DisplayArchelonShell animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/display/archelon_shell/archelon_shell.json");
    }

    @Override
    public void setLivingAnimations(DisplayArchelonShell entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        (this.getAnimationProcessor().getBone("shell")).setRotationY((float) -(entity.yRot *(PI/180.0F) + PI/2));
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}