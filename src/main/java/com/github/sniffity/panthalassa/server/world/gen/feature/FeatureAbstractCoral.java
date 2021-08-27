package com.github.sniffity.panthalassa.server.world.gen.feature;


import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public abstract class FeatureAbstractCoral extends Feature<NoFeatureConfig> {
    public FeatureAbstractCoral(Codec<NoFeatureConfig> p_i231940_1_) {
        super(p_i231940_1_);
    }

    public boolean place(ISeedReader p_241855_1_, ChunkGenerator p_241855_2_, Random p_241855_3_, BlockPos p_241855_4_, NoFeatureConfig p_241855_5_) {
        BlockState blockstate = BlockTags.CORAL_BLOCKS.getRandomElement(p_241855_3_).defaultBlockState();
        return this.placeFeature(p_241855_1_, p_241855_3_, p_241855_4_, blockstate);
    }

    protected abstract boolean placeFeature(IWorld p_204623_1_, Random p_204623_2_, BlockPos p_204623_3_, BlockState p_204623_4_);

    protected boolean placeCoralBlock(IWorld p_204624_1_, Random p_204624_2_, BlockPos p_204624_3_, BlockState p_204624_4_) {
        BlockState blockstate = p_204624_1_.getBlockState(p_204624_3_);
        FluidState fluidState = p_204624_1_.getFluidState(p_204624_3_);
        BlockPos blockposAbove = p_204624_3_.above();
        BlockState blockStateAbove = p_204624_1_.getBlockState(blockposAbove);

        BlockPos blockposBelow = p_204624_3_.below();
        BlockState blockstateBelow = p_204624_1_.getBlockState(blockposBelow);

        if (((blockstate.is(PanthalassaBlocks.PANTHALASSA_WATER.get())) && (blockstateBelow.is(PanthalassaBlocks.PANTHALASSA_SAND.get()))) || blockstate.is(BlockTags.CORALS) && (blockStateAbove.is(PanthalassaBlocks.PANTHALASSA_WATER.get()))){
            p_204624_1_.setBlock(p_204624_3_, p_204624_4_, 3);
            if (p_204624_2_.nextFloat() < 0.25F) {
                p_204624_1_.setBlock(blockposAbove, BlockTags.CORALS.getRandomElement(p_204624_2_).defaultBlockState(), 2);
            } else if (p_204624_2_.nextFloat() < 0.05F) {
                p_204624_1_.setBlock(blockposAbove, Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, Integer.valueOf(p_204624_2_.nextInt(4) + 1)), 2);
            }

            for(Direction direction : Direction.Plane.HORIZONTAL) {
                if (p_204624_2_.nextFloat() < 0.2F) {
                    BlockPos blockpos1 = p_204624_3_.relative(direction);
                    if (p_204624_1_.getBlockState(blockpos1).is(PanthalassaBlocks.PANTHALASSA_WATER.get())) {
                        BlockState blockstate1 = BlockTags.WALL_CORALS.getRandomElement(p_204624_2_).defaultBlockState().setValue(DeadCoralWallFanBlock.FACING, direction);
                        p_204624_1_.setBlock(blockpos1, blockstate1, 2);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    protected boolean placeSecondaryCoralBlock(IWorld p_204624_1_, Random p_204624_2_, BlockPos p_204624_3_, BlockState p_204624_4_) {
        BlockState blockstate = p_204624_1_.getBlockState(p_204624_3_);
        FluidState fluidState = p_204624_1_.getFluidState(p_204624_3_);
        BlockPos blockposAbove = p_204624_3_.above();
        BlockState blockstateAbove = p_204624_1_.getBlockState(blockposAbove);
        FluidState fluidStateAbove = p_204624_1_.getFluidState(blockposAbove);


        if (((blockstate.is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) || blockstate.is(BlockTags.CORALS) && (blockstateAbove.is(PanthalassaBlocks.PANTHALASSA_WATER.get()))){
            p_204624_1_.setBlock(p_204624_3_, p_204624_4_, 3);
            if (p_204624_2_.nextFloat() < 0.25F) {
                p_204624_1_.setBlock(blockposAbove, BlockTags.CORALS.getRandomElement(p_204624_2_).defaultBlockState(), 2);
            } else if (p_204624_2_.nextFloat() < 0.05F) {
                p_204624_1_.setBlock(blockposAbove, Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, Integer.valueOf(p_204624_2_.nextInt(4) + 1)), 2);
            }

            for(Direction direction : Direction.Plane.HORIZONTAL) {
                if (p_204624_2_.nextFloat() < 0.2F) {
                    BlockPos blockpos1 = p_204624_3_.relative(direction);
                    if (p_204624_1_.getBlockState(blockpos1).is(PanthalassaBlocks.PANTHALASSA_WATER.get())) {
                        BlockState blockstate1 = BlockTags.WALL_CORALS.getRandomElement(p_204624_2_).defaultBlockState().setValue(DeadCoralWallFanBlock.FACING, direction);
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