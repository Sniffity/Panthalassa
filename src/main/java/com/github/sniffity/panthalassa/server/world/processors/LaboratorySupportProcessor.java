package com.github.sniffity.panthalassa.server.world.processors;

import com.github.sniffity.panthalassa.server.registry.PanthalassaProcessors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.material.Fluid;

public class LaboratorySupportProcessor extends StructureProcessor {

    public static final Codec<LaboratorySupportProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            RegistryOps.retrieveRegistry(Registry.PROCESSOR_LIST_REGISTRY).forGetter((processor) -> processor.processorListRegistry),
            BlockState.CODEC.fieldOf("start_block").forGetter((processor) -> processor.startBlockState),
            BlockState.CODEC.fieldOf("support_block").forGetter((processor) -> processor.supportBlockState))
            .apply(instance, instance.stable(LaboratorySupportProcessor::new)));

    public final Registry<StructureProcessorList> processorListRegistry;
    public final BlockState startBlockState;
    public final BlockState supportBlockState;

    private LaboratorySupportProcessor(Registry<StructureProcessorList> processorListRegistry,
                                       BlockState startBlock,
                                       BlockState supportBlock) {
        this.processorListRegistry = processorListRegistry;
        this.startBlockState = startBlock;
        this.supportBlockState = supportBlock;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos templateOffset, BlockPos worldOffset, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        //Grab a BlockState...
        BlockState blockState = structureBlockInfoWorld.state;
        //If the BlockState matches one of the start blocks, where we want to build a support, initialize the processBlock procedure...
        if (blockState == startBlockState) {
            //Get the current position....
            BlockPos worldPos = structureBlockInfoWorld.pos;
            //Get the block we will be carrying out replacements with....
            BlockState replacementState = supportBlockState;
            //Define a mutableBlockPos which we will move to replace the blocks in a downwards column...
            BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos().set(worldPos);
            //Replace the initial block, where we are starting...
            levelReader.getChunk(currentPos).setBlockState(currentPos, replacementState, false);
            //Move down, we are ready to start the loop...
            BlockState currentBlock = levelReader.getBlockState(currentPos.below());
            //Replace blocks with the replacementBlock until we hit a solid block...
            while(currentBlock.getFluidState().is(FluidTags.WATER) ) {
                StructureTemplate.StructureBlockInfo pillarState = new StructureTemplate.StructureBlockInfo(currentPos.immutable(), replacementState, null);
                levelReader.getChunk(currentPos).setBlockState(currentPos, pillarState.state, false);
                currentPos.move(Direction.DOWN);
                currentBlock = levelReader.getBlockState(currentPos);
                if (currentPos.getY()<0) {
                    break;
                }
            }
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return PanthalassaProcessors.LABORATORY_SUPPORT_PROCESSOR;
    }
}