package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.gen.carver.CarverAncientCanyon;
import com.github.sniffity.panthalassa.server.world.gen.feature.FeaturePrimevalExpanseKelp;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaCarvers {
    public static final DeferredRegister<WorldCarver<?>> CARVERS = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, Panthalassa.MODID);

    public static final RegistryObject<WorldCarver<?>> ANCIENT_CANYON_CARVER =
            CARVERS.register("ancient_canyon_carver", () -> new CarverAncientCanyon(ProbabilityConfig.CODEC));
}
