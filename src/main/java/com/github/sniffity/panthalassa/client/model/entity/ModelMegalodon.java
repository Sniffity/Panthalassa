package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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
        if (entity.isInWater() || !entity.isOnGround()) {
            (this.getAnimationProcessor().getBone("neck")).setRotationX(((float) Mth.atan2((entity.getDeltaMovement().y), Mth.sqrt((float) ((entity.getDeltaMovement().x) * (entity.getDeltaMovement().x) + (entity.getDeltaMovement().z) * (entity.getDeltaMovement().z))))));
        }
        (this.getAnimationProcessor().getBone("tail1")).setRotationY((float) (entity.adjustYaw * (PI / 180.0F)) * 5.0F);
        (this.getAnimationProcessor().getBone("tail2")).setRotationY((float) (entity.adjustYaw * (PI / 180.0F)) * 5.0F);
        (this.getAnimationProcessor().getBone("tail3")).setRotationY((float) (entity.adjustYaw * (PI / 180.0F)) * 5.0F);
        (this.getAnimationProcessor().getBone("head")).setRotationY((float) (-entity.adjustYaw * (PI / 180.0F)) * 5.0F);
    }


    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}