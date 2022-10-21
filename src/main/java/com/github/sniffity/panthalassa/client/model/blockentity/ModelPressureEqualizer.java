package com.github.sniffity.panthalassa.client.model.blockentity;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.block.BlockPressureEqualizerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class ModelPressureEqualizer extends AnimatedGeoModel<BlockPressureEqualizerBlockEntity> {

    @Override
    public ResourceLocation getAnimationResource(BlockPressureEqualizerBlockEntity animatable) {
        return new ResourceLocation(Panthalassa.MODID, "animations/blockentity/pressure_equalizer/pressure_equalizer.json");
    }

    @Override
    public ResourceLocation getModelResource(BlockPressureEqualizerBlockEntity animatable) {
        return new ResourceLocation(Panthalassa.MODID, "geo/blockentity/pressure_equalizer/pressure_equalizer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlockPressureEqualizerBlockEntity entity) {
        return new ResourceLocation(Panthalassa.MODID, "textures/block/pressure_equalizer.png");
    }
}
