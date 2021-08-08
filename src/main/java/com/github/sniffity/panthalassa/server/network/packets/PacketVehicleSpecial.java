package com.github.sniffity.panthalassa.server.network.packets;

import com.github.sniffity.panthalassa.server.vehicle.PanthalassaVehicle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketVehicleSpecial {
    public PacketVehicleSpecial() {}

    public static void handle(PacketVehicleSpecial packet, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide().isServer()) {
            context.get().enqueueWork(() -> {
                ServerPlayerEntity player = context.get().getSender();
                if (player != null) {
                    Entity vehicle = player.getRidingEntity();
                    PanthalassaVehicle panthalassaVehicle = (PanthalassaVehicle) vehicle;
                    if (panthalassaVehicle != null) {
                        panthalassaVehicle.respondKeybindSpecial();
                    }
                }
            });
            context.get().setPacketHandled(true);
        }
    }

    public static PacketVehicleSpecial decode(PacketBuffer buffer) {
            return new PacketVehicleSpecial();
    }

    public static void encode(PacketVehicleSpecial packet, PacketBuffer buffer) {}
}