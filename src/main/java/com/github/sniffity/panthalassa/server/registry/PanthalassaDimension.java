package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.PanthalassaWorldSavedData;
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

    public static void worldTick(TickEvent.LevelTickEvent event){
        if(event.phase == TickEvent.Phase.END && !event.level.isClientSide()){
            PanthalassaWorldSavedData.tick((ServerLevel) event.level);
        }
    }
}
