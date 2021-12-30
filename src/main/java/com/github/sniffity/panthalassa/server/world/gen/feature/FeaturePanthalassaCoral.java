package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public abstract class FeaturePanthalassaCoral extends Feature<NoneFeatureConfiguration> {
    public FeaturePanthalassaCoral(Codec<NoneFeatureConfiguration> p_65429_) {
        super(p_65429_);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159536_) {
        Random random = p_159536_.random();
        WorldGenLevel worldgenlevel = p_159536_.level();
        BlockPos blockpos = p_159536_.origin();
        BlockState blockstate = BlockTags.CORAL_BLOCKS.getRandomElement(random).defaultBlockState();
        return this.placeFeature(worldgenlevel, random, blockpos, blockstate);
    }

    protected abstract boolean placeFeature(LevelAccessor p_65430_, Random p_65431_, BlockPos p_65432_, BlockState p_65433_);

    protected boolean placeCoralBlock(LevelAccessor p_65447_, Random p_65448_, BlockPos p_65449_, BlockState p_65450_) {
        BlockPos blockpos = p_65449_.above();
        BlockState blockstate = p_65447_.getBlockState(p_65449_);
        BlockPos blockposBelow = p_65449_.below();
        BlockState blockstateBelow = p_65447_.getBlockState(blockposBelow);
        BlockState blockStateAbove = p_65447_.getBlockState(blockpos);

        if (((blockstate.is(PanthalassaBlocks.PANTHALASSA_WATER.get())) && (blockstateBelow.is(PanthalassaBlocks.PANTHALASSA_SAND.get()))) || blockstate.is(BlockTags.CORALS) && (blockStateAbove.is(PanthalassaBlocks.PANTHALASSA_WATER.get()))){
            p_65447_.setBlock(p_65449_, p_65450_, 3);
            if (p_65448_.nextFloat() < 0.25F) {
                p_65447_.setBlock(blockpos, BlockTags.CORALS.getRandomElement(p_65448_).defaultBlockState(), 2);
            } else if (p_65448_.nextFloat() < 0.05F) {
                p_65447_.setBlock(blockpos, Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, Integer.valueOf(p_65448_.nextInt(4) + 1)), 2);
            }

            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (p_65448_.nextFloat() < 0.2F) {
                    BlockPos blockpos1 = p_65449_.relative(direction);
                    if (p_65447_.getBlockState(blockpos1).is(PanthalassaBlocks.PANTHALASSA_WATER.get())) {
                        BlockState blockstate1 = BlockTags.WALL_CORALS.getRandomElement(p_65448_).defaultBlockState();
                        if (blockstate1.hasProperty(BaseCoralWallFanBlock.FACING)) {
                            blockstate1 = blockstate1.setValue(BaseCoralWallFanBlock.FACING, direction);
                        }
                        p_65447_.setBlock(blockpos1, blockstate1, 2);
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}