package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.block.BlockKrethrossTop;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class FeatureKrethross extends Feature<NoFeatureConfig> {
    public FeatureKrethross(Codec<NoFeatureConfig> p_i231967_1_) {
        super(p_i231967_1_);
    }

    @Override
    public boolean place(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        int i = 0;
        int kelp_limit = 2;
        double r = Math.floor(Math.random()*(81)+20);
        BlockPos blockpos = new BlockPos(pos.getX(), r, pos.getZ());
        if ((reader.getBlockState(blockpos).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))&&(pos.getY()<kelp_limit)) {
            BlockState blockstate = PanthalassaBlocks.KRETHROSS.get().defaultBlockState();
            BlockState blockstate1 = PanthalassaBlocks.KRETHROSS_PLANT.get().defaultBlockState();
            int k = 1 + rand.nextInt(20);

            for(int l = 0; l <= k; ++l) {
                if (reader.getBlockState(blockpos).is(PanthalassaBlocks.PANTHALASSA_WATER.get()) && (reader.getBlockState(blockpos.above())).is(PanthalassaBlocks.PANTHALASSA_WATER.get()) && blockstate1.canSurvive(reader, blockpos) && !reader.getBlockState(blockpos.below()).is(PanthalassaBlocks.KRETHROSS.get())) {
                    if (l == k) {
                        reader.setBlock(blockpos, blockstate.setValue(BlockKrethrossTop.AGE, Integer.valueOf(rand.nextInt(4) + 20)), 2);
                        ++i;
                    } else {
                        reader.setBlock(blockpos, blockstate1, 2);
                    }
                } else if (l > 0) {
                    BlockPos blockpos1 = blockpos.below();
                    if ((blockstate.canSurvive(reader, blockpos1) && !reader.getBlockState(blockpos1.below()).is(PanthalassaBlocks.KRETHROSS.get()))) {
                        reader.setBlock(blockpos1, blockstate.setValue(BlockKrethrossTop.AGE, Integer.valueOf(rand.nextInt(4) + 20)), 2);
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