package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.KelpTopBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class FeaturePanthalassaKelp extends Feature<NoFeatureConfig> {
    public FeaturePanthalassaKelp(Codec<NoFeatureConfig> p_i231967_1_) {
        super(p_i231967_1_);
    }

    @Override
    public boolean place(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        int i = 0;
        int kelp_limit = 128;
        double r = Math.floor(Math.random()*(81)+20);
        BlockPos blockpos = new BlockPos(pos.getX(), pos.getY()+r, pos.getZ());
        if ((reader.getBlockState(blockpos).getBlock()== Blocks.WATER)&&(pos.getY()<kelp_limit)) {
            BlockState blockstate = Blocks.KELP.defaultBlockState();
            BlockState blockstate1 = Blocks.KELP_PLANT.defaultBlockState();
            int k = 1 + rand.nextInt(20);

            for(int l = 0; l <= k; ++l) {
                if (reader.getBlockState(blockpos).is(Blocks.WATER) && reader.getBlockState(blockpos.above()).is(Blocks.WATER) && blockstate1.canSurvive(reader, blockpos) && !reader.getBlockState(blockpos.below()).is(Blocks.KELP)) {
                    if (l == k) {
                        reader.setBlock(blockpos, blockstate.setValue(KelpTopBlock.AGE, Integer.valueOf(rand.nextInt(4) + 20)), 2);
                        ++i;
                    } else {
                        reader.setBlock(blockpos, blockstate1, 2);
                    }
                } else if (l > 0) {
                    BlockPos blockpos1 = blockpos.below();
                    if ((blockstate.canSurvive(reader, blockpos1) && !reader.getBlockState(blockpos1.below()).is(Blocks.KELP))) {
                        reader.setBlock(blockpos1, blockstate.setValue(KelpTopBlock.AGE, Integer.valueOf(rand.nextInt(4) + 20)), 2);
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