package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.block.BlockKrethrossTop;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FeatureKrethross extends Feature<NoneFeatureConfiguration> {
    public FeatureKrethross(Codec<NoneFeatureConfiguration> p_i231967_1_) {
        super(p_i231967_1_);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159956_) {
        RandomSource rand = p_159956_.random();
        WorldGenLevel worldgenlevel = p_159956_.level();
        BlockPos pos = p_159956_.origin();

        int i = 0;
        int kelp_limit = 2;
        double r = Math.floor(Math.random()*(81)+20);
        BlockPos blockpos = new BlockPos(pos.getX(), r, pos.getZ());
        if ((worldgenlevel.getBlockState(blockpos).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))&&(pos.getY()<kelp_limit)) {
            BlockState blockstate = PanthalassaBlocks.KRETHROSS.get().defaultBlockState();
            BlockState blockstate1 = PanthalassaBlocks.KRETHROSS_PLANT.get().defaultBlockState();
            int k = 1 + rand.nextInt(20);

            for(int l = 0; l <= k; ++l) {
                if (worldgenlevel.getBlockState(blockpos).is(PanthalassaBlocks.PANTHALASSA_WATER.get()) && (worldgenlevel.getBlockState(blockpos.above())).is(PanthalassaBlocks.PANTHALASSA_WATER.get()) && blockstate1.canSurvive(worldgenlevel, blockpos) && !worldgenlevel.getBlockState(blockpos.below()).is(PanthalassaBlocks.KRETHROSS.get())) {
                    if (l == k) {
                        worldgenlevel.setBlock(blockpos, blockstate.setValue(BlockKrethrossTop.AGE, Integer.valueOf(rand.nextInt(4) + 20)), 2);
                        ++i;
                    } else {
                        worldgenlevel.setBlock(blockpos, blockstate1, 2);
                    }
                } else if (l > 0) {
                    BlockPos blockpos1 = blockpos.below();
                    if ((blockstate.canSurvive(worldgenlevel, blockpos1) && !worldgenlevel.getBlockState(blockpos1.below()).is(PanthalassaBlocks.KRETHROSS.get()))) {
                        worldgenlevel.setBlock(blockpos1, blockstate.setValue(BlockKrethrossTop.AGE, Integer.valueOf(rand.nextInt(4) + 20)), 2);
                        ++i;
                    }
                    break;
                }
                blockpos = blockpos.above();
            }
        }
        return i > 0;
    }
}