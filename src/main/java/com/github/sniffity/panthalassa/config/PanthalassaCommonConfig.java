package com.github.sniffity.panthalassa.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.checkerframework.checker.units.qual.A;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * Panthalassa Mod - Class: PanthalassaCommonConfig <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Mowzie's Mobs handles
 * their own config for controlling entity spawns.
 */

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
        public final ForgeConfigSpec.BooleanValue crushDepth;
        public final ForgeConfigSpec.BooleanValue randomSwimmingChecks;

        GeneralConfig(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            this.giveJournal = builder
                    .comment("This boolean value will determine whether or not the Panthalassa Journal (Guide Book) is given to players when they first join the server")
                    .define("give_journal", true);
            this.externalSpawningBoolean = builder
                    .comment("This boolean value will determine whether Panthalassa's creatures spawn outside the Panthalassa Dimension")
                    .define("external_spawning", false);
            this.crushDepth = builder
                    .comment("This boolean value will determine whether the crush depth mechanic is enabled within Panthalassa")
                    .define("crush_depth", false);
            this.randomSwimmingChecks = builder
                    .comment("This boolean value will determine whether entities will try and avoid walls when randomly swimming")
                    .define("random_swimming_avoid", false);
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
                    .defineInRange("max_group_size", maxGroupSize, 1, Integer.MAX_VALUE);
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
            this.biomeWhitelist = builder.comment("Allow spawns in these biomes")
                    .defineList("biome_whitelist", biomeWhitelist, STRING_PREDICATE);
            builder.pop();
        }
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> biomeWhitelist;
    }

    public static class Kronosaurus {
        Kronosaurus(final ForgeConfigSpec.Builder builder) {
            builder.push("kronosaurus");
            externalSpawning = new ExternalSpawningConfig(builder,
                    2,
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
                    1,
                    1,
                    1,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Archelon {
        Archelon(final ForgeConfigSpec.Builder builder) {
            builder.push("archelon");
            externalSpawning = new ExternalSpawningConfig(builder,
                    3,
                    1,
                    2,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Mosasaurus {
        Mosasaurus(final ForgeConfigSpec.Builder builder) {
            builder.push("mosasaurus");
            externalSpawning = new ExternalSpawningConfig(builder,
                    1,
                    1,
                    1,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Coelacanth {
        Coelacanth(final ForgeConfigSpec.Builder builder) {
            builder.push("coelacanth");
            externalSpawning = new ExternalSpawningConfig(builder,
                    5,
                    3,
                    5,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Dunkleosteus {
        Dunkleosteus(final ForgeConfigSpec.Builder builder) {
            builder.push("dunkleosteus");
            externalSpawning = new ExternalSpawningConfig(builder,
                    1,
                    1,
                    2,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Leedischthys {
        Leedischthys(final ForgeConfigSpec.Builder builder) {
            builder.push("leedischthys");
            externalSpawning = new ExternalSpawningConfig(builder,
                    2,
                    1,
                    2,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class GiantOrthocone {
        GiantOrthocone(final ForgeConfigSpec.Builder builder) {
            builder.push("giant_orthocone");
            externalSpawning = new ExternalSpawningConfig(builder,
                    1,
                    1,
                    1,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Basilosaurus {
        Basilosaurus(final ForgeConfigSpec.Builder builder) {
            builder.push("basilosaurus");
            externalSpawning = new ExternalSpawningConfig(builder,
                    3,
                    1,
                    2,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Thalassomedon {
        Thalassomedon(final ForgeConfigSpec.Builder builder) {
            builder.push("thalassomedon");
            externalSpawning = new ExternalSpawningConfig(builder,
                    3,
                    1,
                    2,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Acrolepis {
        Acrolepis(final ForgeConfigSpec.Builder builder) {
            builder.push("acrolepis");
            externalSpawning = new ExternalSpawningConfig(builder,
                    3,
                    3,
                    5,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Ceratodus {
        Ceratodus(final ForgeConfigSpec.Builder builder) {
            builder.push("ceratodus");
            externalSpawning = new ExternalSpawningConfig(builder,
                    3,
                    3,
                    5,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Helicoprion {
        Helicoprion(final ForgeConfigSpec.Builder builder) {
            builder.push("helicoprion");
            externalSpawning = new ExternalSpawningConfig(builder,
                    3,
                    3,
                    5,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Anglerfish {
        Anglerfish(final ForgeConfigSpec.Builder builder) {
            builder.push("anglerfish");
            externalSpawning = new ExternalSpawningConfig(builder,
                    3,
                    3,
                    5,
                    new BiomeSpawningConfig(builder, Collections.singletonList("minecraft:cold_ocean,minecraft:deep_cold_ocean,minecraft:deep_frozen_ocean,minecraft:deep_lukewarm_ocean,minecraft:deep_ocean,minecraft:frozen_ocean,minecraft:lukewarm_ocean,minecraft:ocean,minecraft:warm_ocean,minecraft:frozen_river,minecraft:river")));
            builder.pop();
        }
        public final ExternalSpawningConfig externalSpawning;
    }

    public static class Anomalocaris {
        Anomalocaris(final ForgeConfigSpec.Builder builder) {
            builder.push("anomalocaris");
            externalSpawning = new ExternalSpawningConfig(builder,
                    3,
                    3,
                    5,
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
            MOSASAURUS = new Mosasaurus(builder);
            COELACANTH = new Coelacanth(builder);
            DUNKLEOSTEUS = new Dunkleosteus(builder);
            LEEDSICHTHYS = new Leedischthys(builder);
            GIANT_ORTHOCONE = new GiantOrthocone(builder);
            BASILOSAURUS = new Basilosaurus(builder);
            THALASSOMEDON = new Thalassomedon(builder);
            ACROLEPIS = new Acrolepis(builder);
            CERATODUS = new Ceratodus(builder);
            HELICOPRION = new Helicoprion(builder);
            ANGLERFISH = new Anglerfish(builder);
            ANOMALOCARIS = new Anomalocaris(builder);
            builder.pop();
        }

        public final Kronosaurus KRONOSAURUS;
        public final Megalodon MEGALODON;
        public final Archelon ARCHELON;
        public final Mosasaurus MOSASAURUS;
        public final Coelacanth COELACANTH;
        public final Dunkleosteus DUNKLEOSTEUS;
        public final Leedischthys LEEDSICHTHYS;
        public final GiantOrthocone GIANT_ORTHOCONE;
        public final Basilosaurus BASILOSAURUS;
        public final Thalassomedon THALASSOMEDON;
        public final Acrolepis ACROLEPIS;
        public final Ceratodus CERATODUS;
        public final Helicoprion HELICOPRION;
        public final Anglerfish ANGLERFISH;
        public final Anomalocaris ANOMALOCARIS;
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