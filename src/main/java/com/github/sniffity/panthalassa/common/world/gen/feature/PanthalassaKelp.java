package com.github.sniffity.panthalassa.common.world.gen.feature;

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

public class PanthalassaKelp extends Feature<NoFeatureConfig> {
    public PanthalassaKelp(Codec<NoFeatureConfig> p_i231967_1_) {
        super(p_i231967_1_);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        int i = 0;
        int kelp_limit = 128;
        BlockPos blockpos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        if ((reader.getBlockState(blockpos).getBlock()== Blocks.WATER)&&(pos.getY()<kelp_limit)) {
            BlockState blockstate = Blocks.KELP.getDefaultState();
            BlockState blockstate1 = Blocks.KELP_PLANT.getDefaultState();
            int k = 1 + rand.nextInt(10);

            for(int l = 0; l <= k; ++l) {
                if (reader.getBlockState(blockpos).isIn(Blocks.WATER) && reader.getBlockState(blockpos.up()).isIn(Blocks.WATER) && blockstate1.isValidPosition(reader, blockpos)) {
                    if (l == k) {
                        reader.setBlockState(blockpos, blockstate.with(KelpTopBlock.AGE, Integer.valueOf(rand.nextInt(4) + 20)), 2);
                        ++i;
                    } else {
                        reader.setBlockState(blockpos, blockstate1, 2);
                    }
                } else if (l > 0) {
                    BlockPos blockpos1 = blockpos.down();
                    if (blockstate.isValidPosition(reader, blockpos1) && !reader.getBlockState(blockpos1.down()).isIn(Blocks.KELP)) {
                        reader.setBlockState(blockpos1, blockstate.with(KelpTopBlock.AGE, Integer.valueOf(rand.nextInt(4) + 20)), 2);
                        ++i;
                    }
                    break;
                }
                blockpos = blockpos.up();
            }
        }
        return i > 0;
    }
}