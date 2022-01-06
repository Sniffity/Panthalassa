package com.github.sniffity.panthalassa.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PanthalassaServerConfig {

    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Panthalassa Server Config");
        builder.push("Give Journan");
        builder.comment("This boolean value will determine whether or not the Panthalassa Journal (Guide Book) is given to players when they first join the server");
        giveJournal = builder
                .define("give_journal", true);
        builder.pop();

    }


    public static ForgeConfigSpec.BooleanValue giveJournal;


}
