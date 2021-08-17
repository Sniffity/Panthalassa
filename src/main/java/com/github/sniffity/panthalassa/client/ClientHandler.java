package com.github.sniffity.panthalassa.client;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.client.events.CameraSetupEvent;
import com.github.sniffity.panthalassa.client.events.KeyInputEvent;
import com.github.sniffity.panthalassa.client.events.RenderTickEvent;
import com.github.sniffity.panthalassa.client.render.entity.RenderArchelon;
import com.github.sniffity.panthalassa.client.render.entity.RenderKronosaurus;
import com.github.sniffity.panthalassa.client.render.entity.RenderMegalodon;
import com.github.sniffity.panthalassa.client.render.vehicle.RenderAG;
import com.github.sniffity.panthalassa.client.render.vehicle.RenderMRSV;
import com.github.sniffity.panthalassa.server.registry.PanthalassaEntityTypes;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.glfw.GLFW;


@Mod.EventBusSubscriber(modid = Panthalassa.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyInputEvent());
        MinecraftForge.EVENT_BUS.register(new RenderTickEvent());
        MinecraftForge.EVENT_BUS.register(new CameraSetupEvent());
        registerEntityRenderers();
        registerKeybinds();
    }

    public static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.KRONOSAURUS.get(),
                RenderKronosaurus::new);
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.MEGALODON.get(),
                RenderMegalodon::new);
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.ARCHELON.get(),
                RenderArchelon::new);

        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.MRSV.get(),
                manager -> new RenderMRSV(manager));
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.AG.get(),
                manager -> new RenderAG(manager));
    }

    public static final KeyBinding KEY_VEHICLE_LIGHTS = new KeyBinding("key.vehicle.lights",  GLFW.GLFW_KEY_H, "key.panthalassa.category");
    public static final KeyBinding KEY_VEHICLE_SPECIAL = new KeyBinding("key.vehicle.special", GLFW.GLFW_KEY_Y, "key.panthalassa.category");
    public static final KeyBinding KEY_VEHICLE_SONAR = new KeyBinding("key.vehicle.sonar", GLFW.GLFW_KEY_N, "key.panthalassa.category");

    public static void registerKeybinds() {
        ClientRegistry.registerKeyBinding(KEY_VEHICLE_LIGHTS);
        ClientRegistry.registerKeyBinding(KEY_VEHICLE_SPECIAL);
        ClientRegistry.registerKeyBinding(KEY_VEHICLE_SONAR);

    }

}

