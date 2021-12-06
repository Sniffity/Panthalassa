package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.*;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleAGII;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleMRSV;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Panthalassa.MODID);

    public static final RegistryObject<EntityType<EntityKronosaurus>> KRONOSAURUS = ENTITY_TYPES.register ("kronosaurus",()->
            EntityType.Builder.of(EntityKronosaurus::new,EntityClassification.CREATURE)
                    .sized(1.0F, 1.0F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "kronosaurus").toString()));

    public static final RegistryObject<EntityType<EntityMegalodon>> MEGALODON = ENTITY_TYPES.register ("megalodon",()->
            EntityType.Builder.of(EntityMegalodon::new,EntityClassification.CREATURE)
                    .sized(1.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "megalodon").toString()));

    public static final RegistryObject<EntityType<EntityArchelon>> ARCHELON = ENTITY_TYPES.register ("archelon",()->
            EntityType.Builder.of(EntityArchelon::new,EntityClassification.CREATURE)
                    .sized(1.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "archelon").toString()));

    public static final RegistryObject<EntityType<EntityMosasaurus>> MOSASAURUS = ENTITY_TYPES.register ("mosasaurus",()->
            EntityType.Builder.of(EntityMosasaurus::new,EntityClassification.CREATURE)
                    .sized(1.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "mosasaurus").toString()));

    public static final RegistryObject<EntityType<EntityCoelacanth>> COELACANTH = ENTITY_TYPES.register ("coelacanth",()->
            EntityType.Builder.of(EntityCoelacanth::new,EntityClassification.CREATURE)
                    .sized(1.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "coelacanth").toString()));

    public static final RegistryObject<EntityType<EntityDunkleosteus>> DUNKLEOSTEUS = ENTITY_TYPES.register ("dunkleosteus",()->
            EntityType.Builder.of(EntityDunkleosteus::new,EntityClassification.CREATURE)
                    .sized(1.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "dunkleosteus").toString()));

    public static final RegistryObject<EntityType<EntityLeedsichthys>> LEEDSICHTHYS = ENTITY_TYPES.register ("leedsichthys",()->
            EntityType.Builder.of(EntityLeedsichthys::new,EntityClassification.CREATURE)
                    .sized(1.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "leedsichthys").toString()));

    public static final RegistryObject<EntityType<VehicleMRSV>> MRSV = ENTITY_TYPES.register ("manta_ray_submersible_vehicle",()->
            EntityType.Builder
                    .<VehicleMRSV>of(VehicleMRSV::new,EntityClassification.MISC)
                    .sized(2.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "manta_ray_submersible_vehicle").toString()));


    public static final RegistryObject<EntityType<VehicleAGII>> AGII = ENTITY_TYPES.register ("abyss_glider_2_submersible_vehicle",()->
            EntityType.Builder
                    .<VehicleAGII>of(VehicleAGII::new,EntityClassification.MISC)
                    .sized(2.0F, 2.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "abyss_glider_2_submersible_vehicle").toString()));

    public static void spawnPlacements() {

        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES.isOpaque = (state) -> {
            return (state.getMaterial().blocksMotion() || !state.getFluidState().isEmpty()) && !(state.getBlock() == PanthalassaBlocks.PANTHALASSA_WATER.get());
        };

        EntitySpawnPlacementRegistry.register(KRONOSAURUS.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PanthalassaEntity::canPanthalassaEntitySpawn);
        EntitySpawnPlacementRegistry.register(MOSASAURUS.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PanthalassaEntity::canPanthalassaEntitySpawn);
        EntitySpawnPlacementRegistry.register(COELACANTH.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PanthalassaEntity::canPanthalassaEntitySpawn);
        EntitySpawnPlacementRegistry.register(MEGALODON.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PanthalassaEntity::canPanthalassaEntitySpawn);
        EntitySpawnPlacementRegistry.register(ARCHELON.get(), EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, PanthalassaEntity::canPanthalassaEntitySpawn);

    }

}