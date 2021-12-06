package com.github.sniffity.panthalassa.client;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.client.events.CameraSetupEvent;
import com.github.sniffity.panthalassa.client.events.KeyInputEvent;
import com.github.sniffity.panthalassa.client.events.RenderTickEvent;
import com.github.sniffity.panthalassa.client.render.entity.*;
import com.github.sniffity.panthalassa.client.render.vehicle.RenderAGII;
import com.github.sniffity.panthalassa.client.render.vehicle.RenderMRSV;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.server.registry.PanthalassaEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.BlockItem;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.function.Supplier;


@Mod.EventBusSubscriber(modid = Panthalassa.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyInputEvent());
        MinecraftForge.EVENT_BUS.register(new RenderTickEvent());
        MinecraftForge.EVENT_BUS.register(new CameraSetupEvent());
        registerEntityRenderers();
        registerKeybinds();
        registerBlockColors();
        registerItemColors();
        registerBlockRenderers();
    }

    public static void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.KRONOSAURUS.get(),
                RenderKronosaurus::new);
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.MEGALODON.get(),
                RenderMegalodon::new);
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.ARCHELON.get(),
                RenderArchelon::new);
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.MOSASAURUS.get(),
                RenderMosasaurus::new);
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.COELACANTH.get(),
                RenderCoelacanth::new);
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.DUNKLEOSTEUS.get(),
                RenderDunkleosteus::new);
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.LEEDSICHTHYS.get(),
                RenderLeedsichthys::new);
        
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.MRSV.get(),
                RenderMRSV::new);
        RenderingRegistry.registerEntityRenderingHandler(PanthalassaEntityTypes.AGII.get(),
                RenderAGII::new);
    }

    public static final KeyBinding KEY_VEHICLE_LIGHTS = new KeyBinding("key.vehicle.lights",  GLFW.GLFW_KEY_H, "key.panthalassa.category");
    public static final KeyBinding KEY_VEHICLE_SPECIAL = new KeyBinding("key.vehicle.special", GLFW.GLFW_KEY_Y, "key.panthalassa.category");
    public static final KeyBinding KEY_VEHICLE_SONAR = new KeyBinding("key.vehicle.sonar", GLFW.GLFW_KEY_N, "key.panthalassa.category");

    public static void registerKeybinds() {
        ClientRegistry.registerKeyBinding(KEY_VEHICLE_LIGHTS);
        ClientRegistry.registerKeyBinding(KEY_VEHICLE_SPECIAL);
        ClientRegistry.registerKeyBinding(KEY_VEHICLE_SONAR);

    }


    private static void render(Supplier<? extends Block> block, RenderType render) {
        RenderTypeLookup.setRenderLayer(block.get(), render);
    }

    public static void registerBlockRenderers() {
        RenderType cutout = RenderType.cutout();
        RenderType mipped = RenderType.cutoutMipped();
        RenderType translucent = RenderType.translucent();

        render(PanthalassaBlocks.KRETHROSS, cutout);
        render(PanthalassaBlocks.KRETHROSS_PLANT, cutout);

    }


    public static void registerBlockColors() {
        BlockColors colors = Minecraft.getInstance().getBlockColors();

        colors.register((state, world, pos, tint) ->
                        world != null && pos != null ? BiomeColors.getAverageWaterColor(world, pos) : new Color(63, 101, 145).getRGB(),
                PanthalassaBlocks.KRETHROSS.get(),
                PanthalassaBlocks.KRETHROSS_PLANT.get()

        );

    }

    public static void registerItemColors() {
        BlockColors bColors = Minecraft.getInstance().getBlockColors();
        ItemColors iColors = Minecraft.getInstance().getItemColors();

        iColors.register((stack, tint) -> bColors.getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, 0),
                PanthalassaBlocks.KRETHROSS.get(),
                PanthalassaBlocks.KRETHROSS_PLANT.get()
        );

        iColors.register((stack, tint) -> {
                    if(tint == 0) {
                        return new Color(91, 117, 91).getRGB();
                    }
                    return -1;
                },
                PanthalassaBlocks.KRETHROSS.get(),
                PanthalassaBlocks.KRETHROSS_PLANT.get()
        );
    }


}

