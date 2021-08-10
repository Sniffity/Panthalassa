package com.github.sniffity.panthalassa.server.world.gen.structure;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.NetherFossilStructures;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class StructurePanthalassaFossil extends Structure<NoFeatureConfig> {
    public StructurePanthalassaFossil(Codec<NoFeatureConfig> p_i232105_1_) {
        super(p_i232105_1_);
    }

    public Structure.IStartFactory<NoFeatureConfig> getStartFactory() {
        return net.minecraft.world.gen.feature.structure.NetherFossilStructure.Start::new;
    }

    public static class Start extends MarginedStructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> p_i232106_1_, int p_i232106_2_, int p_i232106_3_, MutableBoundingBox p_i232106_4_, int p_i232106_5_, long p_i232106_6_) {
            super(p_i232106_1_, p_i232106_2_, p_i232106_3_, p_i232106_4_, p_i232106_5_, p_i232106_6_);
        }

        public void generatePieces(DynamicRegistries p_230364_1_, ChunkGenerator p_230364_2_, TemplateManager p_230364_3_, int p_230364_4_, int p_230364_5_, Biome p_230364_6_, NoFeatureConfig p_230364_7_) {
            ChunkPos chunkpos = new ChunkPos(p_230364_4_, p_230364_5_);
            int i = chunkpos.getMinBlockX() + this.random.nextInt(16);
            int j = chunkpos.getMinBlockZ() + this.random.nextInt(16);
            int k = p_230364_2_.getSeaLevel();
            int l = k + this.random.nextInt(p_230364_2_.getGenDepth() - 2 - k);
            IBlockReader iblockreader = p_230364_2_.getBaseColumn(i, j);

            for(BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(i, l, j); l > k; --l) {
                BlockState blockstate = iblockreader.getBlockState(blockpos$mutable);
                blockpos$mutable.move(Direction.DOWN);
                BlockState blockstate1 = iblockreader.getBlockState(blockpos$mutable);
                if (blockstate.is(Blocks.WATER) &&
                        (((blockstate1.is(PanthalassaBlocks.PANTHALASSA_COARSE_SOIL.get()))|| (blockstate1.is(PanthalassaBlocks.PANTHALASSA_SOIL.get())) || (blockstate1.is(PanthalassaBlocks.PANTHALASSA_LOOSE_SOIL.get()))))
                        || blockstate1.isFaceSturdy(iblockreader, blockpos$mutable, Direction.UP)) {
                    break;
                }
            }

            if (l > k) {
                NetherFossilStructures.addPieces(p_230364_3_, this.pieces, this.random, new BlockPos(i, l, j));
                this.calculateBoundingBox();
            }
        }
    }
}