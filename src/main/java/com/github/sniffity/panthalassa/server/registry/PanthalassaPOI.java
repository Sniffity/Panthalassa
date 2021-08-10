package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaPOI {

    public static final DeferredRegister<PointOfInterestType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, Panthalassa.MODID);

    public static final RegistryObject<PointOfInterestType> PANTHALASSA_POI_PORTAL = POI.register("panthalassa_poi_portal",
            () -> new PointOfInterestType("panthalassa_poi_portal", PointOfInterestType.getBlockStates(PanthalassaBlocks.PORTAL.get()), 0, 1));
}