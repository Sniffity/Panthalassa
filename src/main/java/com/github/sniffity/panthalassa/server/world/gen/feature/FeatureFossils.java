package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import java.util.Random;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.FossilFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.apache.commons.lang3.mutable.MutableInt;

public class FeatureFossils extends Feature<FossilFeatureConfiguration> {
    public FeatureFossils(Codec<FossilFeatureConfiguration> p_65851_) {
        super(p_65851_);
    }

    public boolean place(FeaturePlaceContext<FossilFeatureConfiguration> p_159789_) {
        Random rand = p_159789_.random();
        WorldGenLevel worldgenlevel = p_159789_.level();
        BlockPos p_241855_4_ = p_159789_.origin();
        FossilFeatureConfiguration fossilfeatureconfiguration = p_159789_.config();

        double r = Math.floor(Math.random()*(81)+20);
        BlockPos blockpos0 = new BlockPos(p_241855_4_.getX(), r, p_241855_4_.getZ());

        Rotation rotation = Rotation.getRandom(rand);
        int i = rand.nextInt(fossilfeatureconfiguration.fossilStructures.size());
        StructureManager templatemanager = worldgenlevel.getLevel().getServer().getStructureManager();
        StructureTemplate structuretemplate = templatemanager.getOrCreate(fossilfeatureconfiguration.fossilStructures.get(i));
        StructureTemplate structuretemplate1 = templatemanager.getOrCreate(fossilfeatureconfiguration.overlayStructures.get(i));
        ChunkPos chunkpos = new ChunkPos(blockpos0);
        BoundingBox mutableboundingbox = new BoundingBox(chunkpos.getMinBlockX(), 0, chunkpos.getMinBlockZ(), chunkpos.getMaxBlockX(), 256, chunkpos.getMaxBlockZ());
        StructurePlaceSettings placementsettings = (new StructurePlaceSettings()).setRotation(rotation).setBoundingBox(mutableboundingbox).setRandom(rand).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        Vec3i vec3i = structuretemplate.getSize(rotation);
        BlockPos blockpos = blockpos0.offset(-vec3i.getX() / 2, 0, -vec3i.getZ() / 2);
        int j = rand.nextInt(16 - blockpos.getX());

        for(int k = 0; k < vec3i.getX(); ++k) {
            for(int l = 0; l < vec3i.getZ(); ++l) {
                j = Math.min(j, worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, blockpos.getX() + k, blockpos.getZ() + l));
            }
        }
        int i1 = Math.max(j - 15 - rand.nextInt(10), worldgenlevel.getMinBuildHeight() + 10);
        BlockPos blockpos1 = structuretemplate.getZeroPositionWithTransform(blockpos.atY(i1), Mirror.NONE, rotation);

        if ((worldgenlevel.getBlockState(blockpos1.below()).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            return false;
        }
        if (!(worldgenlevel.getBlockState(blockpos1).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            return false;
        }
        if ((worldgenlevel.getBlockState(blockpos1.below().north()).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            return false;
        }
        if ((worldgenlevel.getBlockState(blockpos1.below().south()).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            return false;
        }
        if ((worldgenlevel.getBlockState(blockpos1.below().east()).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            return false;
        }
        if ((worldgenlevel.getBlockState(blockpos1.below().west()).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            return false;
        }

        BlockPos blockpos2 = new BlockPos(blockpos1.getX(), blockpos1.getY()-2, blockpos1.getZ());

        BlockRotProcessor integrityprocessor = new BlockRotProcessor(0.9F);
        placementsettings.clearProcessors().addProcessor(integrityprocessor);
        structuretemplate1.placeInWorld(worldgenlevel, blockpos2, blockpos2, placementsettings, rand, 4);
        placementsettings.popProcessor(integrityprocessor);

        return true;
    }

    private static int countEmptyCorners(WorldGenLevel p_159782_, BoundingBox p_159783_) {
        MutableInt mutableint = new MutableInt(0);
        p_159783_.forAllCorners((p_159787_) -> {
            BlockState blockstate = p_159782_.getBlockState(p_159787_);
            if (blockstate.isAir() || blockstate.is(Blocks.LAVA) || blockstate.is(Blocks.WATER)) {
                mutableint.add(1);
            }

        });
        return mutableint.getValue();
    }
}