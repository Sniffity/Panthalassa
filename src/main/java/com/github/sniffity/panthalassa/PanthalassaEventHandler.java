package com.github.sniffity.panthalassa;

import com.github.sniffity.panthalassa.client.render.creature.RenderKronosaurus;
import com.github.sniffity.panthalassa.registry.PanthalassaEntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;


@EventBusSubscriber(bus =  EventBusSubscriber.Bus.MOD, modid = Panthalassa.MODID)
public class PanthalassaEventHandler {

    @SubscribeEvent
    public static void createDefaultAttributes(EntityAttributeCreationEvent event) {
        event.put(
                PanthalassaEntityTypes.KRONOSAURUS.get(),
                LivingEntity.createLivingAttributes()
                        .add(Attributes.ATTACK_DAMAGE, 1)
                        .add(Attributes.ATTACK_KNOCKBACK, 1)
                        .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                        .add(Attributes.FOLLOW_RANGE, 100)
                        .add(Attributes.MAX_HEALTH, 125)
                        .add(Attributes.MOVEMENT_SPEED, 0.10F)
                        .build()
        );
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PanthalassaEntityTypes.KRONOSAURUS.get(), RenderKronosaurus::new);
    }
}
