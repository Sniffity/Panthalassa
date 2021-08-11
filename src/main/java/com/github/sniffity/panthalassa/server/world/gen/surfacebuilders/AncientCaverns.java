package com.github.sniffity.panthalassa.server.world.gen.surfacebuilders;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.ValleySurfaceBuilder;

public class AncientCaverns extends ValleySurfaceBuilder {
    private static final BlockState CEILING_BLOCK_1 = Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA.defaultBlockState();
    private static final BlockState CEILING_BLOCK_2 = Blocks.RED_GLAZED_TERRACOTTA.defaultBlockState();
    private static final BlockState CEILING_BLOCK_3 = Blocks.MAGENTA_GLAZED_TERRACOTTA.defaultBlockState();
    private static final BlockState FLOOR_BLOCK_1 = Blocks.ORANGE_GLAZED_TERRACOTTA.defaultBlockState();
    private static final BlockState FLOOR_BLOCK_2 = Blocks.PINK_GLAZED_TERRACOTTA.defaultBlockState();
    private static final BlockState FLOOR_BLOCK_3 = Blocks.WHITE_GLAZED_TERRACOTTA.defaultBlockState();

    private static final BlockState PATCH = PanthalassaBlocks.PANTHALASSA_SOIL.get().defaultBlockState();

    private static final ImmutableList<BlockState> FLOOR_BLOCK_STATES = ImmutableList.of(FLOOR_BLOCK_1, FLOOR_BLOCK_2, FLOOR_BLOCK_3);
    private static final ImmutableList<BlockState> CEILING_BLOCK_STATES = ImmutableList.of(CEILING_BLOCK_1, CEILING_BLOCK_2, CEILING_BLOCK_3);

    public AncientCaverns(Codec<SurfaceBuilderConfig> p_i232123_1_) {
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

