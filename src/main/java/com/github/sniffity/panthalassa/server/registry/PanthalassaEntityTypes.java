package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.*;
import com.github.sniffity.panthalassa.server.entity.display.DisplayGiantOrthoconeShell;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleAGII;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleMRSV;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PanthalassaEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Panthalassa.MODID);

    public static final RegistryObject<EntityType<EntityKronosaurus>> KRONOSAURUS = ENTITY_TYPES.register ("kronosaurus",()->
            EntityType.Builder.of(EntityKronosaurus::new,MobCategory.MONSTER)
                    .sized(1.0F, 1.0F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "kronosaurus").toString()));

    public static final RegistryObject<EntityType<EntityMegalodon>> MEGALODON = ENTITY_TYPES.register ("megalodon",()->
            EntityType.Builder.of(EntityMegalodon::new,MobCategory.MONSTER)
                    .sized(1.0F, 1.0F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "megalodon").toString()));

    public static final RegistryObject<EntityType<EntityArchelon>> ARCHELON = ENTITY_TYPES.register ("archelon",()->
            EntityType.Builder.of(EntityArchelon::new,MobCategory.MONSTER)
                    .sized(2.0F, 1.0F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "archelon").toString()));

    public static final RegistryObject<EntityType<EntityMosasaurus>> MOSASAURUS = ENTITY_TYPES.register ("mosasaurus",()->
            EntityType.Builder.of(EntityMosasaurus::new,MobCategory.MONSTER)
                    .sized(1.0F, 1.0F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "mosasaurus").toString()));

    public static final RegistryObject<EntityType<EntityCoelacanth>> COELACANTH = ENTITY_TYPES.register ("coelacanth",()->
            EntityType.Builder.of(EntityCoelacanth::new,MobCategory.MONSTER)
                    .sized(1.0F, 1.0F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "coelacanth").toString()));

    public static final RegistryObject<EntityType<EntityDunkleosteus>> DUNKLEOSTEUS = ENTITY_TYPES.register ("dunkleosteus",()->
            EntityType.Builder.of(EntityDunkleosteus::new,MobCategory.MONSTER)
                    .sized(1.5F, 1.3F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "dunkleosteus").toString()));

    public static final RegistryObject<EntityType<EntityLeedsichthys>> LEEDSICHTHYS = ENTITY_TYPES.register ("leedsichthys",()->
            EntityType.Builder.of(EntityLeedsichthys::new,MobCategory.MONSTER)
                    .sized(1.8F, 1.2F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "leedsichthys").toString()));

    public static final RegistryObject<EntityType<EntityGiantOrthocone>> GIANT_ORTHOCONE = ENTITY_TYPES.register ("giant_orthocone",()->
            EntityType.Builder.of(EntityGiantOrthocone::new,MobCategory.MONSTER)
                    .sized(1.8F, 1.2F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "giant_orthocone").toString()));

    public static final RegistryObject<EntityType<EntityBasilosaurus>> BASILOSAURUS = ENTITY_TYPES.register ("basilosaurus",()->
            EntityType.Builder.of(EntityBasilosaurus::new,MobCategory.MONSTER)
                    .sized(1.8F, 1.2F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "basilosaurus").toString()));

    public static final RegistryObject<EntityType<VehicleMRSV>> MRSV = ENTITY_TYPES.register ("manta_ray_submersible_vehicle",()->
            EntityType.Builder
                    .<VehicleMRSV>of(VehicleMRSV::new,MobCategory.MISC)
                    .sized(2.0F, 1.5F)
                    .build(new ResourceLocation(Panthalassa.MODID, "manta_ray_submersible_vehicle").toString()));

    public static final RegistryObject<EntityType<VehicleAGII>> AGII = ENTITY_TYPES.register ("abyss_glider_2_submersible_vehicle",()->
            EntityType.Builder
                    .<VehicleAGII>of(VehicleAGII::new,MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "abyss_glider_2_submersible_vehicle").toString()));

    public static final RegistryObject<EntityType<DisplayGiantOrthoconeShell>> GIANT_ORTHOCONE_SHELL = ENTITY_TYPES.register ("giant_orthocone_shell",()->
            EntityType.Builder
                    .<DisplayGiantOrthoconeShell>of(DisplayGiantOrthoconeShell::new,MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "giant_orthocone_shell").toString()));
}