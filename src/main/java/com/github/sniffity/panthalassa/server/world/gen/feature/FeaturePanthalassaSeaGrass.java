package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallSeaGrassBlock;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ProbabilityConfig;

public class FeaturePanthalassaSeaGrass extends Feature<ProbabilityConfig> {
    public FeaturePanthalassaSeaGrass(Codec<ProbabilityConfig> p_i231988_1_) {
        super(p_i231988_1_);
    }

    public boolean place(ISeedReader p_241855_1_, ChunkGenerator p_241855_2_, Random p_241855_3_, BlockPos p_241855_4_, ProbabilityConfig p_241855_5_) {
        boolean flag = false;
        int i = p_241855_3_.nextInt(8) - p_241855_3_.nextInt(8);
        int j = p_241855_3_.nextInt(8) - p_241855_3_.nextInt(8);
        double r = Math.floor(Math.random()*(81)+20);
        BlockPos blockpos = new BlockPos(p_241855_4_.getX() + i, r, p_241855_4_.getZ() + j);
        if (p_241855_1_.getBlockState(blockpos).is(Blocks.WATER)) {
            boolean flag1 = p_241855_3_.nextDouble() < (double)p_241855_5_.probability;
            BlockState blockstate = flag1 ? Blocks.TALL_SEAGRASS.defaultBlockState() : Blocks.SEAGRASS.defaultBlockState();
            if (blockstate.canSurvive(p_241855_1_, blockpos)) {
                if (flag1) {
                    BlockState blockstate1 = blockstate.setValue(TallSeaGrassBlock.HALF, DoubleBlockHalf.UPPER);
                    BlockPos blockpos1 = blockpos.above();
                    if (p_241855_1_.getBlockState(blockpos1).is(Blocks.WATER)) {
                        p_241855_1_.setBlock(blockpos, blockstate, 2);
                        p_241855_1_.setBlock(blockpos1, blockstate1, 2);
                    }
                } else {
                    p_241855_1_.setBlock(blockpos, blockstate, 2);
                }

                flag = true;
            }
        }

        return flag;
    }
}