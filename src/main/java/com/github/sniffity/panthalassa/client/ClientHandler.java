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
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.BlockItem;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.function.Supplier;


@Mod.EventBusSubscriber(modid = Panthalassa.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {
    Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyInputEvent());
        MinecraftForge.EVENT_BUS.register(new RenderTickEvent());
        MinecraftForge.EVENT_BUS.register(new CameraSetupEvent());
        registerKeybinds();
        registerBlockColors();
        registerItemColors();
        registerBlockRenderers();
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PanthalassaEntityTypes.KRONOSAURUS,
                RenderKronosaurus::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.MEGALODON,
                RenderMegalodon::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.ARCHELON,
                RenderArchelon::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.MOSASAURUS,
                RenderMosasaurus::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.COELACANTH,
                RenderCoelacanth::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.DUNKLEOSTEUS,
                RenderDunkleosteus::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.LEEDSICHTHYS,
                RenderLeedsichthys::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.MRSV,
                RenderMRSV::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.AGII,
                RenderAGII::new);
    }

    public static final KeyMapping KEY_VEHICLE_LIGHTS = new KeyMapping("key.vehicle.lights",  GLFW.GLFW_KEY_H, "key.panthalassa.category");
    public static final KeyMapping KEY_VEHICLE_SPECIAL = new KeyMapping("key.vehicle.special", GLFW.GLFW_KEY_Y, "key.panthalassa.category");
    public static final KeyMapping KEY_VEHICLE_SONAR = new KeyMapping("key.vehicle.sonar", GLFW.GLFW_KEY_N, "key.panthalassa.category");

    public static void registerKeybinds() {
        ClientRegistry.registerKeyBinding(KEY_VEHICLE_LIGHTS);
        ClientRegistry.registerKeyBinding(KEY_VEHICLE_SPECIAL);
        ClientRegistry.registerKeyBinding(KEY_VEHICLE_SONAR);

    }


    private static void render(Supplier<? extends Block> block, RenderType render) {
        ItemBlockRenderTypes.setRenderLayer(block.get(), render);
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

