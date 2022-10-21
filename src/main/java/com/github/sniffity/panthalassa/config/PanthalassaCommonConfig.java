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
 * their own config.
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
        public final ForgeConfigSpec.BooleanValue crushDepth;
        public final ForgeConfigSpec.BooleanValue randomSwimmingChecks;

        GeneralConfig(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            this.giveJournal = builder
                    .comment("This boolean value will determine whether or not the Panthalassa Journal (Guide Book) is given to players when they first join the server")
                    .define("give_journal", true);
            this.crushDepth = builder
                    .comment("This boolean value will determine whether the crush depth mechanic is enabled within Panthalassa")
                    .define("crush_depth", false);
            this.randomSwimmingChecks = builder
                    .comment("This boolean value will determine whether entities will try and avoid walls when randomly swimming")
                    .define("random_swimming_avoid", false);
            builder.pop();
        }
    }

    public static class Common {
        private Common(final ForgeConfigSpec.Builder builder) {
            GENERAL = new GeneralConfig(builder);
        }
        public final GeneralConfig GENERAL;
    }
}