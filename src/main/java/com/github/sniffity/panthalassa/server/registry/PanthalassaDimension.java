package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.PanthalassaWorldSavedData;
import com.github.sniffity.panthalassa.server.world.dimension.PanthalassaBiomeProvider;
import com.github.sniffity.panthalassa.server.world.dimension.PanthalassaChunkGenerator;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;


public class PanthalassaDimension {
    public static final RegistryKey<DimensionType> PANTHALASSA_TYPE = RegistryKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(Panthalassa.MODID, "panthalassa"));
    public static final RegistryKey<World> PANTHALASSA = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(Panthalassa.MODID, "panthalassa"));

    public static void worldTick(TickEvent.WorldTickEvent event){
        if(event.phase == TickEvent.Phase.END && !event.world.isClientSide()){
            PanthalassaWorldSavedData.tick((ServerWorld) event.world);
        }
    }


    private static ResourceLocation name(String name) {
        return new ResourceLocation(Panthalassa.MODID, name);
    }

    public static void registerDimensionAccessories() {
        Registry.register(Registry.CHUNK_GENERATOR, name("chunk_generator"), PanthalassaChunkGenerator.CODEC);
        Registry.register(Registry.BIOME_SOURCE, name("biome_provider"), PanthalassaBiomeProvider.CODEC);
    }
}
