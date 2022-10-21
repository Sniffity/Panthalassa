package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FeatureCoralClaw extends FeaturePanthalassaAbstractCoral {
    public FeatureCoralClaw(Codec<NoneFeatureConfiguration> p_i231939_1_) {
        super(p_i231939_1_);
    }

    protected boolean placeFeature(LevelAccessor p_65424_, RandomSource p_65425_, BlockPos p_65426_, BlockState p_65427_) {
        double r = Math.floor(Math.random() * (81) + 20);
        BlockPos blockposAdjusted = new BlockPos(p_65426_.getX(), r, p_65426_.getZ());
        Random random = new Random();


        if (!this.placeCoralBlock(p_65424_, p_65425_, blockposAdjusted, p_65427_)) {
            return false;
        } else {
            Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(p_65425_);
            int i = p_65425_.nextInt(2) + 2;
            List<Direction> list = Lists.newArrayList(direction, direction.getClockWise(), direction.getCounterClockWise());
            Collections.shuffle(list, random);

            for (Direction direction1 : list.subList(0, i)) {
                BlockPos.MutableBlockPos blockpos$mutableblockpos = blockposAdjusted.mutable();
                int j = p_65425_.nextInt(2) + 1;
                blockpos$mutableblockpos.move(direction1);
                int k;
                Direction direction2;
                if (direction1 == direction) {
                    direction2 = direction;
                    k = p_65425_.nextInt(3) + 2;
                } else {
                    blockpos$mutableblockpos.move(Direction.UP);
                    Direction[] adirection = new Direction[]{direction1, Direction.UP};
                    direction2 = Util.getRandom(adirection, p_65425_);
                    k = p_65425_.nextInt(3) + 3;
                }

                for (int l = 0; l < j && this.placeSecondaryCoralBlock(p_65424_, p_65425_, blockpos$mutableblockpos, p_65427_); ++l) {
                    blockpos$mutableblockpos.move(direction2);
                }

                blockpos$mutableblockpos.move(direction2.getOpposite());
                blockpos$mutableblockpos.move(Direction.UP);

                for (int i1 = 0; i1 < k; ++i1) {
                    blockpos$mutableblockpos.move(direction);
                    if (!this.placeSecondaryCoralBlock(p_65424_, p_65425_, blockpos$mutableblockpos, p_65427_)) {
                        break;
                    }

                    if (p_65425_.nextFloat() < 0.25F) {
                        blockpos$mutableblockpos.move(Direction.UP);
                    }
                }
            }

            return true;
        }
    }
}