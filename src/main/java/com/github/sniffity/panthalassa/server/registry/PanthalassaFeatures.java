package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.gen.feature.*;
import net.minecraft.world.level.levelgen.feature.FossilFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraftforge.registries.RegistryObject;

public class PanthalassaFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Panthalassa.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PRIMEVAL_EXPANSE_KELP =
            FEATURES.register("primeval_expanse_kelp", () -> new FeaturePrimevalExpanseKelp(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ABYSSAL_OVERGROWTH_KELP =
            FEATURES.register("panthalassa_kelp", () -> new FeaturePanthalassaKelp(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> KRETHROSS =
            FEATURES.register("krethross", () -> new FeatureKrethross(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<ProbabilityFeatureConfiguration>> PANTHALASSA_SEA_GRASS =
            FEATURES.register("panthalassa_sea_grass", () -> new FeaturePanthalassaSeaGrass(ProbabilityFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PRIMORDIAL_STALKS =
            FEATURES.register("primordial_stalks", () -> new FeaturePrimordialStalks(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PANTHALASSA_CORAL_CLAW =
            FEATURES.register("panthalassa_coral_claw", () -> new FeatureCoralClaw(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PANTHALASSA_CORAL_MUSHROOM =
            FEATURES.register("panthalassa_coral_mushroom", () -> new FeatureCoralMushroom(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PANTHALASSA_CORAL_TREE =
            FEATURES.register("panthalassa_coral_tree", () -> new FeatureCoralTree(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ANCIENT_PILLARS =
            FEATURES.register("ancient_pillars", () -> new FeatureAncientPillar(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<FossilFeatureConfiguration>> FOSSILS =
            FEATURES.register("fossils", () -> new FeatureFossils(FossilFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<BlockStateConfiguration>> PANTHALASSA_ROCKS =
            FEATURES.register("panthalassa_rocks", () -> new FeaturePanthalassaRocks(BlockStateConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> PANTHALASSA_ICE_SPIKES =
            FEATURES.register("panthalassa_ice_spikes", () -> new FeaturePanthalassaIceSpikes(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<ProbabilityFeatureConfiguration>> FROSTGRASS =
            FEATURES.register("frostgrass", () -> new FeatureFrostgrass(ProbabilityFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> HYDROTHERMAL_VENTS =
            FEATURES.register("hydrothermal_vents", () -> new FeatureHydrothermalVents(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> ROCK_SPIKES =
            FEATURES.register("rock_spikes", () -> new FeatureRockSpikes(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> DEAD_CORAL_TREE =
            FEATURES.register("dead_coral_tree", () -> new FeatureDeadCoralTree(NoneFeatureConfiguration.CODEC));
}