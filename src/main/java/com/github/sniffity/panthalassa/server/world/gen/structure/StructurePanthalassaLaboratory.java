package com.github.sniffity.panthalassa.server.world.gen.structure;

import com.github.sniffity.panthalassa.Panthalassa;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import org.apache.logging.log4j.Level;

import java.util.Optional;

public class StructurePanthalassaLaboratory extends StructureFeature<JigsawConfiguration> {

    public StructurePanthalassaLaboratory(Codec<JigsawConfiguration> codec) {
        super(codec, StructurePanthalassaLaboratory::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    protected static int isFeatureChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        BlockPos centerOfChunk = context.chunkPos().getWorldPosition();
        int landHeight = context.chunkGenerator().getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
        NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor());
        BlockState topBlock = columnOfBlocks.getBlock(landHeight);
        BlockPos centerPos;
        if (topBlock.getFluidState().is(FluidTags.WATER)) {
            int i = 0;
            while (!topBlock.canOcclude()) {
                i++;
                topBlock = columnOfBlocks.getBlock(centerOfChunk.above(landHeight - i).getY());
            }
            float yHeight = (centerOfChunk.above(landHeight - i).getY());

            int x = centerOfChunk.getX() * 16;
            int z = centerOfChunk.getZ() * 16;
            centerPos = new BlockPos(x, yHeight, z);


            return (int) yHeight;
        }
        return 100;
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {

        int yHeight = StructurePanthalassaLaboratory.isFeatureChunk(context);
        if (!(yHeight<=42)) {
            return Optional.empty();
        }


        JigsawConfiguration newConfig = new JigsawConfiguration(
                () -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                        .get(new ResourceLocation(Panthalassa.MODID, "panthalassa_laboratory/start_pool")),
                10
        );

        PieceGeneratorSupplier.Context<JigsawConfiguration> newContext = new PieceGeneratorSupplier.Context<>(
                context.chunkGenerator(),
                context.biomeSource(),
                context.seed(),
                context.chunkPos(),
                newConfig,
                context.heightAccessor(),
                context.validBiome(),
                context.structureManager(),
                context.registryAccess()
        );

        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);
        BlockPos blockpos1 = new BlockPos(blockpos.getX(),yHeight,blockpos.getZ());

        Optional<PieceGenerator<JigsawConfiguration>> structurePiecesGenerator =
                JigsawPlacement.addPieces(
                        newContext,
                        PoolElementStructurePiece::new,
                        blockpos1,
                        false,
                        false
                );

        return structurePiecesGenerator;
    }
}