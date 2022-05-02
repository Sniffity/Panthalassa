package com.github.sniffity.panthalassa.server.world.dimension;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
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

public class PanthalassaChunkGenerator extends NoiseBasedChunkGenerator {

    public static final Codec<PanthalassaChunkGenerator> CODEC = RecordCodecBuilder.create((p_188643_) -> {
        return p_188643_.group(RegistryLookupCodec.create(Registry.NOISE_REGISTRY).forGetter((p_188716_) -> {
            return p_188716_.noises;
        }), BiomeSource.CODEC.fieldOf("biome_source").forGetter((p_188711_) -> {
            return p_188711_.biomeSource;
        }), Codec.LONG.fieldOf("seed")
                .orElseGet(SeedBearer::provideSeed)
                .forGetter((p_188690_) -> { return p_188690_.seed;
        }), NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter((p_188652_) -> {
            return p_188652_.settings;
        })).apply(p_188643_, p_188643_.stable(PanthalassaChunkGenerator::new));
    });

    public PanthalassaChunkGenerator(Registry<NormalNoise.NoiseParameters> p_188609_, BiomeSource p_188610_, long p_188611_, Supplier<NoiseGeneratorSettings> p_188612_) {
        super(p_188609_, p_188610_, p_188610_, p_188611_, p_188612_);
    }


    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ChunkGenerator withSeed(long p_64374_) {
        return new PanthalassaChunkGenerator(this.noises, this.biomeSource.withSeed(p_64374_), p_64374_, this.settings);
    }
}