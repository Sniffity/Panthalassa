package com.github.sniffity.panthalassa.server.world.gen.structure;

import com.github.sniffity.panthalassa.server.registry.PanthalassaStructures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

public class StructurePanthalassaObservatory extends Structure {

    public static final Codec<StructurePanthalassaObservatory> CODEC = RecordCodecBuilder.<StructurePanthalassaObservatory>mapCodec(instance -> instance.group(
            StructurePanthalassaLaboratory.settingsCodec(instance),
            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
            ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
            Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
            HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
            Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
            Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)
    ).apply(instance, StructurePanthalassaObservatory::new)).codec();

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int size;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;


    public StructurePanthalassaObservatory(Structure.StructureSettings config,
                                          Holder<StructureTemplatePool> startPool,
                                          Optional<ResourceLocation> startJigsawName,
                                          int size,
                                          HeightProvider startHeight,
                                          Optional<Heightmap.Types> projectStartToHeightmap,
                                          int maxDistanceFromCenter)
    {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    protected static int determineYHeight(Structure.GenerationContext context) {
        //Start at a mid Y point of Panthalassa, to ensure no structures near the ceiling...
        //All Panthalassa Observatories will be below this limit
        int start = 70;
        BlockPos centerOfChunk = context.chunkPos().getWorldPosition();
        //Grab a column of blocks, based off of the centerOfChunk where we're trying to place the structure.
        NoiseColumn columnOfBlocks = context.chunkGenerator().getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor(),context.randomState());
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

    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {

        int yHeight = StructurePanthalassaObservatory.determineYHeight(context);
        if (yHeight==100) {
            return Optional.empty();
        }

        ChunkPos chunkPos = context.chunkPos();
        BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), yHeight, chunkPos.getMinBlockZ());

        Optional<Structure.GenerationStub> structurePiecesGenerator =
                JigsawPlacement.addPieces(
                        context, // Used for JigsawPlacement to get all the proper behaviors done.
                        this.startPool, // The starting pool to use to create the structure layout from
                        this.startJigsawName, // Can be used to only spawn from one Jigsaw block. But we don't need to worry about this.
                        this.size, // How deep a branch of pieces can go away from center piece. (5 means branches cannot be longer than 5 pieces from center piece)
                        blockPos, // Where to spawn the structure.
                        false, // "useExpansionHack" This is for legacy villages to generate properly. You should keep this false always.
                        this.projectStartToHeightmap, // Adds the terrain height's y value to the passed in blockpos's y value. (This uses WORLD_SURFACE_WG heightmap which stops at top water too)
                        // Here, blockpos's y value is 60 which means the structure spawn 60 blocks above terrain height.
                        // Set this to false for structure to be place only at the passed in blockpos's Y value instead.
                        // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                        this.maxDistanceFromCenter); // Maximum limit for how far pieces can spawn from center. You cannot set this bigger than 128 or else pieces gets cutoff.
        return structurePiecesGenerator;
    }


    @Override
    public StructureType<?> type() {
        return PanthalassaStructures.PANTHALASSA_OBSERVATORY.get();
    }
}