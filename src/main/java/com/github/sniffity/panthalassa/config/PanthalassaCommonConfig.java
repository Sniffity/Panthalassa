package com.github.sniffity.panthalassa.config;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public final class PanthalassaCommonConfig {

    public static ForgeConfigSpec COMMON_CONFIG;
    public static final Common COMMON;
    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    private static final Predicate<Object> STRING_PREDICATE = s -> s instanceof String;

    static {
        COMMON = new Common(COMMON_BUILDER);
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static class GeneralConfig {
        public final ForgeConfigSpec.BooleanValue giveJournal;
        public final ForgeConfigSpec.BooleanValue externalSpawningBoolean;

        GeneralConfig(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            this.giveJournal = builder
                    .comment("This boolean value will determine whether or not the Panthalassa Journal (Guide Book) is given to players when they first join the server")
                    .define("give_journal", true);
            this.externalSpawningBoolean = builder
                    .comment("This boolean value will determine whether Panthalassa's creatures spawn outside the Panthalassa Dimesnsion")
                    .define("external_spawning", false);
            builder.pop();
        }
    }


    public static class ExternalSpawningConfig {
        ExternalSpawningConfig(final ForgeConfigSpec.Builder builder, int spawnRate, int minGroupSize, int maxGroupSize, BiomeSpawningConfig biomeSpawningConfig) {
            builder.comment("This config will only take effect if external_spawning is set to TRUE");
            this.spawnRate = builder
                    .comment("External Spawn Rates will be proportional to this value. Set to 0 to disable spawning")
                    .defineInRange("spawn_rate", spawnRate, 0, Integer.MAX_VALUE);
            this.minGroupSize = builder
                    .comment("Minimum number of this entity that will spawn in each group")
                    .defineInRange("min_group_size", minGroupSize, 1, Integer.MAX_VALUE);
            this.maxGroupSize = builder
                    .comment("Maximum number of this entity that will spawn in each group")
                    .defineInRange("min_group_size", minGroupSize, 1, Integer.MAX_VALUE);
            this.biomeSpawningConfig = biomeSpawningConfig;
        }

        public final ForgeConfigSpec.IntValue spawnRate;
        public final ForgeConfigSpec.IntValue minGroupSize;
        public final ForgeConfigSpec.IntValue maxGroupSize;
        public final BiomeSpawningConfig biomeSpawningConfig;
    }

    public static class BiomeSpawningConfig {
        BiomeSpawningConfig(final ForgeConfigSpec.Builder builder, List<? extends String> biomeWhitelist) {
            builder.push("biome_config");
            this.biomeWhitelist = builder.comment("Allow spawns in these biomes regardless of the biome type settings")
                    .defineList("biome_whitelist", biomeWhitelist, STRING_PREDICATE);
            builder.pop();
        }

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> biomeWhitelist;
    }

    public static class Kronosaurus {
        Kronosaurus(final ForgeConfigSpec.Builder builder) {
            builder.push("kronosaurus");
            externalSpawning = new ExternalSpawningConfig(builder,
                    20,
                    1,
                    3,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Megalodon {
        Megalodon(final ForgeConfigSpec.Builder builder) {
            builder.push("megalodon");
            externalSpawning = new ExternalSpawningConfig(builder,
                    10,
                    1,
                    1,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Archelon {
        Archelon(final ForgeConfigSpec.Builder builder) {
            builder.push("Archelon");
            externalSpawning = new ExternalSpawningConfig(builder,
                    30,
                    1,
                    2,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class EntitiesConfig {
        EntitiesConfig(final ForgeConfigSpec.Builder builder) {
            builder.push("entities");
            KRONOSAURUS = new Kronosaurus(builder);
            MEGALODON = new Megalodon(builder);
            ARCHELON = new Archelon(builder);
            builder.pop();
        }

        public final Kronosaurus KRONOSAURUS;
        public final Megalodon MEGALODON;
        public final Archelon ARCHELON;

    }

    public static class Common {
        private Common(final ForgeConfigSpec.Builder builder) {
            GENERAL = new GeneralConfig(builder);
            ENTITIES = new EntitiesConfig(builder);
        }
        public final GeneralConfig GENERAL;
        public final EntitiesConfig ENTITIES;

    }
}
