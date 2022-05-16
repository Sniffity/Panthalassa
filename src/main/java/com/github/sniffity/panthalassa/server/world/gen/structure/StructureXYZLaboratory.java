package com.github.sniffity.panthalassa.server.world.gen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.Optional;

public class StructureXYZLaboratory extends StructureFeature<JigsawConfiguration> {

    public StructureXYZLaboratory() {
        super(JigsawConfiguration.CODEC, StructureXYZLaboratory::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    protected static int determineYHeight(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        //Start at the mid Y point of Panthalassa, no structures near the ceiling...
        int start = 64;
        BlockPos centerOfChunk = context.chunkPos().getWorldPosition();
        NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor());
        BlockState topBlock = columnOfBlocks.getBlock(start);
        //Move down until it finds water...
        int j = 0;
        boolean flag = false;
        while (!topBlock.getFluidState().is(FluidTags.WATER)) {
            j++;
            //Adjusting the topBlock accordingly...
            topBlock = columnOfBlocks.getBlock(centerOfChunk.above(start - j).getY());
            //If we go too far down, stop...
            if (start-j < 0) {
                flag = true;
                break;
            }
        }
        //We've found water, now keep moving down until we find solid ground again....
        int i = 0;
        int k = 0;
        while (!topBlock.canOcclude()) {
            i++;
            k++;
            topBlock = columnOfBlocks.getBlock(centerOfChunk.above(start - j - i).getY());
            //If we go too far down, stop...
            if (start-j-i < 0) {
                flag = true;
                break;
            }
        }
        //We've found solid ground with a column of water above it.
        // If the column of water is large enough and we did not go too far down, proceed to generate the structure...
        if (k>10 && !flag) {
            return (centerOfChunk.above(start - j - i).getY());
        }
        //Else, return a value that will fail the next check.
        return 100;
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {

        int yHeight = StructureXYZLaboratory.determineYHeight(context);
        if (yHeight!=100) {
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