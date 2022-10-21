package com.github.sniffity.panthalassa.client;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.client.events.CameraSetupEvent;
import com.github.sniffity.panthalassa.client.events.KeyInputEvent;
import com.github.sniffity.panthalassa.client.events.RenderTickEvent;
import com.github.sniffity.panthalassa.client.render.blockentity.RenderHydrothermalVent;
import com.github.sniffity.panthalassa.client.render.blockentity.RenderPressureEqualizer;
import com.github.sniffity.panthalassa.client.render.display.RenderArchelonShell;
import com.github.sniffity.panthalassa.client.render.display.RenderGiantOrthoconeShell;
import com.github.sniffity.panthalassa.client.render.display.RenderKronosaurusSkull;
import com.github.sniffity.panthalassa.client.render.display.RenderMegalodonJaws;
import com.github.sniffity.panthalassa.client.render.entity.*;
import com.github.sniffity.panthalassa.client.render.projectile.RenderBlastTorpedo;
import com.github.sniffity.panthalassa.client.render.projectile.RenderTranquilizingTorpedo;
import com.github.sniffity.panthalassa.client.render.vehicle.RenderAGII;
import com.github.sniffity.panthalassa.client.render.vehicle.RenderECSV;
import com.github.sniffity.panthalassa.client.render.vehicle.RenderMRSV;
import com.github.sniffity.panthalassa.client.render.vehicle.RenderPCSV;
import com.github.sniffity.panthalassa.server.item.armor.ItemDivingSuit;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlockEntities;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.server.registry.PanthalassaEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.BlockItem;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import java.awt.*;


@Mod.EventBusSubscriber(modid = Panthalassa.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyInputEvent());
        MinecraftForge.EVENT_BUS.register(new RenderTickEvent());
        MinecraftForge.EVENT_BUS.register(new CameraSetupEvent());
        registerBlockColors();
        registerItemColors();
    }

    @SubscribeEvent
    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
        event.register(KEY_VEHICLE_LIGHTS);
        event.register(KEY_VEHICLE_SPECIAL);
        event.register(KEY_VEHICLE_SONAR);
    }


    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(PanthalassaEntityTypes.KRONOSAURUS.get(),
                RenderKronosaurus::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.MEGALODON.get(),
                RenderMegalodon::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.ARCHELON.get(),
                RenderArchelon::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.MOSASAURUS.get(),
                RenderMosasaurus::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.COELACANTH.get(),
                RenderCoelacanth::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.DUNKLEOSTEUS.get(),
                RenderDunkleosteus::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.LEEDSICHTHYS.get(),
                RenderLeedsichthys::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.GIANT_ORTHOCONE.get(),
                RenderGiantOrthocone::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.BASILOSAURUS.get(),
                RenderBasilosaurus::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.THALASSOMEDON.get(),
                RenderThalassomedon::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.ACROLEPIS.get(),
                RenderAcrolepis::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.CERATODUS.get(),
                RenderCeratodus::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.HELICOPRION.get(),
                RenderHelicoprion::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.ANGLERFISH.get(),
                RenderAnglerfish::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.ANOMALOCARIS.get(),
                RenderAnomalocaris::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.MRSV.get(),
                RenderMRSV::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.AGII.get(),
                RenderAGII::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.GIANT_ORTHOCONE_SHELL.get(),
                RenderGiantOrthoconeShell::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.KRONOSAURUS_SKULL.get(),
                RenderKronosaurusSkull::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.ARCHELON_SHELL.get(),
                RenderArchelonShell::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.MEGALODON_JAWS.get(),
                RenderMegalodonJaws::new);
        event.registerBlockEntityRenderer(PanthalassaBlockEntities.HYDROTHERMAL_VENT_TE.get(),
                RenderHydrothermalVent::new);
        event.registerBlockEntityRenderer(PanthalassaBlockEntities.PRESSURE_EQUALIZER_TE.get(),
                RenderPressureEqualizer::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.PCSV.get(),
                RenderPCSV::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.ECSV.get(),
                RenderECSV::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.BLAST_TORPEDO.get(),
                RenderBlastTorpedo::new);
        event.registerEntityRenderer(PanthalassaEntityTypes.TRANQUILIZING_TORPEDO.get(),
                RenderTranquilizingTorpedo::new);
    }

    public static final KeyMapping KEY_VEHICLE_LIGHTS = new KeyMapping("key.vehicle.lights",  GLFW.GLFW_KEY_H, "key.panthalassa.category");
    public static final KeyMapping KEY_VEHICLE_SPECIAL = new KeyMapping("key.vehicle.special", GLFW.GLFW_KEY_Y, "key.panthalassa.category");
    public static final KeyMapping KEY_VEHICLE_SONAR = new KeyMapping("key.vehicle.sonar", GLFW.GLFW_KEY_N, "key.panthalassa.category");

    public static void registerBlockColors() {
        BlockColors colors = Minecraft.getInstance().getBlockColors();

        colors.register((state, world, pos, tint) ->
                        world != null && pos != null ? BiomeColors.getAverageWaterColor(world, pos) : new Color(63, 101, 145).getRGB(),
                PanthalassaBlocks.KRETHROSS.get(),
                PanthalassaBlocks.KRETHROSS_PLANT.get(),
                PanthalassaBlocks.FROSTGRASS.get()
        );
    }

    public static void registerItemColors() {
        BlockColors bColors = Minecraft.getInstance().getBlockColors();
        ItemColors iColors = Minecraft.getInstance().getItemColors();

        iColors.register((stack, tint) -> bColors.getColor(((BlockItem) stack.getItem()).getBlock().defaultBlockState(), null, null, 0),
                PanthalassaBlocks.KRETHROSS.get(),
                PanthalassaBlocks.KRETHROSS_PLANT.get(),
                PanthalassaBlocks.FROSTGRASS.get()
        );

        iColors.register((stack, tint) -> {
            if(tint == 0) {
                return new Color(91, 117, 91).getRGB();
            }
            return -1;
            },
                PanthalassaBlocks.KRETHROSS.get(),
                PanthalassaBlocks.KRETHROSS_PLANT.get().asItem(),
                PanthalassaBlocks.FROSTGRASS.get()
        );
    }
}