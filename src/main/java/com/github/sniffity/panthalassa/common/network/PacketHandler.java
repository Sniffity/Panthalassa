package com.github.sniffity.panthalassa.common.network;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.lwjgl.glfw.GLFW;



public class PacketHandler {

    public static final String PROTOCOL_VERSION ="0.1.0";

    public static final SimpleChannel SIMPLE_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Panthalassa.MODID, "network"),
                    () -> PROTOCOL_VERSION,
                    version -> version.equals(PROTOCOL_VERSION),
                    version -> version.equals(PROTOCOL_VERSION));
    public static final KeyBinding VEHICLE_KEY_SPECIAL = new KeyBinding("key.vehicle.special", GLFW.GLFW_KEY_H, "key.categories.panthalassa.vehicle");

    public static final KeyBinding VEHICLE_KEY_LIGHTS = new KeyBinding("key.vehicle.lights", GLFW.GLFW_KEY_H, "key.categories.panthalassa.vehicle");


    SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Panthalassa.MODID, "networking"))
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .networkProtocolVersion(() -> "1.0.0")
            .simpleChannel();
}
