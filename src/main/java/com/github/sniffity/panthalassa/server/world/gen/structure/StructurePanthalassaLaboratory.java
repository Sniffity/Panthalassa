package com.github.sniffity.panthalassa.server.world.gen.structure;

import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import java.util.Optional;

public class StructurePanthalassaLaboratory extends StructureFeature<JigsawConfiguration> {

    public StructurePanthalassaLaboratory() {
        super(JigsawConfiguration.CODEC, StructurePanthalassaLaboratory::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    protected static int determineYHeight(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        BlockPos centerOfChunk = context.chunkPos().getWorldPosition();
        int landHeight = context.chunkGenerator().getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
        NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor());
        BlockState topBlock = columnOfBlocks.getBlock(landHeight);
        if (topBlock.getFluidState().is(FluidTags.WATER)) {
            int i = 0;
            while (!topBlock.canOcclude() && centerOfChunk.above(landHeight - i).getY() > 0) {
                i++;
                topBlock = columnOfBlocks.getBlock(centerOfChunk.above(landHeight - i).getY());
            }
            float yHeight = (centerOfChunk.above(landHeight - i).getY());
            return (int) yHeight;
        }
        return 100;
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {

        int yHeight = StructurePanthalassaLaboratory.determineYHeight(context);
        if (!(yHeight<=42)) {
            return Optional.empty();
        }

        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);
        BlockPos blockpos1 = new BlockPos(blockpos.getX(),yHeight,blockpos.getZ());

        Optional<PieceGenerator<JigsawConfiguration>> structurePiecesGenerator =
                JigsawPlacement.addPieces(
                        context,
                        PoolElementStructurePiece::new,
                        blockpos1,
                        false,
                        false
                );
        return structurePiecesGenerator;
    }
}