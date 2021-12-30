package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;
import java.util.Random;

public class FeaturePanthalassaRocks extends Feature<BlockStateConfiguration> {
    public FeaturePanthalassaRocks(Codec<BlockStateConfiguration> p_i231931_1_) {
        super(p_i231931_1_);
    }

    public boolean place(FeaturePlaceContext<BlockStateConfiguration> p_159884_) {
        Random rand = p_159884_.random();
        WorldGenLevel worldgenlevel = p_159884_.level();
        BlockPos pos = p_159884_.origin();

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
                    if (worldgenlevel.getBlockState(pos0.below()).is(PanthalassaBlocks.PANTHALASSA_WATER.get())) {
                        break label46;
                    }

                    if (
                            !(worldgenlevel.getBlockState(pos0.below()) == PanthalassaBlocks.PANTHALASSA_SOIL.get().defaultBlockState())
                            &&
                            !(worldgenlevel.getBlockState(pos0.below()) == PanthalassaBlocks.PANTHALASSA_COARSE_SOIL.get().defaultBlockState())
                            &&
                            !(worldgenlevel.getBlockState(pos0.below()) == PanthalassaBlocks.PANTHALASSA_LOOSE_SOIL.get().defaultBlockState())
                            &&
                            !(worldgenlevel.getBlockState(pos0.below()) == PanthalassaBlocks.ANCIENT_ROCK.get().defaultBlockState())

                    )
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
                            worldgenlevel.setBlock(blockpos, p_159884_.config().state, 4);
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