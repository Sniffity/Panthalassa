package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import javax.annotation.Nullable;
import static java.lang.Math.PI;

public class ModelKronosaurus extends AnimatedGeoModel<EntityKronosaurus>
{
    @Override
    public ResourceLocation getModelLocation(EntityKronosaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/creature/kronosaurus/kronosaurus.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityKronosaurus object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/creature/kronosaurus/kronosaurus.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityKronosaurus animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/creature/kronosaurus/kronosaurus.json");
    }

    @Override
    public void setLivingAnimations(EntityKronosaurus entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        if (entity.isInWater() && !entity.level.getBlockState(entity.blockPosition().below()).canOcclude()) {
            (this.getAnimationProcessor().getBone("torso")).setRotationX(entity.prevRotationPitch+(entity.rotationPitch-entity.prevRotationPitch)*customPredicate.getPartialTick());
        }
        (this.getAnimationProcessor().getBone("lower_torso")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5.0F);
        (this.getAnimationProcessor().getBone("lower_torso_tail")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5.0F);
        (this.getAnimationProcessor().getBone("tail_section_1")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5.0F);
        (this.getAnimationProcessor().getBone("tail_section_2")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5.0F);
        (this.getAnimationProcessor().getBone("tail_section_3")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5.0F);
        (this.getAnimationProcessor().getBone("tail_section_4")).setRotationY((float)(entity.adjustYaw*(PI/180.0F))*5.0F);
        (this.getAnimationProcessor().getBone("neck")).setRotationY((float)(-entity.adjustYaw*(PI/180.0F))*5.0F);

    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}