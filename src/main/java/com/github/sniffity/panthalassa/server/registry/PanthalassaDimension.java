package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.PanthalassaWorldSavedData;
import com.github.sniffity.panthalassa.server.world.dimension.PanthalassaBiomeSource;
import com.github.sniffity.panthalassa.server.world.dimension.PanthalassaChunkGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;

public class PanthalassaDimension {
    public static final ResourceKey<DimensionType> PANTHALASSA_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(Panthalassa.MODID, "panthalassa"));
    public static final ResourceKey<Level> PANTHALASSA = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(Panthalassa.MODID, "panthalassa"));

    public static void worldTick(TickEvent.WorldTickEvent event){
        if(event.phase == TickEvent.Phase.END && !event.world.isClientSide()){
            PanthalassaWorldSavedData.tick((ServerLevel) event.world);
        }
    }


    private static ResourceLocation name(String name) {
        return new ResourceLocation(Panthalassa.MODID, name);
    }

    public static void registerDimensionAccessories() {
        Registry.register(Registry.CHUNK_GENERATOR, name("chunk_generator"), PanthalassaChunkGenerator.CODEC);
        Registry.register(Registry.BIOME_SOURCE, name("biome_provider"), PanthalassaBiomeSource.CODEC);
    }
}
