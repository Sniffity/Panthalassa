package com.github.sniffity.panthalassa.common.network;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;


public class PanthalassaPacketHandler {

    public static final String PROTOCOL_VERSION ="0.1.0";

    public static final SimpleChannel SIMPLE_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Panthalassa.MODID, "network"),
                    () -> PROTOCOL_VERSION,
                    version -> version.equals(PROTOCOL_VERSION),
                    version -> version.equals(PROTOCOL_VERSION));



    SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Panthalassa.MODID, "networking"))
            .clientAcceptedVersions(s -> true)
            .serverAcceptedVersions(s -> true)
            .networkProtocolVersion(() -> "1.0.0")
            .simpleChannel();
}
