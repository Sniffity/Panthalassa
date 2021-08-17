package com.github.sniffity.panthalassa.server.registry;
 import com.github.sniffity.panthalassa.Panthalassa;
 import net.minecraft.util.ResourceLocation;
 import net.minecraft.util.registry.Registry;
        import net.minecraft.util.registry.WorldGenRegistries;
        import net.minecraft.world.gen.FlatGenerationSettings;
        import net.minecraft.world.gen.feature.IFeatureConfig;
        import net.minecraft.world.gen.feature.StructureFeature;

public class PanthalassaConfiguredStructures {

    public static StructureFeature<?, ?> CONFIGURED_PANTHALASSA_LABORATORY = PanthalassaStructures.PANTHALASSA_LABORATORY.get().configured(IFeatureConfig.NONE);


    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(Panthalassa.MODID, "configured_panthalassa_laboratory"), CONFIGURED_PANTHALASSA_LABORATORY);


        FlatGenerationSettings.STRUCTURE_FEATURES.put(PanthalassaStructures.PANTHALASSA_LABORATORY.get(), CONFIGURED_PANTHALASSA_LABORATORY);
    }
}