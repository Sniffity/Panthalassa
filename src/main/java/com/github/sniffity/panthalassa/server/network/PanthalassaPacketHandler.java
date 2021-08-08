package com.github.sniffity.panthalassa.server.network;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.network.packets.PacketVehicleLights;
import com.github.sniffity.panthalassa.server.network.packets.PacketVehicleSonar;
import com.github.sniffity.panthalassa.server.network.packets.PacketVehicleSpecial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PanthalassaPacketHandler {

    private static final String PROTOCOL_VERSION ="1.0";
    private static int messageId = 0;

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Panthalassa.MODID, "network"),
                    () -> PROTOCOL_VERSION,
                    version -> version.equals(PROTOCOL_VERSION),
                    version -> version.equals(PROTOCOL_VERSION));

    public static void register() {
        INSTANCE.registerMessage(messageId++, PacketVehicleSpecial.class, PacketVehicleSpecial::encode, PacketVehicleSpecial::decode, PacketVehicleSpecial::handle);
        INSTANCE.registerMessage(messageId++, PacketVehicleLights.class, PacketVehicleLights::encode, PacketVehicleLights::decode, PacketVehicleLights::handle);
        INSTANCE.registerMessage(messageId++, PacketVehicleSonar.class, PacketVehicleSonar::encode, PacketVehicleSonar::decode, PacketVehicleSonar::handle);

    }
}
