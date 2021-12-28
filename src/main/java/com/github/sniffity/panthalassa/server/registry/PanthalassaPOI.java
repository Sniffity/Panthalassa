package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PanthalassaPOI {

    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, Panthalassa.MODID);

    public static final RegistryObject<PoiType> PANTHALASSA_POI_PORTAL = POI.register("panthalassa_poi_portal",
            () -> new PoiType("panthalassa_poi_portal", PoiType.getBlockStates(PanthalassaBlocks.PORTAL.get()), 0, 1));
}