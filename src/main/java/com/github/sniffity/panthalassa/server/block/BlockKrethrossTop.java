package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockKrethrossTop extends AbstractTopPlantBlock implements ILiquidContainer {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);

    public BlockKrethrossTop(AbstractBlock.Properties p_i48781_1_) {
        super(p_i48781_1_, Direction.UP, SHAPE, true, 0.14D);
    }

    protected boolean canGrowInto(BlockState p_230334_1_) {
        return p_230334_1_.is(Blocks.WATER);
    }

    protected Block getBodyBlock() {
        return PanthalassaBlocks.KRETHROSS_PLANT.get();
    }

    protected boolean canAttachToBlock(Block p_230333_1_) {
        return p_230333_1_ != Blocks.MAGMA_BLOCK;
    }

    public boolean canPlaceLiquid(IBlockReader p_204510_1_, BlockPos p_204510_2_, BlockState p_204510_3_, Fluid p_204510_4_) {
        return false;
    }

    public boolean placeLiquid(IWorld p_204509_1_, BlockPos p_204509_2_, BlockState p_204509_3_, FluidState p_204509_4_) {
        return false;
    }

    protected int getBlocksToGrowWhenBonemealed(Random p_230332_1_) {
        return 1;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        FluidState fluidstate = p_196258_1_.getLevel().getFluidState(p_196258_1_.getClickedPos());
        return fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8 ? super.getStateForPlacement(p_196258_1_) : null;
    }

    public FluidState getFluidState(BlockState p_204507_1_) {
        return Fluids.WATER.getSource(false);
    }
}