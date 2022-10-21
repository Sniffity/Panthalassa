package com.github.sniffity.panthalassa.client.model.blockentity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.block.BlockHydrothermalVentBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelHydrothermalVent extends AnimatedGeoModel<BlockHydrothermalVentBlockEntity> {

    @Override
    public ResourceLocation getAnimationResource(BlockHydrothermalVentBlockEntity animatable) {
        return new ResourceLocation(Panthalassa.MODID, "animations/blockentity/hydrothermal_vent/hydrothermal_vent.json");
    }

    @Override
    public ResourceLocation getModelResource(BlockHydrothermalVentBlockEntity animatable) {
        return new ResourceLocation(Panthalassa.MODID, "geo/blockentity/hydrothermal_vent/hydrothermal_vent.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlockHydrothermalVentBlockEntity entity) {
        return new ResourceLocation(Panthalassa.MODID, "textures/block/hydrothermal_vent.png");
    }
}
