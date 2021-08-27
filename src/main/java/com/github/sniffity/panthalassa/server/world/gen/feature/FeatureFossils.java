package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.mojang.serialization.Codec;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.IntegrityProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class FeatureFossils extends Feature<NoFeatureConfig> {
    private static final ResourceLocation SPINE_1 = new ResourceLocation("fossil/spine_1");
    private static final ResourceLocation SPINE_2 = new ResourceLocation("fossil/spine_2");
    private static final ResourceLocation SPINE_3 = new ResourceLocation("fossil/spine_3");
    private static final ResourceLocation SPINE_4 = new ResourceLocation("fossil/spine_4");
    private static final ResourceLocation SKULL_1 = new ResourceLocation("fossil/skull_1");
    private static final ResourceLocation SKULL_2 = new ResourceLocation("fossil/skull_2");
    private static final ResourceLocation SKULL_3 = new ResourceLocation("fossil/skull_3");
    private static final ResourceLocation SKULL_4 = new ResourceLocation("fossil/skull_4");
    private static final ResourceLocation[] fossils = new ResourceLocation[]{SPINE_1, SPINE_2, SPINE_3, SPINE_4, SKULL_1, SKULL_2, SKULL_3, SKULL_4};

    public FeatureFossils(Codec<NoFeatureConfig> p_i231955_1_) {
        super(p_i231955_1_);
    }

    public boolean place(ISeedReader p_241855_1_, ChunkGenerator p_241855_2_, Random p_241855_3_, BlockPos p_241855_4_, NoFeatureConfig p_241855_5_) {
        double r = Math.floor(Math.random()*(81)+20);
        BlockPos blockpos0 = new BlockPos(p_241855_4_.getX(), r, p_241855_4_.getZ());

        Rotation rotation = Rotation.getRandom(p_241855_3_);
        int i = p_241855_3_.nextInt(fossils.length);
        TemplateManager templatemanager = p_241855_1_.getLevel().getServer().getStructureManager();
        Template template = templatemanager.getOrCreate(fossils[i]);
        ChunkPos chunkpos = new ChunkPos(blockpos0);
        MutableBoundingBox mutableboundingbox = new MutableBoundingBox(chunkpos.getMinBlockX(), 0, chunkpos.getMinBlockZ(), chunkpos.getMaxBlockX(), 256, chunkpos.getMaxBlockZ());
        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setBoundingBox(mutableboundingbox).setRandom(p_241855_3_).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR);
        BlockPos blockpos = template.getSize(rotation);
        int j = p_241855_3_.nextInt(16 - blockpos.getX());
        int k = p_241855_3_.nextInt(16 - blockpos.getZ());

        BlockPos blockpos1 = template.getZeroPositionWithTransform(blockpos0.offset(j, 0, k), Mirror.NONE, rotation);

        if ((p_241855_1_.getBlockState(blockpos1.below()).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            return false;
        }
        if (!(p_241855_1_.getBlockState(blockpos1).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            return false;
        }
        if ((p_241855_1_.getBlockState(blockpos1.below().north()).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            return false;
        }
        if ((p_241855_1_.getBlockState(blockpos1.below().south()).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            return false;
        }
        if ((p_241855_1_.getBlockState(blockpos1.below().east()).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            return false;
        }
        if ((p_241855_1_.getBlockState(blockpos1.below().west()).is(PanthalassaBlocks.PANTHALASSA_WATER.get()))) {
            return false;
        }

        BlockPos blockpos2 = new BlockPos(blockpos1.getX(), blockpos1.getY()-2, blockpos1.getZ());

        IntegrityProcessor integrityprocessor = new IntegrityProcessor(0.9F);
        placementsettings.clearProcessors().addProcessor(integrityprocessor);
        template.placeInWorld(p_241855_1_, blockpos2, blockpos2, placementsettings, p_241855_3_, 4);
        placementsettings.popProcessor(integrityprocessor);

        return true;
    }
}