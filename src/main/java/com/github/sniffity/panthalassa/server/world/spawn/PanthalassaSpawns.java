package com.github.sniffity.panthalassa.server.world.spawn;

import com.github.sniffity.panthalassa.config.PanthalassaCommonConfig;
import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import com.github.sniffity.panthalassa.server.registry.PanthalassaEntityTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.*;

/**
 * Panthalassa Mod - Class: PanthalassaCommonConfig <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Mowzie's Mobs handles
 * their entity spawning and pulling data from their config.
 */

public class PanthalassaSpawns {
    public static void registerSpawnPlacementTypes() {
        SpawnPlacements.register(PanthalassaEntityTypes.KRONOSAURUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.MEGALODON.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.ARCHELON.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.MOSASAURUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.COELACANTH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.DUNKLEOSTEUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.LEEDSICHTHYS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.GIANT_ORTHOCONE.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.BASILOSAURUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.THALASSOMEDON.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.ACROLEPIS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.CERATODUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.HELICOPRION.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.ANGLERFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        SpawnPlacements.register(PanthalassaEntityTypes.ANOMALOCARIS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
    }

    public static void onBiomeLoading(BiomeLoadingEvent event) {
        if (PanthalassaCommonConfig.COMMON.GENERAL.externalSpawningBoolean.get()) {
            ResourceLocation biomeName = event.getName();
            if (biomeName == null)
                return;
            if (PanthalassaCommonConfig.COMMON.ENTITIES.KRONOSAURUS.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.KRONOSAURUS.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.KRONOSAURUS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.KRONOSAURUS.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.MEGALODON.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.MEGALODON.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.MEGALODON.get(), PanthalassaCommonConfig.COMMON.ENTITIES.MEGALODON.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.ARCHELON.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.ARCHELON.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.ARCHELON.get(), PanthalassaCommonConfig.COMMON.ENTITIES.ARCHELON.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.MOSASAURUS.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.MOSASAURUS.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.MOSASAURUS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.MOSASAURUS.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.COELACANTH.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.COELACANTH.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.COELACANTH.get(), PanthalassaCommonConfig.COMMON.ENTITIES.COELACANTH.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.DUNKLEOSTEUS.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.DUNKLEOSTEUS.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.DUNKLEOSTEUS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.DUNKLEOSTEUS.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.LEEDSICHTHYS.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.LEEDSICHTHYS.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.LEEDSICHTHYS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.LEEDSICHTHYS.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.GIANT_ORTHOCONE.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.GIANT_ORTHOCONE.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.GIANT_ORTHOCONE.get(), PanthalassaCommonConfig.COMMON.ENTITIES.GIANT_ORTHOCONE.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.BASILOSAURUS.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.BASILOSAURUS.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.BASILOSAURUS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.BASILOSAURUS.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.THALASSOMEDON.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.THALASSOMEDON.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.THALASSOMEDON.get(), PanthalassaCommonConfig.COMMON.ENTITIES.THALASSOMEDON.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.ACROLEPIS.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.ACROLEPIS.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.ACROLEPIS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.ACROLEPIS.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.CERATODUS.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.CERATODUS.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.CERATODUS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.CERATODUS.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.HELICOPRION.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.HELICOPRION.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.HELICOPRION.get(), PanthalassaCommonConfig.COMMON.ENTITIES.HELICOPRION.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.ANGLERFISH.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.ANGLERFISH.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.ANGLERFISH.get(), PanthalassaCommonConfig.COMMON.ENTITIES.ANGLERFISH.externalSpawning, MobCategory.MONSTER, event);
            }
            if (PanthalassaCommonConfig.COMMON.ENTITIES.ANOMALOCARIS.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.ANOMALOCARIS.externalSpawning.biomeSpawningConfig, biomeName)) {
                registerEntityWorldSpawn(PanthalassaEntityTypes.ANOMALOCARIS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.ANOMALOCARIS.externalSpawning, MobCategory.MONSTER, event);
            }
        }
    }

    public static boolean isBiomeInConfig(PanthalassaCommonConfig.BiomeSpawningConfig biomeConfig, ResourceLocation biomeName) {
        String biomeComboString = biomeConfig.biomeWhitelist.get().toString();
        String[] biomeStringArray = biomeComboString.toLowerCase().replace(" ", "").split(",");
        if (Arrays.asList(biomeStringArray).contains(biomeName.toString())) {
            return true;
        }
        return false;
    }

    private static void registerEntityWorldSpawn (EntityType < ? > entity, PanthalassaCommonConfig.ExternalSpawningConfig spawnConfig, MobCategory classification, BiomeLoadingEvent event){
        event.getSpawns().getSpawner(classification).add(new MobSpawnSettings.SpawnerData(entity, spawnConfig.spawnRate.get(), spawnConfig.minGroupSize.get(), spawnConfig.maxGroupSize.get()));
    }
}