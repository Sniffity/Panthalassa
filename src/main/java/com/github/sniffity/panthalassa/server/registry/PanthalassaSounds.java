package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Panthalassa.MODID);

    public static final RegistryObject<SoundEvent> COELACANTH_AMBIENT = create("entity.creature.coelacanth.coelacanth_ambient");
    public static final RegistryObject<SoundEvent> KRONOSAURUS_AMBIENT = create("entity.creature.kronosaurus.kronosaurus_ambient");
    public static final RegistryObject<SoundEvent> MEGALODON_AMBIENT = create("entity.creature.megalodon.megalodon_ambient");

    private static RegistryObject<SoundEvent> create(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(Panthalassa.MODID, name)));
    }
}