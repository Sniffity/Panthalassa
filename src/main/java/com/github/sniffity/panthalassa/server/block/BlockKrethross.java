package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class BlockKrethross extends AbstractBodyPlantBlock implements ILiquidContainer {
    public BlockKrethross(AbstractBlock.Properties p_i48782_1_) {
        super(p_i48782_1_, Direction.UP, VoxelShapes.block(), true);
    }

    protected AbstractTopPlantBlock getHeadBlock() {
        return PanthalassaBlocks.KRETHROSS.get();
    }

    public FluidState getFluidState(BlockState blockState) {
        return Fluids.WATER.getSource(false);
    }

    public boolean canPlaceLiquid(IBlockReader blockReader, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return false;
    }

    public boolean placeLiquid(IWorld world, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        return false;
    }
}