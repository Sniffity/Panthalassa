package com.github.sniffity.panthalassa.server.world.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

/**
 * Panthalassa Mod - Class: PanthalassaChunkGenerator <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed implementing the same methods the Undergarden uses to generate
 * random seeds.
 */

public class PanthalassaChunkGenerator extends NoiseChunkGenerator {

    public static final Codec<PanthalassaChunkGenerator> CODEC = RecordCodecBuilder.create(
            (instance) -> instance.group(
                    BiomeProvider.CODEC.fieldOf("biome_source")
                            .forGetter((chunkGenerator) -> chunkGenerator.biomeSource),
                    Codec.LONG.fieldOf("seed")
                            .orElseGet(SeedBearer::provideSeed)
                            .forGetter((chunkGenerator) -> chunkGenerator.seed),
                    DimensionSettings.CODEC.fieldOf("settings")
                            .forGetter((chunkGenerator) -> chunkGenerator.settings))
                    .apply(instance, instance.stable(PanthalassaChunkGenerator::new)));

    public PanthalassaChunkGenerator(BiomeProvider biomeProvider, long seed, Supplier<DimensionSettings> dimensionSettingsSupplier) {
        super(biomeProvider, seed, dimensionSettingsSupplier);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ChunkGenerator withSeed(long seed) {
        return new PanthalassaChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }
}