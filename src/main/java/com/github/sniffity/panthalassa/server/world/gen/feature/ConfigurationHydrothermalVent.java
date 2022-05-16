package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class ConfigurationHydrothermalVent implements FeatureConfiguration {
    public static final Codec<ConfigurationHydrothermalVent> CODEC = RecordCodecBuilder.create((p_159816_) -> {
        return p_159816_.group(ResourceLocation.CODEC.listOf().fieldOf("hydrothermal_structures").forGetter((p_159830_) -> {
            return p_159830_.hydrothermalVentStructures;
        })).apply(p_159816_, ConfigurationHydrothermalVent::new);
    });
    public final List<ResourceLocation> hydrothermalVentStructures;


    public ConfigurationHydrothermalVent(List<ResourceLocation> resourceLocations) {
        if (resourceLocations.isEmpty()) {
            throw new IllegalArgumentException("Hydrothermal structures list needs at least one entry");
        }
        else {
            this.hydrothermalVentStructures = resourceLocations;
        }
    }
}