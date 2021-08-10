package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.gen.feature.FeaturePanthalassaKelp;
import com.github.sniffity.panthalassa.server.world.gen.feature.FeaturePanthalassaRocks;
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
}
