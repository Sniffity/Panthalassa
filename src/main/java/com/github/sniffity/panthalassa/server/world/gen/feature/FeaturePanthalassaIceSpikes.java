package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class FeaturePanthalassaIceSpikes extends Feature<NoneFeatureConfiguration> {
    public FeaturePanthalassaIceSpikes(Codec<NoneFeatureConfiguration> p_66003_) {
        super(p_66003_);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159882_) {
        BlockPos blockpos0 = p_159882_.origin();
        Random random = p_159882_.random();
        double r = Math.floor(Math.random() * (81) + 20);
        BlockPos pos0 = new BlockPos(blockpos0.getX(), r, blockpos0.getZ());

        WorldGenLevel worldgenlevel;
        for(worldgenlevel = p_159882_.level(); worldgenlevel.isEmptyBlock(pos0) && pos0.getY() > worldgenlevel.getMinBuildHeight() + 2; pos0 = pos0.below()) {
        }

        if (!worldgenlevel.getBlockState(pos0).is(Blocks.ICE)) {
            return false;
        } else {
            pos0 = pos0.above(random.nextInt(4));
            int i = random.nextInt(4) + 7;
            int j = i / 4 + random.nextInt(2);
            if (j > 1 && random.nextInt(60) == 0) {
                pos0 = pos0.above(10 + random.nextInt(30));
            }

            for(int k = 0; k < i; ++k) {
                float f = (1.0F - (float)k / (float)i) * (float)j;
                int l = Mth.ceil(f);

                for(int i1 = -l; i1 <= l; ++i1) {
                    float f1 = (float)Mth.abs(i1) - 0.25F;

                    for(int j1 = -l; j1 <= l; ++j1) {
                        float f2 = (float)Mth.abs(j1) - 0.25F;
                        if ((i1 == 0 && j1 == 0 || !(f1 * f1 + f2 * f2 > f * f)) && (i1 != -l && i1 != l && j1 != -l && j1 != l || !(random.nextFloat() > 0.75F))) {
                            BlockState blockstate = worldgenlevel.getBlockState(pos0.offset(i1, k, j1));
                            if (blockstate.is(PanthalassaBlocks.PANTHALASSA_WATER.get()) || isDirt(blockstate) || blockstate.is(Blocks.ICE)) {
                                this.setBlock(worldgenlevel, pos0.offset(i1, k, j1), Blocks.PACKED_ICE.defaultBlockState());
                            }

                            if (k != 0 && l > 1) {
                                blockstate = worldgenlevel.getBlockState(pos0.offset(i1, -k, j1));
                                if (blockstate.is(PanthalassaBlocks.PANTHALASSA_WATER.get()) || blockstate.is(Blocks.ICE)) {
                                    this.setBlock(worldgenlevel, pos0.offset(i1, -k, j1), Blocks.PACKED_ICE.defaultBlockState());
                                }
                            }
                        }
                    }
                }
            }

            int k1 = j - 1;
            if (k1 < 0) {
                k1 = 0;
            } else if (k1 > 1) {
                k1 = 1;
            }

            for(int l1 = -k1; l1 <= k1; ++l1) {
                for(int i2 = -k1; i2 <= k1; ++i2) {
                    BlockPos blockpos1 = pos0.offset(l1, -1, i2);
                    int j2 = 50;
                    if (Math.abs(l1) == 1 && Math.abs(i2) == 1) {
                        j2 = random.nextInt(5);
                    }

                    while(blockpos1.getY() > 50) {
                        BlockState blockstate1 = worldgenlevel.getBlockState(blockpos1);
                        if (!blockstate1.isAir() && !isDirt(blockstate1) && !blockstate1.is(PanthalassaBlocks.PANTHALASSA_WATER.get()) && !blockstate1.is(Blocks.ICE) && !blockstate1.is(Blocks.PACKED_ICE)) {
                            break;
                        }

                        this.setBlock(worldgenlevel, blockpos1, Blocks.PACKED_ICE.defaultBlockState());
                        blockpos1 = blockpos1.below();
                        --j2;
                        if (j2 <= 0) {
                            blockpos1 = blockpos1.below(random.nextInt(5) + 1);
                            j2 = random.nextInt(5);
                        }
                    }
                }
            }

            return true;
        }
    }
}