package com.github.sniffity.panthalassa.server.world.gen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.PostPlacementProcessor;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;

import java.util.Optional;

public class StructurePanthalassaObservatory extends StructureFeature<JigsawConfiguration> {

    public StructurePanthalassaObservatory() {
        super(JigsawConfiguration.CODEC, StructurePanthalassaObservatory::createPiecesGenerator, PostPlacementProcessor.NONE);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    protected static int determineYHeight(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        //Start at a mid Y point of Panthalassa, to ensure no structures near the ceiling...
        //All Panthalassa Observatories will be below this limit
        int start = 70;
        BlockPos centerOfChunk = context.chunkPos().getWorldPosition();
        //Grab a column of blocks, based off of the centerOfChunk where we're trying to place the structure.
        NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor());
        //We will proceed to analyze this column, starting at Y = start.
        BlockState topBlock = columnOfBlocks.getBlock(start);
        //Now we will start moving down the column of blocks, from Y = start....
        int j = 0;
        boolean flag = false;
        //We will continue moving down the column until we find water....
        while (!topBlock.getFluidState().is(FluidTags.WATER)) {
            //If we do not find water, we increase the value of j.
            //j is what we are subtracting from our start point, hence we go further down as j gets bigger.
            j++;
            //Adjusting the topBlock, the one we're analyzing, accordingly...
            topBlock = columnOfBlocks.getBlock(start-j);
            //If we go too far down, stop...
            if (start-j < 0) {
                flag = true;
                break;
            }
        }
        //We've found water, now keep moving down until we find solid ground again....
        //Ideally, we will move down a column of water, until we hit solid ground...
        int i = 0;
        while (!topBlock.canOcclude()) {
            i++;
            topBlock = columnOfBlocks.getBlock(start-j-i);
            //If we go too far down, stop...
            if (start-j-i < 0) {
                flag = true;
                break;
            }
        }
        //We've found solid ground with a column of water above it.
        // If the column of water is large enough and we did not go too far down, proceed to generate the structure...
        if (i>10 && !flag) {
            return start-j-i;
        }
        //Else, return a value that will fail the next check.
        return 100;
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {

        int yHeight = StructurePanthalassaObservatory.determineYHeight(context);
        if (yHeight==100) {
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