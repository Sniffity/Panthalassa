package com.github.sniffity.panthalassa.server.network.packets;

import com.github.sniffity.panthalassa.server.vehicle.PanthalassaVehicle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketVehicleLights {
    public PacketVehicleLights() {}

    public static void handle(PacketVehicleLights packet, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide().isServer()) {
            context.get().enqueueWork(() -> {
                ServerPlayerEntity player = context.get().getSender();
                if (player != null) {
                    Entity vehicle = player.getRidingEntity();
                    PanthalassaVehicle panthalassaVehicle = (PanthalassaVehicle) vehicle;
                    if (panthalassaVehicle != null) {
                        panthalassaVehicle.respondKeybindLight();
                    }
                }
            });
        }
        context.get().setPacketHandled(true);
    }

    public static PacketVehicleLights decode(PacketBuffer buffer) {
        return new PacketVehicleLights();
    }

    public static void encode(PacketVehicleLights packet, PacketBuffer buffer) {}
}



