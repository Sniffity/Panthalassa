package com.github.sniffity.panthalassa.client;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.client.render.entity.RenderKronosaurus;
import com.github.sniffity.panthalassa.client.render.vehicle.RenderMSRV;
import com.github.sniffity.panthalassa.common.registry.PanthalassaEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Panthalassa.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @OnlyIn(Dist.CLIENT)
        @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {

        MinecraftForge.EVENT_BUS.register(new VehicleOverlay());
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.KRONOSAURUS.get(),
                manager -> new RenderKronosaurus(manager));
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.MSRV.get(),
                manager -> new RenderMSRV(manager));
    }
}