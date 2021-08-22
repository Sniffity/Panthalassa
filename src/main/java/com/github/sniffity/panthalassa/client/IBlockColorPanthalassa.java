package com.github.sniffity.panthalassa.client;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

import javax.annotation.Nullable;

public interface IBlockColorPanthalassa {
    int getColor(BlockState p_getColor_1_, @Nullable IBlockDisplayReader p_getColor_2_, @Nullable BlockPos p_getColor_3_, int p_getColor_4_);
}