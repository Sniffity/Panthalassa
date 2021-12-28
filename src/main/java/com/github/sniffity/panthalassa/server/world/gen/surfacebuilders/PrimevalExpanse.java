package com.github.sniffity.panthalassa.server.world.gen.surfacebuilders;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;

/*
public class PrimevalExpanse extends NetherCappedSurfaceBuilder {
    private static final BlockState CEILING_BLOCK_1 = PanthalassaBlocks.PANTHALASSA_STONE.get().defaultBlockState();
    private static final BlockState CEILING_BLOCK_2 = PanthalassaBlocks.PANTHALASSA_COARSE_STONE.get().defaultBlockState();
    private static final BlockState CEILING_BLOCK_3 = PanthalassaBlocks.PANTHALASSA_LOOSE_STONE.get().defaultBlockState();
    private static final BlockState FLOOR_BLOCK_1 = PanthalassaBlocks.PANTHALASSA_SOIL.get().defaultBlockState();
    private static final BlockState FLOOR_BLOCK_2 = PanthalassaBlocks.PANTHALASSA_COARSE_SOIL.get().defaultBlockState();
    private static final BlockState FLOOR_BLOCK_3 = PanthalassaBlocks.PANTHALASSA_LOOSE_SOIL.get().defaultBlockState();

    private static final BlockState PATCH = PanthalassaBlocks.PANTHALASSA_STONE.get().defaultBlockState();

    private static final ImmutableList<BlockState> FLOOR_BLOCK_STATES = ImmutableList.of(FLOOR_BLOCK_1, FLOOR_BLOCK_2, FLOOR_BLOCK_3);
    private static final ImmutableList<BlockState> CEILING_BLOCK_STATES = ImmutableList.of(CEILING_BLOCK_1, CEILING_BLOCK_2, CEILING_BLOCK_3);

    public PrimevalExpanse(Codec<SurfaceBuilderBaseConfiguration> p_i232123_1_) {
        super(p_i232123_1_);
    }

    protected ImmutableList<BlockState> getFloorBlockStates() {
        return FLOOR_BLOCK_STATES;
    }

    protected ImmutableList<BlockState> getCeilingBlockStates() {
        return CEILING_BLOCK_STATES;
    }

    protected BlockState getPatchBlockState() {
        return PATCH;
    }
}
*/