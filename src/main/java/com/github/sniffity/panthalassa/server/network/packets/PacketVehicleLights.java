package com.github.sniffity.panthalassa.server.network.packets;

import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class PacketVehicleLights {
    public PacketVehicleLights() {}

    public static void handle(PacketVehicleLights packet, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide().isServer()) {
            context.get().enqueueWork(() -> {
                ServerPlayer player = context.get().getSender();
                if (player != null) {
                    Entity vehicle = player.getVehicle();
                    PanthalassaVehicle panthalassaVehicle = (PanthalassaVehicle) vehicle;
                    if (panthalassaVehicle != null) {
                        panthalassaVehicle.respondKeybindLight();
                    }
                }
            });
        }
        context.get().setPacketHandled(true);
    }

    public static PacketVehicleLights decode(FriendlyByteBuf buffer) {
        return new PacketVehicleLights();
    }

    public static void encode(PacketVehicleLights packet, FriendlyByteBuf buffer) {}
}