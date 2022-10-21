package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.apache.commons.lang3.mutable.MutableInt;

public class FeatureFossils extends Feature<FossilFeatureConfiguration> {
    public FeatureFossils(Codec<FossilFeatureConfiguration> p_65851_) {
        super(p_65851_);
    }

    public boolean place(FeaturePlaceContext<FossilFeatureConfiguration> p_159789_) {
        RandomSource rand = p_159789_.random();
        WorldGenLevel worldgenlevel = p_159789_.level();
        BlockPos p_241855_4_ = p_159789_.origin();
        FossilFeatureConfiguration fossilfeatureconfiguration = p_159789_.config();

        double r = Math.floor(Math.random()*(81)+20);
        BlockPos blockpos0 = new BlockPos(p_241855_4_.getX(), r, p_241855_4_.getZ());

        Rotation rotation = Rotation.getRandom(rand);
        int i = rand.nextInt(fossilfeatureconfiguration.fossilStructures.size());
        StructureTemplateManager templatemanager = worldgenlevel.getLevel().getServer().getStructureManager();
        StructureTemplate structuretemplate = templatemanager.getOrCreate(fossilfeatureconfiguration.fossilStructures.get(i));
        StructureTemplate structuretemplate1 = templatemanager.getOrCreate(fossilfeatureconfiguration.overlayStructures.get(i));
        ChunkPos chunkpos = new ChunkPos(blockpos0);
        BoundingBox boundingbox = new BoundingBox(chunkpos.getMinBlockX() - 16, worldgenlevel.getMinBuildHeight(), chunkpos.getMinBlockZ() - 16, chunkpos.getMaxBlockX() + 16, worldgenlevel.getMaxBuildHeight(), chunkpos.getMaxBlockZ() + 16);
        BoundingBox mutableboundingbox = new BoundingBox(chunkpos.getMinBlockX(), 0, chunkpos.getMinBlockZ(), chunkpos.getMaxBlockX(), 256, chunkpos.getMaxBlockZ());
        StructurePlaceSettings structureplacesettings = (new StructurePlaceSettings()).setRotation(rotation).setBoundingBox(boundingbox).setRandom(rand);
        Vec3i vec3i = structuretemplate.getSize(rotation);
        BlockPos blockpos = blockpos0.offset(-vec3i.getX() / 2, 0, -vec3i.getZ() / 2);
        int j = blockpos0.getY();

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

        BlockPos blockpos2 = structuretemplate.getZeroPositionWithTransform(blockpos1.atY(i1), Mirror.NONE, rotation);

        if (countEmptyCorners(worldgenlevel, structuretemplate.getBoundingBox(structureplacesettings, blockpos2)) > fossilfeatureconfiguration.maxEmptyCornersAllowed) {
            return false;
        } else {
            structureplacesettings.clearProcessors();
            fossilfeatureconfiguration.fossilProcessors.value().list().forEach(structureplacesettings::addProcessor);
            structuretemplate.placeInWorld(worldgenlevel, blockpos2, blockpos2, structureplacesettings, rand, 4);
            structureplacesettings.clearProcessors();
            fossilfeatureconfiguration.overlayProcessors.value().list().forEach(structureplacesettings::addProcessor);
            structuretemplate1.placeInWorld(worldgenlevel, blockpos2, blockpos2, structureplacesettings, rand, 4);
            return true;
        }

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