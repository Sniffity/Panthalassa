package com.github.sniffity.panthalassa.client;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.client.render.entity.RenderKronosaurus;
import com.github.sniffity.panthalassa.client.render.vehicle.RenderMRSV;
import com.github.sniffity.panthalassa.client.vehicle.VehicleCamera;
import com.github.sniffity.panthalassa.client.vehicle.VehicleOverlay;
import com.github.sniffity.panthalassa.common.registry.PanthalassaEntityTypes;
import com.github.sniffity.panthalassa.vehicle.PanthalassaVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
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
//        MinecraftForge.EVENT_BUS.register(new VehicleCamera());
//        MinecraftForge.EVENT_BUS.register(new VehcileGUI());
        MinecraftForge.EVENT_BUS.addListener(VehicleCamera::setupVehicleCamera);


        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.KRONOSAURUS.get(),
                manager -> new RenderKronosaurus(manager));
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.mrsv.get(),
                manager -> new RenderMRSV(manager));
    }
}

