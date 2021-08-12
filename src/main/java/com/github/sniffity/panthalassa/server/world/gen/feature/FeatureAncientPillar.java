package com.github.sniffity.panthalassa.server.world.gen.feature;


import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class FeatureAncientPillar extends Feature<NoFeatureConfig> {
    public FeatureAncientPillar(Codec<NoFeatureConfig> p_i231926_1_) {
        super(p_i231926_1_);
    }

    public boolean place(ISeedReader p_241855_1_, ChunkGenerator p_241855_2_, Random p_241855_3_, BlockPos p_241855_4_, NoFeatureConfig p_241855_5_) {
        double r = Math.floor(Math.random()*(81)+20);
        BlockPos blockpos = new BlockPos(p_241855_4_.getX(), r, p_241855_4_.getZ());

        if (p_241855_1_.isWaterAt(blockpos) && !p_241855_1_.isWaterAt(blockpos.above())) {
            BlockPos.Mutable blockpos$mutable = blockpos.mutable();
            BlockPos.Mutable blockpos$mutable1 = blockpos.mutable();
            boolean flag = true;
            boolean flag1 = true;
            boolean flag2 = true;
            boolean flag3 = true;


            while(p_241855_1_.isWaterAt(blockpos$mutable)) {
                if (World.isOutsideBuildHeight(blockpos$mutable)) {
                    return true;
                }

                p_241855_1_.setBlock(blockpos$mutable, Blocks.BASALT.defaultBlockState(), 2);
                flag = flag && this.placeHangOff(p_241855_1_, p_241855_3_, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.NORTH));
                flag1 = flag1 && this.placeHangOff(p_241855_1_, p_241855_3_, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.SOUTH));
                flag2 = flag2 && this.placeHangOff(p_241855_1_, p_241855_3_, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.WEST));
                flag3 = flag3 && this.placeHangOff(p_241855_1_, p_241855_3_, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.EAST));
                blockpos$mutable.move(Direction.DOWN);
            }

            blockpos$mutable.move(Direction.UP);
            this.placeBaseHangOff(p_241855_1_, p_241855_3_, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.NORTH));
            this.placeBaseHangOff(p_241855_1_, p_241855_3_, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.SOUTH));
            this.placeBaseHangOff(p_241855_1_, p_241855_3_, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.WEST));
            this.placeBaseHangOff(p_241855_1_, p_241855_3_, blockpos$mutable1.setWithOffset(blockpos$mutable, Direction.EAST));
            blockpos$mutable.move(Direction.DOWN);
            BlockPos.Mutable blockpos$mutable2 = new BlockPos.Mutable();

            for(int i = -3; i < 4; ++i) {
                for(int j = -3; j < 4; ++j) {
                    int k = MathHelper.abs(i) * MathHelper.abs(j);
                    if (p_241855_3_.nextInt(10) < 10 - k) {
                        blockpos$mutable2.set(blockpos$mutable.offset(i, 0, j));
                        int l = 3;

                        while(p_241855_1_.isWaterAt(blockpos$mutable1.setWithOffset(blockpos$mutable2, Direction.DOWN))) {
                            blockpos$mutable2.move(Direction.DOWN);
                            --l;
                            if (l <= 0) {
                                break;
                            }
                        }

                        if (!p_241855_1_.isWaterAt(blockpos$mutable1.setWithOffset(blockpos$mutable2, Direction.DOWN))) {
                            p_241855_1_.setBlock(blockpos$mutable2, Blocks.BASALT.defaultBlockState(), 2);
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private void placeBaseHangOff(IWorld p_236252_1_, Random p_236252_2_, BlockPos p_236252_3_) {
        if (p_236252_2_.nextBoolean()) {
            p_236252_1_.setBlock(p_236252_3_, Blocks.BASALT.defaultBlockState(), 2);
        }

    }

    private boolean placeHangOff(IWorld p_236253_1_, Random p_236253_2_, BlockPos p_236253_3_) {
        if (p_236253_2_.nextInt(10) != 0) {
            p_236253_1_.setBlock(p_236253_3_, Blocks.BASALT.defaultBlockState(), 2);
            return true;
        } else {
            return false;
        }
    }
}