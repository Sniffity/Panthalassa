package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.block.BlockPrimordialStalk;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class FeaturePrimordialStalks extends Feature<NoneFeatureConfiguration> {
    public FeaturePrimordialStalks(Codec<NoneFeatureConfiguration> p_i231936_1_) {
        super(p_i231936_1_);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159446_) {
        BlockPos pos = p_159446_.origin();
        WorldGenLevel worldgenlevel = p_159446_.level();
        Random rand = p_159446_.random();

        double r = Math.floor(Math.random()*(41)+20);
        BlockPos blockpos = new BlockPos(pos.getX(), r, pos.getZ());;
        if (worldgenlevel.isWaterAt(blockpos) && worldgenlevel.getBlockState(blockpos.below()).is(PanthalassaBlocks.PANTHALASSA_SAND.get())) {

            BlockPrimordialStalk.generatePlant(worldgenlevel, blockpos, rand, 8);
            return true;
        } else {
            return false;
        }
    }
}