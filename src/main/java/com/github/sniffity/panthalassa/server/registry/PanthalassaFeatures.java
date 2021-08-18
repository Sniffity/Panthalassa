package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.gen.feature.*;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Panthalassa.MODID);

    public static final RegistryObject<Feature<NoFeatureConfig>> PRIMEVAL_EXPANSE_KELP =
            FEATURES.register("primeval_expanse_kelp", () -> new FeaturePrimevalExpanseKelp(NoFeatureConfig.CODEC));

    public static final RegistryObject<Feature<NoFeatureConfig>> ABYSSAL_OVERGROWTH_KELP =
            FEATURES.register("abyssal_overgrowth_kelp", () -> new FeatureAbyssalOvergrowthKelp(NoFeatureConfig.CODEC));

    public static final RegistryObject<Feature<ProbabilityConfig>> PANTHALASSA_SEA_GRASS =
            FEATURES.register("panthalassa_sea_grass", () -> new FeaturePanthalassaSeaGrass(ProbabilityConfig.CODEC));

    public static final RegistryObject<Feature<NoFeatureConfig>> PRIMORDIAL_STALKS =
            FEATURES.register("primordial_stalks", () -> new FeaturePrimordialStalks(NoFeatureConfig.CODEC));

    public static final RegistryObject<Feature<NoFeatureConfig>> PANTHALASSA_CORAL_CLAW =
            FEATURES.register("panthalassa_coral_claw", () -> new FeatureCoralClaw(NoFeatureConfig.CODEC));

    public static final RegistryObject<Feature<NoFeatureConfig>> PANTHALASSA_CORAL_MUSHROOM =
            FEATURES.register("panthalassa_coral_mushroom", () -> new FeatureCoralMushroom(NoFeatureConfig.CODEC));

    public static final RegistryObject<Feature<NoFeatureConfig>> PANTHALASSA_CORAL_TREE =
            FEATURES.register("panthalassa_coral_tree", () -> new FeatureCoralTree(NoFeatureConfig.CODEC));

    public static final RegistryObject<Feature<NoFeatureConfig>> ANCIENT_PILLARS =
            FEATURES.register("ancient_pillar", () -> new FeatureAncientPillar(NoFeatureConfig.CODEC));

    public static final RegistryObject<Feature<ColumnConfig>> ANCIENT_COLUMNS =
            FEATURES.register("ancient_column", () -> new FeatureAncientColumn(ColumnConfig.CODEC));

    public static final RegistryObject<Feature<NoFeatureConfig>> FOSSILS =
            FEATURES.register("fossils", () -> new FeatureFossils(NoFeatureConfig.CODEC));

    public static final RegistryObject<Feature<BlockStateFeatureConfig>> PANTHALASSA_ROCKS =
            FEATURES.register("panthalassa_rocks", () -> new FeaturePanthalassaRocks(BlockStateFeatureConfig.CODEC));
}