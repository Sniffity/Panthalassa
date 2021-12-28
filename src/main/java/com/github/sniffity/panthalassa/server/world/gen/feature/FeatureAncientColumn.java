package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;

import javax.annotation.Nullable;
import java.util.Random;

public class FeatureAncientColumn extends Feature<ColumnFeatureConfiguration> {

    public FeatureAncientColumn(Codec<ColumnFeatureConfiguration> p_i231925_1_) {
        super(p_i231925_1_);
    }

    public boolean place(FeaturePlaceContext<ColumnFeatureConfiguration> p_159446_) {
        BlockPos p_241855_4_ = p_159446_.origin();
        WorldGenLevel worldgenlevel = p_159446_.level();
        Random rand = p_159446_.random();
        ColumnFeatureConfiguration columnfeatureconfiguration = p_159446_.config();


        double r = Math.floor(Math.random()*(81)+20);
        BlockPos blockpos1 = new BlockPos(p_241855_4_.getX(), r, p_241855_4_.getZ());

        int i = worldgenlevel.getSeaLevel();
        if (!canPlaceAt(worldgenlevel, i, blockpos1.mutable())) {
            return false;
        } else {
            int j = columnfeatureconfiguration.height().sample(rand);
            boolean flag = rand.nextFloat() < 0.9F;
            int k = Math.min(j, flag ? 5 : 8);
            int l = flag ? 50 : 15;
            boolean flag1 = false;

            for(BlockPos blockpos : BlockPos.randomBetweenClosed(rand, l, blockpos1.getX() - k, blockpos1.getY(), blockpos1.getZ() - k, blockpos1.getX() + k, blockpos1.getY(), blockpos1.getZ() + k)) {
                int i1 = j - blockpos.distManhattan(blockpos1);
                if (i1 >= 0) {
                    flag1 |= this.placeColumn(worldgenlevel, i, blockpos1, i1, columnfeatureconfiguration.reach().sample(rand));
                }
            }

            return flag1;
        }
    }

    private boolean placeColumn(LevelAccessor p_236248_1_, int p_236248_2_, BlockPos p_236248_3_, int p_236248_4_, int p_236248_5_) {
        boolean flag = false;

        for(BlockPos blockpos : BlockPos.betweenClosed(p_236248_3_.getX() - p_236248_5_, p_236248_3_.getY(), p_236248_3_.getZ() - p_236248_5_, p_236248_3_.getX() + p_236248_5_, p_236248_3_.getY(), p_236248_3_.getZ() + p_236248_5_)) {
            int i = blockpos.distManhattan(p_236248_3_);
            BlockPos blockpos1 = isWater(p_236248_1_, p_236248_2_, blockpos) ? findSurface(p_236248_1_, p_236248_2_, blockpos.mutable(), i) : findWater(p_236248_1_, blockpos.mutable(), i);
            if (blockpos1 != null) {
                int j = p_236248_4_ - i / 2;

                for(BlockPos.MutableBlockPos blockpos$mutable = blockpos1.mutable(); j >= 0; --j) {
                    if (isWater(p_236248_1_, p_236248_2_, blockpos$mutable)) {
                        this.setBlock(p_236248_1_, blockpos$mutable, Blocks.BASALT.defaultBlockState());
                        blockpos$mutable.move(Direction.UP);
                        flag = true;
                    } else {
                        if (!p_236248_1_.getBlockState(blockpos$mutable).is(Blocks.BASALT)) {
                            break;
                        }

                        blockpos$mutable.move(Direction.UP);
                    }
                }
            }
        }

        return flag;
    }

    @Nullable
    private static BlockPos findSurface(LevelAccessor p_236246_0_, int p_236246_1_, BlockPos.MutableBlockPos p_236246_2_, int p_236246_3_) {
        while(p_236246_2_.getY() > 1 && p_236246_3_ > 0) {
            --p_236246_3_;
            if (canPlaceAt(p_236246_0_, p_236246_1_, p_236246_2_)) {
                return p_236246_2_;
            }

            p_236246_2_.move(Direction.DOWN);
        }

        return null;
    }

    private static boolean canPlaceAt(LevelAccessor p_242762_0_, int p_242762_1_, BlockPos.MutableBlockPos p_242762_2_) {
        if (!isWater(p_242762_0_, p_242762_1_, p_242762_2_)) {
            return false;
        } else {
            BlockState blockState = p_242762_0_.getBlockState(p_242762_2_.move(Direction.DOWN));
            p_242762_2_.move(Direction.UP);
            return !blockState.is(PanthalassaBlocks.PANTHALASSA_WATER.get());
        }
    }

    @Nullable
    private static BlockPos findWater(LevelAccessor p_236249_0_, BlockPos.MutableBlockPos p_236249_1_, int p_236249_2_) {
        while(p_236249_1_.getY() < p_236249_0_.getMaxBuildHeight() && p_236249_2_ > 0) {
            --p_236249_2_;
            BlockState blockstate = p_236249_0_.getBlockState(p_236249_1_);
            if (blockstate.is(PanthalassaBlocks.PANTHALASSA_WATER.get())) {
                return p_236249_1_;
            }

            p_236249_1_.move(Direction.UP);
        }

        return null;
    }

    private static boolean isWater(LevelAccessor p_236247_0_, int p_236247_1_, BlockPos p_236247_2_) {
        BlockState blockstate = p_236247_0_.getBlockState(p_236247_2_);
        return blockstate.is(PanthalassaBlocks.PANTHALASSA_WATER.get());
    }
}