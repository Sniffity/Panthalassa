package com.github.sniffity.panthalassa.common.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

public class PanthalassaDimension {
    public static final RegistryKey<DimensionType> PANTHALASSA_TYPE = RegistryKey.getOrCreateKey(Registry.DIMENSION_TYPE_KEY, new ResourceLocation(Panthalassa.MODID,"panthalassa"));
    public static final RegistryKey<World> PANTHALASSA = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(Panthalassa.MODID, "panthalassa"));
}
