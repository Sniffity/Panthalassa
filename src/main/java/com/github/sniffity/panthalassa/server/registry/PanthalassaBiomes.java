package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaBiomes {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Panthalassa.MODID);

    public static final RegistryKey<Biome> PRIMEVAL_EXPANSE = register("primeval_expanse");
    public static final RegistryKey<Biome> ABYSSAL_OVERGROWTH = register("abyssal_overgrowth");
    public static final RegistryKey<Biome> ANCIENT_CAVERNS = register("ancient_caverns");

    private static ResourceLocation name(String name) {
        return new ResourceLocation(Panthalassa.MODID, name);
    }

    private static RegistryKey<Biome> register(String name) {
        BIOMES.register(name, BiomeMaker::theVoidBiome);
        return RegistryKey.create(Registry.BIOME_REGISTRY, name(name));
    }
}