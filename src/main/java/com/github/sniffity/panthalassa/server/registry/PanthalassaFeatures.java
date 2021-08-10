package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.gen.feature.FeaturePanthalassaKelp;
import com.github.sniffity.panthalassa.server.world.gen.feature.FeaturePanthalassaRocks;
import com.github.sniffity.panthalassa.server.world.gen.feature.FeaturePanthalassaSeaGrass;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Panthalassa.MODID);

    public static final RegistryObject<Feature<NoFeatureConfig>> PANTHALASSA_KELP =
            FEATURES.register("panthalassa_kelp", () -> new FeaturePanthalassaKelp(NoFeatureConfig.CODEC));

    public static final RegistryObject<Feature<ProbabilityConfig>> PANTHALASSA_SEA_GRASS =
            FEATURES.register("panthalassa_sea_grass", () -> new FeaturePanthalassaSeaGrass(ProbabilityConfig.CODEC));

    public static final RegistryObject<Feature<BlockStateFeatureConfig>> PANTHALASSA_ROCKS =
            FEATURES.register("panthalassa_rocks", () -> new FeaturePanthalassaRocks(BlockStateFeatureConfig.CODEC));
}
