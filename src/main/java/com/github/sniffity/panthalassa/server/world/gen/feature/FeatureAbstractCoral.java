package com.github.sniffity.panthalassa.server.world.gen.feature;


import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import java.util.Random;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class FeatureAbstractCoral extends Feature<NoneFeatureConfiguration> {
    public FeatureAbstractCoral(Codec<NoneFeatureConfiguration> p_i231940_1_) {
        super(p_i231940_1_);
    }

    public boolean place(WorldGenLevel worldgenlevel, ChunkGenerator p_241855_2_, Random rand, BlockPos p_241855_4_, NoneFeatureConfiguration ColumnFeatureConfiguration) {
        BlockState blockstate = BlockTags.CORAL_BLOCKS.getRandomElement(rand).defaultBlockState();
        return this.placeFeature(worldgenlevel, rand, p_241855_4_, blockstate);
    }

    protected abstract boolean placeFeature(LevelAccessor p_204623_1_, Random p_204623_2_, BlockPos p_204623_3_, BlockState p_204623_4_);

    protected boolean placeCoralBlock(LevelAccessor p_204624_1_, Random p_204624_2_, BlockPos p_204624_3_, BlockState p_204624_4_) {
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
                        BlockState blockstate1 = BlockTags.WALL_CORALS.getRandomElement(p_204624_2_).defaultBlockState().setValue(BaseCoralWallFanBlock.FACING, direction);
                        p_204624_1_.setBlock(blockpos1, blockstate1, 2);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    protected boolean placeSecondaryCoralBlock(LevelAccessor p_204624_1_, Random p_204624_2_, BlockPos p_204624_3_, BlockState p_204624_4_) {
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
                        BlockState blockstate1 = BlockTags.WALL_CORALS.getRandomElement(p_204624_2_).defaultBlockState().setValue(BaseCoralWallFanBlock.FACING, direction);
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