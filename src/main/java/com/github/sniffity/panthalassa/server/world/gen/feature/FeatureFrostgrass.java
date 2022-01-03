package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

import java.util.Random;

public class FeatureFrostgrass extends Feature<ProbabilityFeatureConfiguration> {
    public FeatureFrostgrass(Codec<ProbabilityFeatureConfiguration> p_i231988_1_) {
        super(p_i231988_1_);
    }

    public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> p_159884_) {

        Random rand = p_159884_.random();
        WorldGenLevel worldgenlevel = p_159884_.level();
        BlockPos pos = p_159884_.origin();
        ProbabilityFeatureConfiguration probabilityfeatureconfiguration = p_159884_.config();

        boolean flag = false;
        int i = rand.nextInt(8) - rand.nextInt(8);
        int j = rand.nextInt(8) - rand.nextInt(8);
        double r = Math.floor(Math.random()*(81)+20);
        BlockPos blockpos = new BlockPos(pos.getX() + i, r, pos.getZ() + j);
        if (worldgenlevel.getBlockState(blockpos).is(PanthalassaBlocks.PANTHALASSA_WATER.get())) {
            BlockState blockstate = PanthalassaBlocks.FROSTGRASS.get().defaultBlockState();
            if (blockstate.canSurvive(worldgenlevel, blockpos)) {
                worldgenlevel.setBlock(blockpos, blockstate, 2);
                flag = true;
            }
        }
        return flag;
    }
}
