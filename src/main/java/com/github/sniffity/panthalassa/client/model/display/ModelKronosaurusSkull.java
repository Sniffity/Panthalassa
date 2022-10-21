package com.github.sniffity.panthalassa.client.model.display;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.display.DisplayKronosaurusSkull;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static java.lang.Math.PI;

public class ModelKronosaurusSkull extends AnimatedGeoModel<DisplayKronosaurusSkull>
{
    @Override
    public ResourceLocation getModelResource(DisplayKronosaurusSkull object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/display/kronosaurus_skull/kronosaurus_skull.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DisplayKronosaurusSkull object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/display/kronosaurus_skull/kronosaurus_skull.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DisplayKronosaurusSkull animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/display/kronosaurus_skull/kronosaurus_skull.json");
    }

    @Override
    public void setLivingAnimations(DisplayKronosaurusSkull entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        (this.getAnimationProcessor().getBone("neck_main")).setRotationY((float) -(entity.yRot *(PI/180.0F) + PI/2));
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}