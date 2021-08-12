package com.github.sniffity.panthalassa.server.world.gen.carver;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.CanyonWorldCarver;
import net.minecraft.world.gen.carver.ICarverConfig;
import net.minecraft.world.gen.carver.UnderwaterCaveWorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import org.apache.commons.lang3.mutable.MutableBoolean;
import java.util.BitSet;
import java.util.Random;

import java.util.function.Function;

public class CarverAncientCanyon extends CanyonWorldCarver {
    public CarverAncientCanyon(Codec<ProbabilityConfig> p_i231919_1_) {
        super(p_i231919_1_);
        this.replaceableBlocks = ImmutableSet.of(
                PanthalassaBlocks.PANTHALASSA_LOOSE_STONE.get(),PanthalassaBlocks.PANTHALASSA_STONE.get(),PanthalassaBlocks.PANTHALASSA_COARSE_STONE.get(),PanthalassaBlocks.PANTHALASSA_ROCK.get());
    }

    protected boolean hasWater(IChunk p_222700_1_, int p_222700_2_, int p_222700_3_, int p_222700_4_, int p_222700_5_, int p_222700_6_, int p_222700_7_, int p_222700_8_, int p_222700_9_) {
        return false;
    }

    protected boolean carveBlock(IChunk p_230358_1_, Function<BlockPos, Biome> p_230358_2_, BitSet p_230358_3_, Random p_230358_4_, BlockPos.Mutable p_230358_5_, BlockPos.Mutable p_230358_6_, BlockPos.Mutable p_230358_7_, int p_230358_8_, int p_230358_9_, int p_230358_10_, int p_230358_11_, int p_230358_12_, int p_230358_13_, int p_230358_14_, int p_230358_15_, MutableBoolean p_230358_16_) {
        return UnderwaterCaveWorldCarver.carveBlock(this, p_230358_1_, p_230358_3_, p_230358_4_, p_230358_5_, p_230358_8_, p_230358_9_, p_230358_10_, p_230358_11_, p_230358_12_, p_230358_13_, p_230358_14_, p_230358_15_);
    }
}