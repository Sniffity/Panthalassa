package com.github.sniffity.panthalassa.server.effects;


import com.github.sniffity.panthalassa.server.registry.PanthalassaEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class CrushResistEffect extends MobEffect {

    public CrushResistEffect() {
        super(MobEffectCategory.BENEFICIAL, 42424242);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return this == PanthalassaEffects.CRUSH_RESIST.get();
    }
}