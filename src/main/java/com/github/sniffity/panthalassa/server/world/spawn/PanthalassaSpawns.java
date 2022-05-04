package com.github.sniffity.panthalassa.server.world.spawn;

import com.github.sniffity.panthalassa.config.PanthalassaCommonConfig;
import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import com.github.sniffity.panthalassa.server.registry.PanthalassaEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;

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
    public static final Map<EntityType<?>, PanthalassaCommonConfig.ExternalSpawningConfig> spawnConfigs = new HashMap<>();

    static {
        spawnConfigs.put(PanthalassaEntityTypes.KRONOSAURUS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.KRONOSAURUS.externalSpawning);
        spawnConfigs.put(PanthalassaEntityTypes.MEGALODON.get(), PanthalassaCommonConfig.COMMON.ENTITIES.MEGALODON.externalSpawning);
        spawnConfigs.put(PanthalassaEntityTypes.ARCHELON.get(), PanthalassaCommonConfig.COMMON.ENTITIES.ARCHELON.externalSpawning);
        spawnConfigs.put(PanthalassaEntityTypes.MOSASAURUS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.MOSASAURUS.externalSpawning);
        spawnConfigs.put(PanthalassaEntityTypes.COELACANTH.get(), PanthalassaCommonConfig.COMMON.ENTITIES.COELACANTH.externalSpawning);
        spawnConfigs.put(PanthalassaEntityTypes.DUNKLEOSTEUS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.DUNKLEOSTEUS.externalSpawning);
        spawnConfigs.put(PanthalassaEntityTypes.LEEDSICHTHYS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.LEEDISCHTHYS.externalSpawning);
        spawnConfigs.put(PanthalassaEntityTypes.GIANT_ORTHOCONE.get(), PanthalassaCommonConfig.COMMON.ENTITIES.GIANT_ORTHOCONE.externalSpawning);
        spawnConfigs.put(PanthalassaEntityTypes.BASILOSAURUS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.BASILOSAURUS.externalSpawning);


    }

    public static void registerSpawnPlacementTypes() {
        SpawnPlacements.Type.create("Panthalassa_Spawns", new TriPredicate<LevelReader, BlockPos, EntityType<? extends Mob>>() {

            @Override
            public boolean test(LevelReader t, BlockPos pos, EntityType<? extends Mob> entityType) {
                BlockState block = t.getBlockState(pos.below());
                if (block.getBlock() == Blocks.BEDROCK || block.getBlock() == Blocks.BARRIER || !block.getMaterial().blocksMotion())
                    return false;
                BlockState iblockstateUp = t.getBlockState(pos);
                BlockState iblockstateUp2 = t.getBlockState(pos.above());
                return NaturalSpawner.isValidEmptySpawnBlock(t, pos, iblockstateUp, iblockstateUp.getFluidState(), entityType) && NaturalSpawner.isValidEmptySpawnBlock(t, pos.above(), iblockstateUp2, iblockstateUp2.getFluidState(), entityType);
            }


        });

        SpawnPlacements.Type panthalassaSpawns = SpawnPlacements.Type.valueOf("Panthalassa_Spawns");
        if (panthalassaSpawns != null && PanthalassaCommonConfig.COMMON.GENERAL.externalSpawningBoolean.get()) {
            SpawnPlacements.register(PanthalassaEntityTypes.KRONOSAURUS.get(), panthalassaSpawns, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
            SpawnPlacements.register(PanthalassaEntityTypes.MEGALODON.get(), panthalassaSpawns, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
            SpawnPlacements.register(PanthalassaEntityTypes.ARCHELON.get(), panthalassaSpawns, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
            SpawnPlacements.register(PanthalassaEntityTypes.MOSASAURUS.get(), panthalassaSpawns, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
            SpawnPlacements.register(PanthalassaEntityTypes.COELACANTH.get(), panthalassaSpawns, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
            SpawnPlacements.register(PanthalassaEntityTypes.DUNKLEOSTEUS.get(), panthalassaSpawns, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
            SpawnPlacements.register(PanthalassaEntityTypes.LEEDSICHTHYS.get(), panthalassaSpawns, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
            SpawnPlacements.register(PanthalassaEntityTypes.GIANT_ORTHOCONE.get(), panthalassaSpawns, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
            SpawnPlacements.register(PanthalassaEntityTypes.BASILOSAURUS.get(), panthalassaSpawns, Heightmap.Types.MOTION_BLOCKING, PanthalassaEntity::canPanthalassaEntitySpawn);
        }
    }

    public static void onBiomeLoading(BiomeLoadingEvent event) {
        ResourceLocation biomeName = event.getName();
        if (biomeName == null || !PanthalassaCommonConfig.COMMON.GENERAL.externalSpawningBoolean.get())
            return;
        if (PanthalassaCommonConfig.COMMON.ENTITIES.KRONOSAURUS.externalSpawning.spawnRate.get() > 0 && isBiomeInConfig(PanthalassaCommonConfig.COMMON.ENTITIES.KRONOSAURUS.externalSpawning.biomeSpawningConfig, biomeName)) {
            registerEntityWorldSpawn(PanthalassaEntityTypes.KRONOSAURUS.get(), PanthalassaCommonConfig.COMMON.ENTITIES.KRONOSAURUS.externalSpawning, MobCategory.MONSTER, event);
        }

    }
    private static void registerEntityWorldSpawn (EntityType < ? > entity, PanthalassaCommonConfig.ExternalSpawningConfig spawnConfig, MobCategory classification, BiomeLoadingEvent event){
        event.getSpawns().getSpawner(classification).add(new MobSpawnSettings.SpawnerData(entity, spawnConfig.spawnRate.get(), spawnConfig.minGroupSize.get(), spawnConfig.maxGroupSize.get()));
    }

    public static boolean isBiomeInConfig(PanthalassaCommonConfig.BiomeSpawningConfig biomeConfig, ResourceLocation biomeName) {
        if (biomeConfig.biomeWhitelist.get().contains(biomeName.toString())) {
            return true;
        }
        Set<BiomeCombo> biomeCombos = new HashSet<>();

        ResourceKey<Biome> biomeRegistryKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, biomeName);
        for (BiomeCombo biomeCombo : biomeCombos) {
            if (biomeCombo.acceptsBiome(biomeRegistryKey)) return true;
        }
        return false;
    }

    private static class BiomeCombo {
        BiomeDictionary.Type[] neededTypes;
        boolean[] inverted;
        private BiomeCombo(String biomeComboString) {
            String[] typeStrings = biomeComboString.toUpperCase().replace(" ", "").split(",");
            inverted = new boolean[typeStrings.length];
            for (int i = 0; i < typeStrings.length; i++) {
                if (typeStrings[i].length() == 0) {
                    continue;
                }
            }
        }

        private boolean acceptsBiome(ResourceKey<Biome> biome) {
            Set<BiomeDictionary.Type> thisTypes = BiomeDictionary.getTypes(biome);
            for (int i = 0; i < neededTypes.length; i++) {
                if (neededTypes[i] == null) continue;
                if (!inverted[i]) {
                    if (!thisTypes.contains(neededTypes[i])) {
                        return false;
                    }
                }
                else {
                    if (thisTypes.contains(neededTypes[i])) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}