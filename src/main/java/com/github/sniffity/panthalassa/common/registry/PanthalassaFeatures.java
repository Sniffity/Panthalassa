package com.github.sniffity.panthalassa.common.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.common.world.gen.feature.PanthalassaKelp;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Panthalassa.MODID);

    public static final RegistryObject<Feature<NoFeatureConfig>> PANTHALASSA_KELP = FEATURES.register("panthalassa_kelp",
            () -> new PanthalassaKelp(NoFeatureConfig.field_236558_a_));

    public static final class ConfiguredFeatures {

        public static final ConfiguredFeature<?, ?> PANTHALASSA_KELP = PanthalassaFeatures.PANTHALASSA_KELP.get().withConfiguration(
                IFeatureConfig.NO_FEATURE_CONFIG
        );

    }

    public static void registerConfiguredFeatures() {
        register("panthalassa_kelp", ConfiguredFeatures.PANTHALASSA_KELP.range(16).square().func_242731_b(100));
    }

    private static <FeatureConfig extends IFeatureConfig> void register(String name, ConfiguredFeature<FeatureConfig, ?> feature) {
        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Panthalassa.MODID, name), feature);
    }
}
