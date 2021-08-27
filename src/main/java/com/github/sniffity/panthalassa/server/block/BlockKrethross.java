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

    public boolean canPlaceLiquid(IBlockReader p_204510_1_, BlockPos p_204510_2_, BlockState p_204510_3_, Fluid p_204510_4_) {
        return false;
    }

    public boolean placeLiquid(IWorld p_204509_1_, BlockPos p_204509_2_, BlockState p_204509_3_, FluidState p_204509_4_) {
        return false;
    }
}