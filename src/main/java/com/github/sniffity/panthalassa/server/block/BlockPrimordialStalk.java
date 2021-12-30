package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPrimordialStalk extends PipeBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockPrimordialStalk(BlockBehaviour.Properties p_i48428_1_) {
        super(0.3125F, p_i48428_1_);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)).setValue(UP, Boolean.valueOf(false)).setValue(DOWN, Boolean.valueOf(false)));
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
        return this.getStateForPlacement(p_196258_1_.getLevel(), p_196258_1_.getClickedPos());
    }

    public BlockState getStateForPlacement(BlockGetter p_196497_1_, BlockPos p_196497_2_) {
        FluidState fluidstate = p_196497_1_.getFluidState(p_196497_2_);
        Block block = p_196497_1_.getBlockState(p_196497_2_.below()).getBlock();
        Block block1 = p_196497_1_.getBlockState(p_196497_2_.above()).getBlock();
        Block block2 = p_196497_1_.getBlockState(p_196497_2_.north()).getBlock();
        Block block3 = p_196497_1_.getBlockState(p_196497_2_.east()).getBlock();
        Block block4 = p_196497_1_.getBlockState(p_196497_2_.south()).getBlock();
        Block block5 = p_196497_1_.getBlockState(p_196497_2_.west()).getBlock();
        return this.defaultBlockState().setValue(DOWN, Boolean.valueOf(block == this || block == PanthalassaBlocks.PRIMORDIAL_STALK.get() || block == PanthalassaBlocks.PANTHALASSA_SAND.get())).setValue(UP, Boolean.valueOf(block1 == this || block1 == PanthalassaBlocks.PRIMORDIAL_STALK.get())).setValue(NORTH, Boolean.valueOf(block2 == this || block2 == PanthalassaBlocks.PRIMORDIAL_STALK.get())).setValue(EAST, Boolean.valueOf(block3 == this || block3 == PanthalassaBlocks.PRIMORDIAL_STALK.get())).setValue(SOUTH, Boolean.valueOf(block4 == this || block4 == PanthalassaBlocks.PRIMORDIAL_STALK.get())).setValue(WEST, Boolean.valueOf(block5 == this || block5 == PanthalassaBlocks.PRIMORDIAL_STALK.get())).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }


    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        if (p_196271_1_.getValue(WATERLOGGED)) {
            p_196271_4_.getFluidTicks().hasScheduledTick(p_196271_5_, Fluids.WATER);
        }
        if (!p_196271_1_.canSurvive(p_196271_4_, p_196271_5_)) {
            p_196271_4_.scheduleTick(p_196271_5_, this, 1);
            return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
        } else {
            boolean flag = p_196271_3_.getBlock() == this || p_196271_3_.is(PanthalassaBlocks.PRIMORDIAL_STALK.get()) || p_196271_2_ == Direction.DOWN && (p_196271_3_.is(PanthalassaBlocks.PANTHALASSA_SAND.get()));
            return p_196271_1_.setValue(PROPERTY_BY_DIRECTION.get(p_196271_2_), Boolean.valueOf(flag));
        }
    }


    public void tick(BlockState p_51714_, ServerLevel p_51715_, BlockPos p_51716_, Random p_51717_) {
        if (!p_51714_.canSurvive(p_51715_, p_51716_)) {
            p_51715_.destroyBlock(p_51716_, true);
        }

    }
    public boolean canSurvive(BlockState p_51724_, LevelReader p_51725_, BlockPos p_51726_) {

        BlockState blockstate = p_51725_.getBlockState(p_51726_.below());
        boolean flag = !p_51725_.getBlockState(p_51726_.above()).is(Blocks.WATER) && !blockstate.is(Blocks.WATER) ;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = p_51726_.relative(direction);
            BlockState blockstate1 = p_51725_.getBlockState(blockpos);
            if (blockstate1.is(this)) {
                if (flag) {
                    return false;
                }

                BlockState blockstate2 = p_51725_.getBlockState(blockpos.below());
                if (blockstate2.is(this) || blockstate2.is(PanthalassaBlocks.PANTHALASSA_SAND.get())) {
                    return true;
                }
            }
        }
        return blockstate.is(this) || blockstate.is(PanthalassaBlocks.PANTHALASSA_SAND.get());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED);
    }

    public FluidState getFluidState(BlockState p_204507_1_) {
        return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
    }
    public boolean isPathfindable(BlockState p_196266_1_, BlockGetter p_196266_2_, BlockPos p_196266_3_, PathComputationType p_196266_4_) {
        return false;
    }

    public static void generatePlant(LevelAccessor p_185603_0_, BlockPos p_185603_1_, Random p_185603_2_, int p_185603_3_) {
        p_185603_0_.setBlock(p_185603_1_, ((BlockPrimordialStalk) PanthalassaBlocks.PRIMORDIAL_STALK.get()).getStateForPlacement(p_185603_0_, p_185603_1_), 2);
        growTreeRecursive(p_185603_0_, p_185603_1_, p_185603_2_, p_185603_1_, p_185603_3_, 0);
    }

    private static void growTreeRecursive(LevelAccessor p_185601_0_, BlockPos p_185601_1_, Random p_185601_2_, BlockPos p_185601_3_, int p_185601_4_, int p_185601_5_) {
        BlockPrimordialStalk primordialStalk = (BlockPrimordialStalk) PanthalassaBlocks.PRIMORDIAL_STALK.get();
        int i = p_185601_2_.nextInt(20) + 1;
        if (p_185601_5_ == 0) {
            ++i;
        }

        for (int j = 0; j < i; ++j) {
            BlockPos blockpos = p_185601_1_.above(j + 1);
            if (!allNeighborsEmpty(p_185601_0_, blockpos, (Direction) null)) {
                return;
            }

            p_185601_0_.setBlock(blockpos, primordialStalk.getStateForPlacement(p_185601_0_, blockpos), 2);
            p_185601_0_.setBlock(blockpos.below(), primordialStalk.getStateForPlacement(p_185601_0_, blockpos.below()), 2);
        }


        if (p_185601_5_ < 4) {
            int l = p_185601_2_.nextInt(4);
            if (p_185601_5_ == 0) {
                ++l;
            }


            for (int k = 0; k < l; ++k) {
                Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(p_185601_2_);
                BlockPos blockpos1 = p_185601_1_.above(i).relative(direction);
                if (Math.abs(blockpos1.getX() - p_185601_3_.getX()) < p_185601_4_ && Math.abs(blockpos1.getZ() - p_185601_3_.getZ()) < p_185601_4_ && p_185601_0_.isWaterAt(blockpos1) && p_185601_0_.isWaterAt(blockpos1.below())) {
                    p_185601_0_.setBlock(blockpos1, primordialStalk.getStateForPlacement(p_185601_0_, blockpos1), 2);
                    p_185601_0_.setBlock(blockpos1.relative(direction.getOpposite()), primordialStalk.getStateForPlacement(p_185601_0_, blockpos1.relative(direction.getOpposite())), 2);
                    growTreeRecursive(p_185601_0_, blockpos1, p_185601_2_, p_185601_3_, p_185601_4_, p_185601_5_ + 1);
                }
            }
        }
    }

    private static boolean allNeighborsEmpty(LevelReader p_185604_0_, BlockPos p_185604_1_, @Nullable Direction p_185604_2_) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if (direction != p_185604_2_ && !p_185604_0_.isWaterAt(p_185604_1_.relative(direction))) {
                return false;
            }
        }
        return true;
    }
}