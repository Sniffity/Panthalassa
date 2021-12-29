package com.github.sniffity.panthalassa.server.world.dimension;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Panthalassa Mod - Class: PanthalassaBiomeSource <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed implementing the same methods the Undergarden uses to generate
 * random seeds.
 */

public class PanthalassaBiomeSource extends MultiNoiseBiomeSource {

        public static final MapCodec<PanthalassaBiomeSource> DIRECT_CODEC =
                RecordCodecBuilder.mapCodec((p_187070_) -> {
            return p_187070_.group(ExtraCodecs.<Pair<Climate.ParameterPoint, Supplier<Biome>>>nonEmptyList(RecordCodecBuilder.<Pair<Climate.ParameterPoint, Supplier<Biome>>>create((p_187078_) -> {
                return p_187078_.group(Climate.ParameterPoint.CODEC.fieldOf("parameters").forGetter(Pair::getFirst), Biome.CODEC.fieldOf("biome").forGetter(Pair::getSecond)).apply(p_187078_, Pair::of);
            }).listOf()).xmap(Climate.ParameterList::new, (Function<Climate.ParameterList<Supplier<Biome>>, List<Pair<Climate.ParameterPoint, Supplier<Biome>>>>) Climate.ParameterList::values).fieldOf("biomes").forGetter((p_187080_) -> {
                return p_187080_.parameters;
            })).apply(p_187070_, p_187070_.stable(PanthalassaBiomeSource::new));
        });

        public final Climate.ParameterList<Supplier<Biome>> parameters;
        public final Optional<net.minecraft.world.level.biome.MultiNoiseBiomeSource.PresetInstance> preset;

        private PanthalassaBiomeSource(Climate.ParameterList<Supplier<Biome>> p_187057_) {
            this(p_187057_, Optional.empty());
        }

    PanthalassaBiomeSource(Climate.ParameterList<Supplier<Biome>> p_187059_, Optional<net.minecraft.world.level.biome.MultiNoiseBiomeSource.PresetInstance> p_187060_) {
            super(p_187059_);
            this.preset = p_187060_;
            this.parameters = p_187059_;
        }

        protected Codec<? extends BiomeSource> codec() {
            return CODEC;
        }

        public BiomeSource withSeed(long p_48466_) {
            return this;
        }

}