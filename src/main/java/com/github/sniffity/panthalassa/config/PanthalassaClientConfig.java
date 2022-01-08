package com.github.sniffity.panthalassa.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PanthalassaClientConfig {

    public static final ForgeConfigSpec GENERAL_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        setupConfig(configBuilder);
        GENERAL_SPEC = configBuilder.build();
    }

    private static void setupConfig(ForgeConfigSpec.Builder builder) {
        builder.comment("Panthalassa Client Config - Values changed in this document will only affect the client.");
        builder.push("Creature Size Multiplier");
        builder.comment("This value can be used to scale creature size upwards.");
        builder.comment("Setting a value of 3, for instance, will make all corresponding creatures three times larger.");
        builder.comment("Word of Warning: Larger creatures will possess less smooth animations and may cause visual clipping issues.");
        archelonSizeMultiplier = builder
                .defineInRange("archelon_size_multiplier", 1.0D, 0.5D, 3.0D);
        coealacanthSizeMultiplier = builder
                .defineInRange("coealacanth_size_multiplier", 1.0D, 0.5D, 3.0D);
        dunkleosteusSizeMultiplier = builder
                .defineInRange("dunkleosteus_size_multiplier", 1.0D, 0.5D, 3.0D);
        kronosaurusSizeMultiplier = builder
                .defineInRange("kronosaurus_size_multiplier", 1.0D, 0.5D, 3.0D);
        leedsichthysSizeMultiplier = builder
                .defineInRange("leedsichthys_size_multiplier", 1.0D, 0.5D, 3.0D);
        megalodonSizeMultiplier = builder
                .defineInRange("megalodon_size_multiplier", 1.0D, 0.5D, 3.0D);
        mosasaurusSizeMultiplier = builder
                .defineInRange("mosasaurus_size_multiplier", 1.0D, 0.5D, 3.0D);
        giantOrthoconeSizeMultiplier = builder
                .defineInRange("giant_orthocone_size_multiplier", 1.0D, 0.5D, 3.0D);
        basilosaurusSizeMultiplier = builder
                .defineInRange("basilosaurus_size_multiplier", 1.0D, 0.5D, 3.0D);
        builder.pop();

        builder.push("Vehicle Overlay");
        builder.comment("This will determine whether the vehicle overlays are enabled or not.");
        vehicleOverlayEnabled = builder
                .define("vehicle-overlay-enabled",true);
    }


    public static ForgeConfigSpec.DoubleValue archelonSizeMultiplier;
    public static ForgeConfigSpec.DoubleValue coealacanthSizeMultiplier;
    public static ForgeConfigSpec.DoubleValue dunkleosteusSizeMultiplier;
    public static ForgeConfigSpec.DoubleValue kronosaurusSizeMultiplier;
    public static ForgeConfigSpec.DoubleValue leedsichthysSizeMultiplier;
    public static ForgeConfigSpec.DoubleValue megalodonSizeMultiplier;
    public static ForgeConfigSpec.DoubleValue mosasaurusSizeMultiplier;
    public static ForgeConfigSpec.DoubleValue giantOrthoconeSizeMultiplier;
    public static ForgeConfigSpec.DoubleValue basilosaurusSizeMultiplier;

    public static ForgeConfigSpec.BooleanValue vehicleOverlayEnabled;

}
