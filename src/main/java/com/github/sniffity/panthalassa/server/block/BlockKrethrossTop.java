package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;
import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockKrethrossTop extends GrowingPlantHeadBlock implements LiquidBlockContainer {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);

    public BlockKrethrossTop(BlockBehaviour.Properties p_i48781_1_) {
        super(p_i48781_1_, Direction.UP, SHAPE, true, 0.14D);
    }

    protected boolean canGrowInto(BlockState blockState) {
        return blockState.is(Blocks.WATER);
    }

    protected Block getBodyBlock() {
        return PanthalassaBlocks.KRETHROSS_PLANT.get();
    }

    protected boolean canAttachToBlock(Block p_230333_1_) {
        return p_230333_1_ != Blocks.MAGMA_BLOCK;
    }

    public boolean canPlaceLiquid(BlockGetter p_204510_1_, BlockPos p_204510_2_, BlockState p_204510_3_, Fluid p_204510_4_) {
        return false;
    }

    public boolean placeLiquid(LevelAccessor p_204509_1_, BlockPos p_204509_2_, BlockState p_204509_3_, FluidState p_204509_4_) {
        return false;
    }

    protected int getBlocksToGrowWhenBonemealed(Random p_230332_1_) {
        return 1;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
        FluidState fluidstate = p_196258_1_.getLevel().getFluidState(p_196258_1_.getClickedPos());
        return fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8 ? super.getStateForPlacement(p_196258_1_) : null;
    }

    public FluidState getFluidState(BlockState p_204507_1_) {
        return Fluids.WATER.getSource(false);
    }
}