package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import javax.annotation.Nullable;

public class BlockPressureEqualizer extends Block implements EntityBlock {

    public BlockPressureEqualizer() {
        super(Properties.of(
                Material.METAL,
                MaterialColor.DIAMOND)
                .strength(3.0F, 3.0F)
                .sound(SoundType.GLASS)
                .lightLevel((state) -> 15));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos p_153098_, BlockState p_153099_) {
        return new BlockPressureEqualizerBlockEntity(p_153098_, p_153099_);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : (level0, pos, state0, blockEntity) -> ((BlockPressureEqualizerBlockEntity) blockEntity).tick(level,pos);
    }

    public boolean isPathfindable(BlockState p_52106_, BlockGetter p_52107_, BlockPos p_52108_, PathComputationType p_52109_) {
        return false;
    }
}