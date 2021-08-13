package com.github.sniffity.panthalassa.client.model.entity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelMegalodon extends AnimatedGeoModel<EntityMegalodon> {
    @Override
    public ResourceLocation getModelLocation(EntityMegalodon object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/megalodon/megalodon.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMegalodon object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/entity/megalodon/megalodon.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityMegalodon animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/megalodon/megalodon.json");
    }

    @Override
    public void setLivingAnimations(EntityMegalodon entity, Integer uniqueID) {
        super.setLivingAnimations(entity, uniqueID);
    }

    @Override
    public IBone getBone(String boneName) {
        return super.getBone(boneName);
    }
}

