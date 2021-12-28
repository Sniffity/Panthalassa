package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

public class PanthalassaConfiguredStructures {

    public static ConfiguredStructureFeature<?, ?> CONFIGURED_PANTHALASSA_LABORATORY = PanthalassaStructures.PANTHALASSA_LABORATORY.get()
            .configured(new JigsawConfiguration(() -> PlainVillagePools.START, 0));


    public static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(Panthalassa.MODID, "configured_panthalassa_laboratory"), CONFIGURED_PANTHALASSA_LABORATORY);

    }
}