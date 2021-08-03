package com.github.sniffity.panthalassa.common.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.common.entity.EntityKronosaurus;
import com.github.sniffity.panthalassa.vehicle.PanthalassaVehicle2;
import com.github.sniffity.panthalassa.vehicle.VehicleMSRV;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Panthalassa.MODID);

    public static final RegistryObject<EntityType<EntityKronosaurus>> KRONOSAURUS = ENTITY_TYPES.register ("kronosaurus",()->
            EntityType.Builder.create(EntityKronosaurus::new,EntityClassification.CREATURE)
                    .size(2.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "kronosaurus").toString()));

    public static final RegistryObject<EntityType<VehicleMSRV>> MSRV = ENTITY_TYPES.register ("manta_ray_submersible_vehicle",()->
            EntityType.Builder.create(VehicleMSRV::new,EntityClassification.CREATURE)
                    .size(2.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "manta_ray_submersible_vehicle").toString()));


    @SuppressWarnings("deprecated")
    public static void setupEntityTypeAttributes (){
        GlobalEntityTypeAttributes.put(KRONOSAURUS.get(), EntityKronosaurus.kronosaurusAttributes().create());
    }
}