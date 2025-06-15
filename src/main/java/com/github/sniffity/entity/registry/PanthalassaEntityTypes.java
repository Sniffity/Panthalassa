package com.github.sniffity.entity.registry;

import com.github.sniffity.Panthalassa;
import com.github.sniffity.entity.creature.CreatureKronosaurus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class PanthalassaEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Panthalassa.MODID);

    public static final Supplier<EntityType<CreatureKronosaurus>> KRONOSAURUS = ENTITY_TYPES.register ("kronosaurus",()->
            EntityType.Builder.of(CreatureKronosaurus::new, MobCategory.CREATURE)
                    .sized(1.0F, 1.0F)
                    .eyeHeight(0.5f)
                    .canSpawnFarFromPlayer()
                    .build(ResourceLocation.fromNamespaceAndPath(Panthalassa.MODID, "kronosaurus").toString()));
}