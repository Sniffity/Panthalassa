package com.github.sniffity.panthalassa.server.world.gen.feature;


import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FeatureAncientPillar extends Feature<NoneFeatureConfiguration> {
    public FeatureAncientPillar(Codec<NoneFeatureConfiguration> p_i231926_1_) {
        super(p_i231926_1_);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159446_) {
        BlockPos pos = p_159446_.origin();
        WorldGenLevel worldgenlevel = p_159446_.level();
        Random rand = p_159446_.random();

        double r = Math.floor(Math.random()*(81)+20);
        BlockPos blockpos = new BlockPos(pos.getX(), r, pos.getZ());

        if (worldgenlevel.isWaterAt(blockpos) && !worldgenlevel.isWaterAt(blockpos.above())) {
            BlockPos.MutableBlockPos blockpos$mutable = blockpos.mutable();
            BlockPos.MutableBlockPos blockpos$mutable1 = blockpos.mutable();
            boolean flag = true;
            boolean flag1 = true;
            boolean flag2 = true;
            boolean flag3 = true;


            while(worldgenlevel.isWaterAt(blockpos$mutable)) {
                if (worldgenlevel.isOutsideBuildHeight(blockpos$mutable)) {
                    return true;
                }

                worldgenlevel.setBlock(blockpos$mutable, Blocks.BASALT.defaultBlockState(), 2);
                flag = flag && this.placeHangOff(worldgenlevel, rand, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.NORTH));
                flag1 = flag1 && this.placeHangOff(worldgenlevel, rand, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.SOUTH));
                flag2 = flag2 && this.placeHangOff(worldgenlevel, rand, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.WEST));
                flag3 = flag3 && this.placeHangOff(worldgenlevel, rand, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.EAST));
                blockpos$mutable.move(Direction.DOWN);
            }

            blockpos$mutable.move(Direction.UP);
            this.placeBaseHangOff(worldgenlevel, rand, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.NORTH));
            this.placeBaseHangOff(worldgenlevel, rand, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.SOUTH));
            this.placeBaseHangOff(worldgenlevel, rand, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.WEST));
            this.placeBaseHangOff(worldgenlevel, rand, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.EAST));
            blockpos$mutable.move(Direction.DOWN);
            BlockPos.MutableBlockPos blockpos$mutable2 = new BlockPos.MutableBlockPos();

            for(int i = -3; i < 4; ++i) {
                for(int j = -3; j < 4; ++j) {
                    int k = Mth.abs(i) * Mth.abs(j);
                    if (rand.nextInt(10) < 10 - k) {
                        blockpos$mutable2.set(blockpos$mutable.offset(i, 0, j));
                        int l = 3;

                        while(worldgenlevel.isWaterAt(blockpos$mutable1.setWithOffset(blockpos$mutable2, Direction.DOWN))) {
                            blockpos$mutable2.move(Direction.DOWN);
                            --l;
                            if (l <= 0) {
                                break;
                            }
                        }

                        if (!worldgenlevel.isWaterAt(blockpos$mutable1.setWithOffset(blockpos$mutable2, Direction.DOWN))) {
                            worldgenlevel.setBlock(blockpos$mutable2, Blocks.BASALT.defaultBlockState(), 2);
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private void placeBaseHangOff(LevelAccessor p_236252_1_, Random p_236252_2_, BlockPos p_236252_3_) {
        if (p_236252_2_.nextBoolean()) {
            p_236252_1_.setBlock(p_236252_3_, Blocks.BASALT.defaultBlockState(), 2);
        }

    }

    private boolean placeHangOff(LevelAccessor p_236253_1_, Random p_236253_2_, BlockPos p_236253_3_) {
        if (p_236253_2_.nextInt(10) != 0) {
            p_236253_1_.setBlock(p_236253_3_, Blocks.BASALT.defaultBlockState(), 2);
            return true;
        } else {
            return false;
        }
    }
}