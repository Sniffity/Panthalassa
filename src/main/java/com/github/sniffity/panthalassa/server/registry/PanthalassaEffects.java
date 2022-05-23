package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.effects.CrushResistEffect;
import com.github.sniffity.panthalassa.server.effects.DisorientEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PanthalassaEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Panthalassa.MODID);

    public static final RegistryObject<MobEffect> CRUSH_RESIST = EFFECTS.register("crush_resist", CrushResistEffect::new);
    public static final RegistryObject<MobEffect> DISORIENT = EFFECTS.register("disorient", DisorientEffect::new);

}
