package com.github.sniffity;

import com.github.sniffity.client.render.creature.RenderKronosaurus;
import com.github.sniffity.entity.registry.PanthalassaEntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.w3c.dom.Attr;


@EventBusSubscriber(bus =  EventBusSubscriber.Bus.MOD, modid = Panthalassa.MODID)
public class PanthalassaEventHandler {

    @SubscribeEvent
    public static void createDefaultAttributes(EntityAttributeCreationEvent event) {
        event.put(
                // Your entity type.
                PanthalassaEntityTypes.KRONOSAURUS.get(),
                // An AttributeSupplier. This is typically created by calling LivingEntity#createLivingAttributes,
                // setting your values on it, and calling #build. You can also create the AttributeSupplier from scratch
                // if you want, see the source of LivingEntity#createLivingAttributes for an example.
                LivingEntity.createLivingAttributes()
                        // Add an attribute with its default value.
                        .add(Attributes.MAX_HEALTH, 50)
                        .add(Attributes.FOLLOW_RANGE)
                        .build()
        );
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PanthalassaEntityTypes.KRONOSAURUS.get(),
                RenderKronosaurus::new);
    }
}
