package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.block.BlockHydrothermalVent;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;


public class FeatureHydrothermalVents extends Feature<NoneFeatureConfiguration> {
    public FeatureHydrothermalVents(Codec<NoneFeatureConfiguration> p_i231931_1_) {
        super(p_i231931_1_);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159884_) {
        WorldGenLevel worldgenlevel = p_159884_.level();
        BlockPos pos = p_159884_.origin();
        BlockState state = PanthalassaBlocks.HYDROTHERMAL_VENT.get().defaultBlockState();

        double r = Math.floor(Math.random() * (81) + 20);
        BlockPos pos0 = new BlockPos(pos.getX(), r, pos.getZ());

        if (worldgenlevel.getBlockState(pos0).is(PanthalassaBlocks.PANTHALASSA_WATER.get()) && worldgenlevel.getBlockState(pos0.below()).is(PanthalassaBlocks.PANTHALASSA_ROCK.get())) {
            worldgenlevel.setBlock(pos0, state.setValue(BlockHydrothermalVent.WATERLOGGED, Boolean.TRUE),2);
            return true;
        }
        return false;
    }
}