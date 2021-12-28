package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.*;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleAGII;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleMRSV;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PanthalassaEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Panthalassa.MODID);

    public static final RegistryObject<EntityType<EntityKronosaurus>> KRONOSAURUS = ENTITY_TYPES.register ("kronosaurus",()->
            EntityType.Builder.of(EntityKronosaurus::new,MobCategory.CREATURE)
                    .sized(1.0F, 1.0F)
                    .canSpawnFarFromPlayer()
                    .build(new ResourceLocation(Panthalassa.MODID, "kronosaurus").toString()));

    public static final RegistryObject<EntityType<EntityMegalodon>> MEGALODON = ENTITY_TYPES.register ("megalodon",()->
            EntityType.Builder.of(EntityMegalodon::new,MobCategory.CREATURE)
                    .sized(1.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "megalodon").toString()));

    public static final RegistryObject<EntityType<EntityArchelon>> ARCHELON = ENTITY_TYPES.register ("archelon",()->
            EntityType.Builder.of(EntityArchelon::new,MobCategory.CREATURE)
                    .sized(1.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "archelon").toString()));

    public static final RegistryObject<EntityType<EntityMosasaurus>> MOSASAURUS = ENTITY_TYPES.register ("mosasaurus",()->
            EntityType.Builder.of(EntityMosasaurus::new,MobCategory.CREATURE)
                    .sized(1.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "mosasaurus").toString()));

    public static final RegistryObject<EntityType<EntityCoelacanth>> COELACANTH = ENTITY_TYPES.register ("coelacanth",()->
            EntityType.Builder.of(EntityCoelacanth::new,MobCategory.CREATURE)
                    .sized(1.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "coelacanth").toString()));

    public static final RegistryObject<EntityType<EntityDunkleosteus>> DUNKLEOSTEUS = ENTITY_TYPES.register ("dunkleosteus",()->
            EntityType.Builder.of(EntityDunkleosteus::new,MobCategory.CREATURE)
                    .sized(1.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "dunkleosteus").toString()));

    public static final RegistryObject<EntityType<EntityLeedsichthys>> LEEDSICHTHYS = ENTITY_TYPES.register ("leedsichthys",()->
            EntityType.Builder.of(EntityLeedsichthys::new,MobCategory.CREATURE)
                    .sized(1.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "leedsichthys").toString()));

    public static final RegistryObject<EntityType<VehicleMRSV>> MRSV = ENTITY_TYPES.register ("manta_ray_submersible_vehicle",()->
            EntityType.Builder
                    .<VehicleMRSV>of(VehicleMRSV::new,MobCategory.MISC)
                    .sized(2.0F, 1.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "manta_ray_submersible_vehicle").toString()));


    public static final RegistryObject<EntityType<VehicleAGII>> AGII = ENTITY_TYPES.register ("abyss_glider_2_submersible_vehicle",()->
            EntityType.Builder
                    .<VehicleAGII>of(VehicleAGII::new,MobCategory.MISC)
                    .sized(2.0F, 2.0F)
                    .build(new ResourceLocation(Panthalassa.MODID, "abyss_glider_2_submersible_vehicle").toString()));

    public static void spawnPlacements() {

        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES.isOpaque = (state) -> {
            return (state.getMaterial().blocksMotion() || !state.getFluidState().isEmpty()) && !(state.getBlock() == PanthalassaBlocks.PANTHALASSA_WATER.get());
        };

        SpawnPlacements.register(KRONOSAURUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(MOSASAURUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(COELACANTH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(MEGALODON.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(ARCHELON.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(LEEDSICHTHYS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(DUNKLEOSTEUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PanthalassaEntity::canPanthalassaEntitySpawn);

    }

}