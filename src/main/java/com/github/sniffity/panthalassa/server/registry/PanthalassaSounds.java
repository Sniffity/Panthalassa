package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PanthalassaSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Panthalassa.MODID);

    public static final RegistryObject<SoundEvent> KRONOSAURUS_AMBIENT = create("entity.creature.kronosaurus.kronosaurus_ambient");

    private static RegistryObject<SoundEvent> create(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(Panthalassa.MODID, name)));
    }
}