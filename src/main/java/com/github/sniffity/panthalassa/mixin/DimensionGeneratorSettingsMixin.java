package com.github.sniffity.panthalassa.mixin;

import com.github.sniffity.panthalassa.server.world.dimension.SeedBearer;
import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * Panthalassa Mod - Class: SeedBearer <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 * Acknowledgements: The following class was developed implementing the same methods the Undergarden uses to generate
 *  random seeds.
 */
/*


@Mixin(WorldGenSettings.class)
public class DimensionGeneratorSettingsMixin {

    @Inject(method = "<init>(JZZLnet/minecraft/core/MappedRegistry;Ljava/util/Optional;)V", at = @At(value = "RETURN"))
    private void getSeedFromConstructor(long seed, boolean generateFeatures, boolean bonusChest, MappedRegistry<LevelStem> options, Optional<String> legacyOptions, CallbackInfo ci) {
        SeedBearer.putInSeed(seed);
    }

}

 */