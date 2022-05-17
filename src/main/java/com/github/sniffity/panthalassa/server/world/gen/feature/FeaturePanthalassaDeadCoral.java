package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;

import java.util.Optional;
import java.util.Random;

public abstract class FeaturePanthalassaDeadCoral extends Feature<NoneFeatureConfiguration> {

    public FeaturePanthalassaDeadCoral(Codec<NoneFeatureConfiguration> p_i231940_1_) {
        super(p_i231940_1_);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159536_) {
        Random rand = p_159536_.random();
        WorldGenLevel worldgenlevel = p_159536_.level();
        return this.placeFeature(worldgenlevel, rand, p_159536_.origin(), Blocks.DEAD_BRAIN_CORAL_BLOCK.defaultBlockState());
    }

    protected abstract boolean placeFeature(LevelAccessor p_204623_1_, Random p_204623_2_, BlockPos p_204623_3_, BlockState p_204623_4_);


    protected boolean placeDeadCoralBlock(LevelAccessor p_204624_1_, Random p_204624_2_, BlockPos p_204624_3_, BlockState p_204624_4_) {
        BlockState blockstate = p_204624_1_.getBlockState(p_204624_3_);
        BlockPos blockposAbove = p_204624_3_.above();
        BlockState blockstateAbove = p_204624_1_.getBlockState(blockposAbove);


        if (((blockstate.is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) || blockstate.is(BlockTags.CORALS) && (blockstateAbove.is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            p_204624_1_.setBlock(p_204624_3_, p_204624_4_, 3);
            if (p_204624_2_.nextFloat() < 0.25F) {
                p_204624_1_.setBlock(blockposAbove, Blocks.DEAD_BRAIN_CORAL_BLOCK.defaultBlockState(), 2);
            } else if (p_204624_2_.nextFloat() < 0.05F) {
                p_204624_1_.setBlock(blockposAbove, Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, Integer.valueOf(p_204624_2_.nextInt(4) + 1)), 2);
            }

            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (p_204624_2_.nextFloat() < 0.2F) {
                    BlockPos blockpos1 = p_204624_3_.relative(direction);
                    if (p_204624_1_.getBlockState(blockpos1).is(PanthalassaBlocks.PANTHALASSA_WATER.get())) {
                        BlockState blockstate1 = Blocks.DEAD_BRAIN_CORAL_BLOCK.defaultBlockState();
                        if (blockstate1.hasProperty(BaseCoralWallFanBlock.FACING)) {
                            blockstate1 = blockstate1.setValue(BaseCoralWallFanBlock.FACING, direction);
                        }

                        p_204624_1_.setBlock(blockpos1, blockstate1, 2);
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }
}