package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleAG;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleMRSV;
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
            EntityType.Builder.of(EntityKronosaurus::new,EntityClassification.CREATURE)
                    .sized(2.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "kronosaurus").toString()));

    public static final RegistryObject<EntityType<EntityMegalodon>> MEGALODON = ENTITY_TYPES.register ("megalodon",()->
            EntityType.Builder.of(EntityMegalodon::new,EntityClassification.CREATURE)
                    .sized(2.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "megalodon").toString()));


    public static final RegistryObject<EntityType<VehicleMRSV>> MRSV = ENTITY_TYPES.register ("manta_ray_submersible_vehicle",()->
            EntityType.Builder
                    .of(VehicleMRSV::new,EntityClassification.MISC)
                    .sized(2.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "manta_ray_submersible_vehicle").toString()));
    public static final RegistryObject<EntityType<VehicleAG>> AG = ENTITY_TYPES.register ("abyss_glider_vehicle",()->
            EntityType.Builder
                    .of(VehicleAG::new,EntityClassification.MISC)
                    .sized(2.0F, 2.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "abyss_glider_vehicle").toString()));


    @SuppressWarnings("deprecated")
    public static void setupEntityTypeAttributes (){
        GlobalEntityTypeAttributes.put(KRONOSAURUS.get(), EntityKronosaurus.kronosaurusAttributes().build());
        GlobalEntityTypeAttributes.put(MEGALODON.get(), EntityMegalodon.megalodonAttributes().build());

    }
}