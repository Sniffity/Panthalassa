package com.github.sniffity.panthalassa.server.effects;

import com.github.sniffity.panthalassa.server.registry.PanthalassaEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class DisorientEffect extends MobEffect {

    public DisorientEffect() {
        super(MobEffectCategory.HARMFUL, 42424242);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return this == PanthalassaEffects.DISORIENT.get();
    }
}
