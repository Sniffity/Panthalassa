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

    protected static boolean isFeatureChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        BlockPos centerOfChunk = context.chunkPos().getWorldPosition();
        int landHeight = context.chunkGenerator().getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
        NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor());
        BlockState topBlock = columnOfBlocks.getBlock(landHeight);
        if (topBlock.getFluidState().is(FluidTags.WATER)) {
            int i = 0;
            while (!topBlock.canOcclude()) {
                i++;
                topBlock = columnOfBlocks.getBlock(centerOfChunk.above(landHeight - i).getY());
            }
            float yHeight = (centerOfChunk.above(landHeight - i).getY());
            return yHeight <= 42;
        }
        return false;
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {

        if (!StructurePanthalassaLaboratory.isFeatureChunk(context)) {
            return Optional.empty();
        }

        JigsawConfiguration newConfig = new JigsawConfiguration(
                () -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                        .get(new ResourceLocation(Panthalassa.MODID, "run_down_house/start_pool")),
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

        Optional<PieceGenerator<JigsawConfiguration>> structurePiecesGenerator =
                JigsawPlacement.addPieces(
                        newContext,
                        PoolElementStructurePiece::new,
                        blockpos,
                        false,
                        true
                );


        if(structurePiecesGenerator.isPresent()) {
            Panthalassa.LOGGER.log(Level.DEBUG, "Laboratory located at " + blockpos);
        }

        return structurePiecesGenerator;
    }
}