package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPrimordialStalk extends SixWayBlock implements IWaterLoggable {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public BlockPrimordialStalk(AbstractBlock.Properties properties) {
        super(0.3125F, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, Boolean.valueOf(false)).setValue(EAST, Boolean.valueOf(false)).setValue(SOUTH, Boolean.valueOf(false)).setValue(WEST, Boolean.valueOf(false)).setValue(UP, Boolean.valueOf(false)).setValue(DOWN, Boolean.valueOf(false)));
    }

    public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
        return this.getStateForPlacement(blockItemUseContext.getLevel(), blockItemUseContext.getClickedPos());
    }

    public BlockState getStateForPlacement(IBlockReader blockReader, BlockPos blockPos) {
        FluidState fluidstate = blockReader.getFluidState(blockPos);
        Block block = blockReader.getBlockState(blockPos.below()).getBlock();
        Block block1 = blockReader.getBlockState(blockPos.above()).getBlock();
        Block block2 = blockReader.getBlockState(blockPos.north()).getBlock();
        Block block3 = blockReader.getBlockState(blockPos.east()).getBlock();
        Block block4 = blockReader.getBlockState(blockPos.south()).getBlock();
        Block block5 = blockReader.getBlockState(blockPos.west()).getBlock();
        return this.defaultBlockState()
                .setValue(DOWN, block == PanthalassaBlocks.PANTHALASSA_SAND.get())
                .setValue(DOWN, block == PanthalassaBlocks.PANTHALASSA_OVERGROWN_SAND.get())
                .setValue(UP, block1 == PanthalassaBlocks.PRIMORDIAL_STALK.get())
                .setValue(NORTH,block2 == PanthalassaBlocks.PRIMORDIAL_STALK.get())
                .setValue(EAST, block3 == PanthalassaBlocks.PRIMORDIAL_STALK.get())
                .setValue(SOUTH, block4 == PanthalassaBlocks.PRIMORDIAL_STALK.get())
                .setValue(WEST, block5 == PanthalassaBlocks.PRIMORDIAL_STALK.get())
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }
    
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState0, IWorld world, BlockPos blockPos0, BlockPos blockPos1) {
        if (blockState.getValue(WATERLOGGED)) {
            world.getLiquidTicks().scheduleTick(blockPos0, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        if (!blockState.canSurvive(world, blockPos0)) {
            world.getBlockTicks().scheduleTick(blockPos0, this, 1);
            return super.updateShape(blockState, direction, blockState0, world, blockPos0, blockPos1);
        } else {
            boolean flag = blockState0.getBlock() == this || blockState0.is(PanthalassaBlocks.PRIMORDIAL_STALK.get()) || direction == Direction.DOWN && blockState0.is(PanthalassaBlocks.PANTHALASSA_SAND.get());
            return blockState.setValue(PROPERTY_BY_DIRECTION.get(direction), Boolean.valueOf(flag));
        }
    }

    public void tick(BlockState blockState, ServerWorld world, BlockPos blockPos, Random random) {
        if (!blockState.canSurvive(world, blockPos)) {
            world.destroyBlock(blockPos, true);
        }

    }

    public boolean canSurvive(BlockState blockState, IWorldReader worldReader, BlockPos blockPos) {
        BlockState blockstate = worldReader.getBlockState(blockPos.below());
        boolean flag = !worldReader.getBlockState(blockPos.above()).is(Blocks.WATER) && !blockstate.is(Blocks.WATER) ;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockpos = blockPos.relative(direction);
            Block block = worldReader.getBlockState(blockpos).getBlock();
            if (block == this) {
                if (flag) {
                    return false;
                }

                Block block1 = worldReader.getBlockState(blockpos.below()).getBlock();
                if (block1 == this || block1 == PanthalassaBlocks.PANTHALASSA_SAND.get()) {
                    return true;
                }
            }
        }

        Block block2 = blockstate.getBlock();
        return block2 == this || block2 == PanthalassaBlocks.PANTHALASSA_SAND.get();
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED);
    }

    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
    public boolean isPathfindable(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, PathType pathType) {
        return false;
    }

    public static void generatePlant(IWorld world, BlockPos blockPos, Random random, int i) {
        world.setBlock(blockPos, ((BlockPrimordialStalk) PanthalassaBlocks.PRIMORDIAL_STALK.get()).getStateForPlacement(world, blockPos), 2);
        growTreeRecursive(world, blockPos, random, blockPos, i, 0);
    }

    private static void growTreeRecursive(IWorld world, BlockPos blockPos, Random random, BlockPos blockPos0, int g, int h) {
        BlockPrimordialStalk primordialStalk = (BlockPrimordialStalk) PanthalassaBlocks.PRIMORDIAL_STALK.get();
        int i = random.nextInt(20) + 1;
        if (h == 0) {
            ++i;
        }

        for (int j = 0; j < i; ++j) {
            BlockPos blockpos = blockPos.above(j + 1);
            if (!allNeighborsEmpty(world, blockpos, (Direction) null)) {
                return;
            }

            world.setBlock(blockpos, primordialStalk.getStateForPlacement(world, blockpos), 2);
            world.setBlock(blockpos.below(), primordialStalk.getStateForPlacement(world, blockpos.below()), 2);
        }


        if (h < 4) {
            int l = random.nextInt(4);
            if (h == 0) {
                ++l;
            }


            for (int k = 0; k < l; ++k) {
                Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                BlockPos blockpos1 = blockPos.above(i).relative(direction);
                if (Math.abs(blockpos1.getX() - blockPos0.getX()) < g && Math.abs(blockpos1.getZ() - blockPos0.getZ()) < g && world.isWaterAt(blockpos1) && world.isWaterAt(blockpos1.below())) {
                    world.setBlock(blockpos1, primordialStalk.getStateForPlacement(world, blockpos1), 2);
                    world.setBlock(blockpos1.relative(direction.getOpposite()), primordialStalk.getStateForPlacement(world, blockpos1.relative(direction.getOpposite())), 2);
                    growTreeRecursive(world, blockpos1, random, blockPos0, g, h + 1);
                }
            }
        }
    }

    private static boolean allNeighborsEmpty(IWorldReader p_185604_0_, BlockPos p_185604_1_, @Nullable Direction p_185604_2_) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if (direction != p_185604_2_ && !p_185604_0_.isWaterAt(p_185604_1_.relative(direction))) {
                return false;
            }
        }
        return true;
    }
}