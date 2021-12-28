package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.CoralFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FeatureCoralClaw extends CoralFeature {
    public FeatureCoralClaw(Codec<NoneFeatureConfiguration> p_i231939_1_) {
        super(p_i231939_1_);
    }

    protected boolean placeFeature(LevelAccessor p_204623_1_, Random p_204623_2_, BlockPos p_204623_3_, BlockState p_204623_4_) {
        double r = Math.floor(Math.random()*(81)+20);
        BlockPos blockposAdjusted = new BlockPos(p_204623_3_.getX(),r,p_204623_3_.getZ());
        if (!this.placeCoralBlock(p_204623_1_, p_204623_2_, blockposAdjusted, p_204623_4_)) {
            return false;
        } else {
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(p_204623_2_);
            int i = p_204623_2_.nextInt(2) + 2;
            List<Direction> list = Lists.newArrayList(direction, direction.getClockWise(), direction.getCounterClockWise());
            Collections.shuffle(list, p_204623_2_);

            for(Direction direction1 : list.subList(0, i)) {
                BlockPos.MutableBlockPos blockpos$mutable = blockposAdjusted.mutable();
                int j = p_204623_2_.nextInt(2) + 1;
                blockpos$mutable.move(direction1);
                int k;
                Direction direction2;
                if (direction1 == direction) {
                    direction2 = direction;
                    k = p_204623_2_.nextInt(3) + 2;
                } else {
                    blockpos$mutable.move(Direction.UP);
                    Direction[] adirection = new Direction[]{direction1, Direction.UP};
                    direction2 = Util.getRandom(adirection, p_204623_2_);
                    k = p_204623_2_.nextInt(3) + 3;
                }

                for(int l = 0; l < j && this.placeCoralBlock(p_204623_1_, p_204623_2_, blockpos$mutable, p_204623_4_); ++l) {
                    blockpos$mutable.move(direction2);
                }

                blockpos$mutable.move(direction2.getOpposite());
                blockpos$mutable.move(Direction.UP);

                for(int i1 = 0; i1 < k; ++i1) {
                    blockpos$mutable.move(direction);
                    if (!this.placeCoralBlock(p_204623_1_, p_204623_2_, blockpos$mutable, p_204623_4_)) {
                        break;
                    }

                    if (p_204623_2_.nextFloat() < 0.25F) {
                        blockpos$mutable.move(Direction.UP);
                    }
                }
            }

            return true;
        }
    }
}