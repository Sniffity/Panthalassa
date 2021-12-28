package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.SurfaceRuleData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/*
public class PanthalassaSurfaceBuilders {
    public static void registerSurfaceRules() {
        Registry.register(Registry.RULE, new ResourceLocation(Panthalassa.MODID, "primeval_expanse"), SurfaceRuleData.CODEC)
    }
    public static final DeferredRegister<SurfaceRules<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, Panthalassa.MODID);

    public static final RegistryObject<SurfaceBuilder<SurfaceBuilderBaseConfiguration>> PRIMEVAL_EXPANSE = SURFACE_BUILDERS.register("primeval_expanse",
            () -> new PrimevalExpanse(SurfaceBuilderBaseConfiguration.CODEC));

    public static final RegistryObject<SurfaceBuilder<SurfaceBuilderBaseConfiguration>> ABYSSAL_OVERGROWTH = SURFACE_BUILDERS.register("abyssal_overgrowth",
            () -> new AbyssalOvergrowth(SurfaceBuilderBaseConfiguration.CODEC));

    public static final RegistryObject<SurfaceBuilder<SurfaceBuilderBaseConfiguration>> PLACEHOLDER_PLACEHOLDER = SURFACE_BUILDERS.register("ancient_caverns",
            () -> new AncientCaverns(SurfaceBuilderBaseConfiguration.CODEC));
}

 */