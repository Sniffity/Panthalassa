package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.block.BlockPrimordialStalk;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class FeaturePrimordialStalks extends Feature<NoFeatureConfig> {
    public FeaturePrimordialStalks(Codec<NoFeatureConfig> p_i231936_1_) {
        super(p_i231936_1_);
    }

    public boolean place(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, NoFeatureConfig noFeatureConfig) {
        double r = Math.floor(Math.random()*(41)+20);
        BlockPos blockpos = new BlockPos(blockPos.getX(), r, blockPos.getZ());;
        if (seedReader.isWaterAt(blockpos) && seedReader.getBlockState(blockpos.below()).is(PanthalassaBlocks.PANTHALASSA_SAND.get())) {

            BlockPrimordialStalk.generatePlant(seedReader, blockpos, random, 8);
            return true;
        } else {
            return false;
        }
    }
}