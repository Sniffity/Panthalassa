package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.CoralFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FeatureCoralTree extends FeatureAbstractCoral {
    public FeatureCoralTree(Codec<NoFeatureConfig> p_i231942_1_) {
        super(p_i231942_1_);
    }

    protected boolean placeFeature(IWorld p_204623_1_, Random p_204623_2_, BlockPos p_204623_3_, BlockState p_204623_4_) {
        double r = Math.floor(Math.random() * (81) + 20);
        BlockPos blockposAdjusted = new BlockPos(p_204623_3_.getX(), r, p_204623_3_.getZ());

        if (p_204623_1_.getBlockState(blockposAdjusted.below()).is(PanthalassaBlocks.PANTHALASSA_SAND.get())) {

            BlockPos.Mutable blockpos$mutable = blockposAdjusted.mutable();
            int i = p_204623_2_.nextInt(3) + 1;

            for (int j = 0; j < i; ++j) {
                if (!this.placeSecondaryCoralBlock(p_204623_1_, p_204623_2_, blockpos$mutable, p_204623_4_)) {
                    return true;
                }

                blockpos$mutable.move(Direction.UP);
            }

            BlockPos blockpos = blockpos$mutable.immutable();
            int k = p_204623_2_.nextInt(3) + 2;
            List<Direction> list = Lists.newArrayList(Direction.Plane.HORIZONTAL);
            Collections.shuffle(list, p_204623_2_);

            for (Direction direction : list.subList(0, k)) {
                blockpos$mutable.set(blockpos);
                blockpos$mutable.move(direction);
                int l = p_204623_2_.nextInt(5) + 2;
                int i1 = 0;

                for (int j1 = 0; j1 < l && this.placeSecondaryCoralBlock(p_204623_1_, p_204623_2_, blockpos$mutable, p_204623_4_); ++j1) {
                    ++i1;
                    blockpos$mutable.move(Direction.UP);
                    if (j1 == 0 || i1 >= 2 && p_204623_2_.nextFloat() < 0.25F) {
                        blockpos$mutable.move(direction);
                        i1 = 0;
                    }
                }
            }
        }
        return true;
    }
}