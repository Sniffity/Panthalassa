package com.github.sniffity.panthalassa.server.world.gen.surfacebuilders;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.ValleySurfaceBuilder;

public class PanthalassaDepths extends ValleySurfaceBuilder {
    private static final BlockState ROOF_BLOCK_1 = PanthalassaBlocks.PANTHALASSA_STONE.get().getDefaultState();
    private static final BlockState ROOF_BLOCK_2 = PanthalassaBlocks.PANTHALASSA_COARSE_STONE.get().getDefaultState();
    private static final BlockState ROOF_BLOCK_3 = PanthalassaBlocks.PANTHALASSA_LOOSE_STONE.get().getDefaultState();
    private static final BlockState FLOOR_BLOCK_1 = PanthalassaBlocks.PANTHALASSA_SOIL.get().getDefaultState();
    private static final BlockState FLOOR_BLOCK_2 = PanthalassaBlocks.PANTHALASSA_COARSE_SOIL.get().getDefaultState();
    private static final BlockState FLOOR_BLOCK_3 = PanthalassaBlocks.PANTHALASSA_LOOSE_SOIL.get().getDefaultState();

    private static final BlockState field_237165_c_ = PanthalassaBlocks.PANTHALASSA_SOIL.get().getDefaultState();

    private static final ImmutableList<BlockState> field_237166_d_ = ImmutableList.of(FLOOR_BLOCK_1, FLOOR_BLOCK_2, FLOOR_BLOCK_3);
    private static final ImmutableList<BlockState> field_237167_e_ = ImmutableList.of(ROOF_BLOCK_1, ROOF_BLOCK_2, ROOF_BLOCK_3);

    public PanthalassaDepths(Codec<SurfaceBuilderConfig> p_i232123_1_) {
        super(p_i232123_1_);
    }

    protected ImmutableList<BlockState> func_230387_a_() {
        return field_237166_d_;
    }

    protected ImmutableList<BlockState> func_230388_b_() {
        return field_237167_e_;
    }

    protected BlockState func_230389_c_() {
        return field_237165_c_;
    }
}