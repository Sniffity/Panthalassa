package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import javax.annotation.Nullable;
import static java.lang.Math.PI;

public class ModelMegalodon extends AnimatedGeoModel<EntityMegalodon> {
    @Override
    public ResourceLocation getModelLocation(EntityMegalodon object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/megalodon/megalodon.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMegalodon object) {
        if (object.getTextureVariant() == 0){
            return new ResourceLocation(Panthalassa.MODID,"textures/creature/megalodon/megalodon_white.png");
        } else if (object.getTextureVariant() == 1) {
            return new ResourceLocation(Panthalassa.MODID,"textures/creature/megalodon/megalodon_pink.png");
        } else {
            return new ResourceLocation(Panthalassa.MODID,"textures/creature/megalodon/megalodon_green.png");
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityMegalodon animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/megalodon/megalodon.json");
    }

    @Override
    public void setLivingAnimations(EntityMegalodon entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if ((entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) || entity.getBreaching()) {
            (this.getAnimationProcessor().getBone("neck")).setRotationX(entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick());
        }
        (this.getAnimationProcessor().getBone("tail1")).setRotationY((float) (entity.adjustYaw * (PI / 180.0F)) * 4.0F);
        (this.getAnimationProcessor().getBone("tail2")).setRotationY((float) (entity.adjustYaw * (PI / 180.0F)) * 4.0F);
        (this.getAnimationProcessor().getBone("tail3")).setRotationY((float) (entity.adjustYaw * (PI / 180.0F)) * 4.0F);
        (this.getAnimationProcessor().getBone("head")).setRotationY((float) (-entity.adjustYaw * (PI / 180.0F)) * 4.0F);
    }


    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}