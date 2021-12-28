package com.github.sniffity.panthalassa.server.world.gen.structure;

import com.github.sniffity.panthalassa.Panthalassa;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.apache.logging.log4j.Level;

public class StructurePanthalassaLaboratory extends Structure<NoFeatureConfig> {

    int i;

    public StructurePanthalassaLaboratory(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public  IStartFactory<NoFeatureConfig> getStartFactory() {
        return StructurePanthalassaLaboratory.Start::new;
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeProvider biomeSource, long seed, SharedSeedRandom chunkRandom, int chunkX, int chunkZ, Biome biome, ChunkPos chunkPos, NoFeatureConfig featureConfig) {
        BlockPos centerOfChunk = new BlockPos(chunkX * 16, 0, chunkZ * 16);
        int landHeight = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
        IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());
        BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));
        if (topBlock.getFluidState().is(FluidTags.WATER)) {
            while (!topBlock.getBlockState().canOcclude()) {
                i++;
                topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight-i));
            }
            float yHeight = (centerOfChunk.above(landHeight-i).getY());
            return yHeight <= 42;
        }
        return false;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        int j;

        @Override
        public void generatePieces(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {

            BlockPos centerOfChunk = new BlockPos(chunkX * 16, 0, chunkZ * 16);
            int landHeight = chunkGenerator.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG);
            IBlockReader columnOfBlocks = chunkGenerator.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ());
            BlockState topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight));
            BlockPos centerPos;
            if (topBlock.getFluidState().is(FluidTags.WATER)) {
                while (!topBlock.getBlockState().canOcclude()) {
                    j++;
                    topBlock = columnOfBlocks.getBlockState(centerOfChunk.above(landHeight - j));
                }
                float yHeight = (centerOfChunk.above(landHeight - j).getY());


                int x = chunkX * 16;
                int z = chunkZ * 16;
                centerPos = new BlockPos(x, yHeight, z);

                if (!(yHeight<= 42)) {
                    return;
                }

            } else {
                return;
            }

            JigsawManager.addPieces(
                    dynamicRegistryManager,
                    new VillageConfig(() -> dynamicRegistryManager.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)

                            .get(new ResourceLocation(Panthalassa.MODID, "panthalassa_laboratory/start_pool")),
                            10),
                    AbstractVillagePiece::new,
                    chunkGenerator,
                    templateManagerIn,
                    centerPos,
                    this.pieces,
                    this.random,
                    false,
                    false);

            Vector3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
            int xOffset = centerPos.getX() - structureCenter.getX();
            int zOffset = centerPos.getZ() - structureCenter.getZ();
            for(StructurePiece structurePiece : this.pieces){
                structurePiece.move(xOffset, 0, zOffset);
            }

            this.calculateBoundingBox();
        }
    }
}