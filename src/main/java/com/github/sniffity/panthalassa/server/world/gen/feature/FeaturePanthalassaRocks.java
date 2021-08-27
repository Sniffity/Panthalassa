package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;


public class FeaturePanthalassaRocks extends Feature<BlockStateFeatureConfig> {
    public FeaturePanthalassaRocks(Codec<BlockStateFeatureConfig> p_i231931_1_) {
        super(p_i231931_1_);
    }

    public boolean place(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BlockStateFeatureConfig config) {
        boolean flag = false;
        double r = Math.floor(Math.random() * (81) + 20);
        BlockPos pos0 = new BlockPos(pos.getX(), r, pos.getZ());

        while(true) {
            if (!flag) {
                pos0 = new BlockPos(pos0.getX(), pos0.getY(), pos0.getZ());
                flag = true;
            }
            pos0 = new BlockPos(pos0.getX(), pos0.getY(), pos0.getZ());
            label46: {
                if (pos0.getY() > 3) {
                    if (reader.getBlockState(pos0.below()).is(PanthalassaBlocks.PANTHALASSA_WATER.get())) {
                        break label46;
                    }

                    if (
                            !(reader.getBlockState(pos0.below()) == PanthalassaBlocks.PANTHALASSA_SOIL.get().defaultBlockState())
                            &&
                            !(reader.getBlockState(pos0.below()) == PanthalassaBlocks.PANTHALASSA_COARSE_SOIL.get().defaultBlockState())
                            &&
                            !(reader.getBlockState(pos0.below()) == PanthalassaBlocks.PANTHALASSA_LOOSE_SOIL.get().defaultBlockState()))
                     {
                        break label46;
                    }
                }

                if (pos0.getY() <= 3) {
                    return false;
                }

                for(int l = 0; l < 3; ++l) {
                    int i = rand.nextInt(5);
                    int j = rand.nextInt(5);
                    int k = rand.nextInt(5);
                    float f = (float)(i + j + k) * 0.333F + 0.5F;

                    for(BlockPos blockpos : BlockPos.betweenClosed(pos0.offset(-i, -j, -k), pos0.offset(i, j, k))) {
                        if (blockpos.distSqr(pos0) <= (double)(f * f)) {
                            reader.setBlock(blockpos, config.state, 4);
                        }
                    }

                    pos0 = pos0.offset(-1 + rand.nextInt(2), -rand.nextInt(2), -1 + rand.nextInt(2));
                }

                return true;
            }

            pos0 = pos0.below();
        }
    }
}