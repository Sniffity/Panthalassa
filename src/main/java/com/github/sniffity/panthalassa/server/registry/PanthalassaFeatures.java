package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.gen.feature.FeaturePanthalassaKelp;
import com.github.sniffity.panthalassa.server.world.gen.feature.FeaturePanthalassaRocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Panthalassa.MODID);

    public static final RegistryObject<Feature<NoFeatureConfig>> PANTHALASSA_KELP =
            FEATURES.register("panthalassa_kelp", () -> new FeaturePanthalassaKelp(NoFeatureConfig.field_236558_a_));


    public static final RegistryObject<Feature<BlockStateFeatureConfig>> PANTHALASSA_ROCKS =
            FEATURES.register("panthalassa_rocks", () -> new FeaturePanthalassaRocks(BlockStateFeatureConfig.field_236455_a_));

    public static final class ConfiguredFeatures {

        public static final ConfiguredFeature<?, ?> PANTHALASSA_KELP = PanthalassaFeatures.PANTHALASSA_KELP.get().withConfiguration(
                IFeatureConfig.NO_FEATURE_CONFIG
        );

        public static final ConfiguredFeature<BlockStateFeatureConfig, ?> PANTHALASSA_ROCKS = PanthalassaFeatures.PANTHALASSA_ROCKS.get().withConfiguration(
                new BlockStateFeatureConfig(
                        PanthalassaBlocks.PANTHALASSA_ROCK.get().getDefaultState()));

    }

        public static void registerConfiguredFeatures() {
            register("panthalassa_kelp", ConfiguredFeatures.PANTHALASSA_KELP
                    .chance(1)
                    .square()
                    .range(128)
                    .func_242731_b(100));

            register("panthalassa_rocks",ConfiguredFeatures.PANTHALASSA_ROCKS
                    .chance(20)
                    .square()
                    .range(128)
                    .func_242731_b(20));
        }

        private static <FeatureConfig extends IFeatureConfig> void register(String name, ConfiguredFeature<FeatureConfig, ?> feature) {
            Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(Panthalassa.MODID, name), feature);
    }
}
