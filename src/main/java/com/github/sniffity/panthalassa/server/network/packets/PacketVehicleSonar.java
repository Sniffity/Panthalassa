package com.github.sniffity.panthalassa.server.network.packets;

import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketVehicleSonar {
    public PacketVehicleSonar() {}

    public static void handle(PacketVehicleSonar packet, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide().isServer()) {
            context.get().enqueueWork(() -> {
                ServerPlayerEntity player = context.get().getSender();
                if (player != null) {
                    Entity vehicle = player.getVehicle();
                    PanthalassaVehicle panthalassaVehicle = (PanthalassaVehicle) vehicle;
                    if (panthalassaVehicle != null) {
                        panthalassaVehicle.respondKeybindSonar();
                    }
                }
            });
            context.get().setPacketHandled(true);
        }
    }

    public static PacketVehicleSonar decode(PacketBuffer buffer) {
        return new PacketVehicleSonar();
    }

    public static void encode(PacketVehicleSonar packet, PacketBuffer buffer) {}
}