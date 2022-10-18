package com.github.sniffity.panthalassa.server.world.processors;

import com.github.sniffity.panthalassa.server.registry.PanthalassaProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/**
 * Panthalassa Mod - Class: WaterloggingFixProcessor <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed by TelepathicGrunt, for his RepurposedStructures mod.
 * All credit goes to him.
 */

public class WaterloggingFixProcessor extends StructureProcessor {

    public static final Codec<WaterloggingFixProcessor> CODEC = Codec.unit(WaterloggingFixProcessor::new);

    private WaterloggingFixProcessor() { }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings) {
        if(!infoIn2.state.getFluidState().isEmpty()) {
            if(levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(infoIn2.pos))) {
                return infoIn2;
            }

            ChunkAccess chunk = levelReader.getChunk(infoIn2.pos);
            int minY = chunk.getMinBuildHeight();
            int maxY = chunk.getMaxBuildHeight();
            int currentY = infoIn2.pos.getY();
            if(currentY >= minY && currentY <= maxY) {
                ((LevelAccessor) levelReader).scheduleTick(infoIn2.pos, infoIn2.state.getBlock(), 0);
            }
        }
        return infoIn2;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return PanthalassaProcessors.WATERLOGGING_FIX_PROCESSOR;
    }
}