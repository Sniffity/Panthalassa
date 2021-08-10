package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;

public class PanthalassaConfiguredFeatures {

    public static final ConfiguredFeature<?, ?> PANTHALASSA_KELP_CONFIGURED = PanthalassaFeatures.PANTHALASSA_KELP
            .get().configured(IFeatureConfig.NONE)
            .decorated(Features.Placements.TOP_SOLID_HEIGHTMAP)
            .squared()
            .decorated(Placement.COUNT_NOISE_BIASED
                    .configured(new TopSolidWithNoiseConfig(80, 80.0D, 0.0D)));

    public static void registerConfiguredFeatures() {
        Registry<ConfiguredFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_FEATURE;

        Registry.register(registry, new ResourceLocation(Panthalassa.MODID, "panthalassa_kelp_configured"), PANTHALASSA_KELP_CONFIGURED);
    }
}

