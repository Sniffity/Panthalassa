package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FeatureCoralTree extends FeaturePanthalassaAbstractCoral {
    public FeatureCoralTree(Codec<NoneFeatureConfiguration> p_i231942_1_) {
        super(p_i231942_1_);
    }

    protected boolean placeFeature(LevelAccessor p_65490_, Random p_65491_, BlockPos p_65492_, BlockState p_65493_) {
        double r = Math.floor(Math.random() * (81) + 20);
        BlockPos blockposAdjusted = new BlockPos(p_65492_.getX(), r, p_65492_.getZ());

        BlockPos.MutableBlockPos blockpos$mutableblockpos = blockposAdjusted.mutable();
        int i = p_65491_.nextInt(3) + 1;

        for(int j = 0; j < i; ++j) {
            if (!this.placeSecondaryCoralBlock(p_65490_, p_65491_, blockpos$mutableblockpos, p_65493_)) {
                return true;
            }

            blockpos$mutableblockpos.move(Direction.UP);
        }

        BlockPos blockpos = blockpos$mutableblockpos.immutable();
        int k = p_65491_.nextInt(3) + 2;
        List<Direction> list = Lists.newArrayList(Direction.Plane.HORIZONTAL);
        Collections.shuffle(list, p_65491_);

        for(Direction direction : list.subList(0, k)) {
            blockpos$mutableblockpos.set(blockpos);
            blockpos$mutableblockpos.move(direction);
            int l = p_65491_.nextInt(5) + 2;
            int i1 = 0;

            for(int j1 = 0; j1 < l && this.placeSecondaryCoralBlock(p_65490_, p_65491_, blockpos$mutableblockpos, p_65493_); ++j1) {
                ++i1;
                blockpos$mutableblockpos.move(Direction.UP);
                if (j1 == 0 || i1 >= 2 && p_65491_.nextFloat() < 0.25F) {
                    blockpos$mutableblockpos.move(direction);
                    i1 = 0;
                }
            }
        }

        return true;
    }
}