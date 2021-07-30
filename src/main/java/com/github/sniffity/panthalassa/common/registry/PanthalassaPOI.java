package com.github.sniffity.panthalassa.common.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaPOI {

    public static final DeferredRegister<PointOfInterestType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, Panthalassa.MODID);

    public static final RegistryObject<PointOfInterestType> PANTHALASSA_PORTAL = POI.register("panthalassa_portal",
            () -> new PointOfInterestType("panthalassa_portal", PointOfInterestType.getAllStates(PanthalassaBlocks.PORTAL.get()), 0, 1));
}

