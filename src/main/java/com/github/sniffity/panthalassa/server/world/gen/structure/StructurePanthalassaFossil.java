package com.github.sniffity.panthalassa.server.world.gen.structure;
/*
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BeardedStructureStart;
import net.minecraft.world.level.levelgen.structure.NetherFossilPieces;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class StructurePanthalassaFossil extends StructureFeature<NoneFeatureConfiguration> {
    public StructurePanthalassaFossil(Codec<NoneFeatureConfiguration> p_i232105_1_) {
        super(p_i232105_1_);
    }

    public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return net.minecraft.world.level.levelgen.structure.NetherFossilFeature.FeatureStart::new;
    }

    public static class Start extends BeardedStructureStart<NoneFeatureConfiguration> {
        public Start(StructureFeature<NoneFeatureConfiguration> p_i232106_1_, int p_i232106_2_, int p_i232106_3_, BoundingBox p_i232106_4_, int p_i232106_5_, long p_i232106_6_) {
            super(p_i232106_1_, p_i232106_2_, p_i232106_3_, p_i232106_4_, p_i232106_5_, p_i232106_6_);
        }

        public void generatePieces(RegistryAccess p_230364_1_, ChunkGenerator p_230364_2_, StructureManager p_230364_3_, int p_230364_4_, int p_230364_5_, Biome p_230364_6_, NoneFeatureConfiguration p_230364_7_) {
            ChunkPos chunkpos = new ChunkPos(p_230364_4_, p_230364_5_);
            int i = chunkpos.getMinBlockX() + this.random.nextInt(16);
            int j = chunkpos.getMinBlockZ() + this.random.nextInt(16);
            int k = p_230364_2_.getSeaLevel();
            int l = k + this.random.nextInt(p_230364_2_.getGenDepth() - 2 - k);
            BlockGetter iblockreader = p_230364_2_.getBaseColumn(i, j);

            for(BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos(i, l, j); l > k; --l) {
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
                NetherFossilPieces.addPieces(p_230364_3_, this.pieces, this.random, new BlockPos(i, l, j));
                this.calculateBoundingBox();
            }
        }
    }
}

 */