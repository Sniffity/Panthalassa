package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class FeatureCoralMushroom extends FeaturePanthalassaAbstractCoral {
    public FeatureCoralMushroom(Codec<NoneFeatureConfiguration> p_i231941_1_) {
        super(p_i231941_1_);
    }

    protected boolean placeFeature(LevelAccessor p_65454_, Random p_65455_, BlockPos p_65456_, BlockState p_65457_) {
        double r = Math.floor(Math.random() * (81) + 20);
        BlockPos blockposAdjusted = new BlockPos(p_65456_.getX(), r, p_65456_.getZ());

        if (p_65454_.getBlockState(blockposAdjusted.below()).is(PanthalassaBlocks.PANTHALASSA_SAND.get()) || p_65454_.getBlockState(blockposAdjusted.below()).is(PanthalassaBlocks.PANTHALASSA_OVERGROWN_SAND.get())) {

            int i = p_65455_.nextInt(3) + 3;
            int j = p_65455_.nextInt(3) + 3;
            int k = p_65455_.nextInt(3) + 3;
            int l = p_65455_.nextInt(3) + 1;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = blockposAdjusted.mutable();

            for (int i1 = 0; i1 <= j; ++i1) {
                for (int j1 = 0; j1 <= i; ++j1) {
                    for (int k1 = 0; k1 <= k; ++k1) {
                        blockpos$mutableblockpos.set(i1 + blockposAdjusted.getX(), j1 + blockposAdjusted.getY(), k1 + blockposAdjusted.getZ());
                        blockpos$mutableblockpos.move(Direction.DOWN, l);
                        if ((i1 != 0 && i1 != j || j1 != 0 && j1 != i) && (k1 != 0 && k1 != k || j1 != 0 && j1 != i) && (i1 != 0 && i1 != j || k1 != 0 && k1 != k) && (i1 == 0 || i1 == j || j1 == 0 || j1 == i || k1 == 0 || k1 == k) && !(p_65455_.nextFloat() < 0.1F) && !this.placeSecondaryCoralBlock(p_65454_, p_65455_, blockpos$mutableblockpos, p_65457_)) {
                        }
                    }
                }
            }
        }
        return true;

    }
}