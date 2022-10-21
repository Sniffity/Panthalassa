package com.github.sniffity.panthalassa.client.model.display;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.display.DisplayMegalodonJaws;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

import static java.lang.Math.PI;

public class ModelMegalodonJaws extends AnimatedGeoModel<DisplayMegalodonJaws>
{
    @Override
    public ResourceLocation getModelResource(DisplayMegalodonJaws object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/display/megalodon_jaws/megalodon_jaws.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DisplayMegalodonJaws object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/display/megalodon_jaws/megalodon_jaws.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DisplayMegalodonJaws animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/display/megalodon_jaws/megalodon_jaws.json");
    }

    @Override
    public void setLivingAnimations(DisplayMegalodonJaws entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        (this.getAnimationProcessor().getBone("main")).setRotationY((float) -(entity.yRot *(PI/180.0F) + PI/2));
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}