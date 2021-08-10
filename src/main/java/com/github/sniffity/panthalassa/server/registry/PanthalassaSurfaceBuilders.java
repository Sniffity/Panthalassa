package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.gen.surfacebuilders.PrimevalExpanse;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaSurfaceBuilders {

    public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, Panthalassa.MODID);

    public static final RegistryObject<SurfaceBuilder<SurfaceBuilderConfig>> PRIMEVAL_EXPANSE = SURFACE_BUILDERS.register("primeval_expanse",
            () -> new PrimevalExpanse(SurfaceBuilderConfig.CODEC));
}