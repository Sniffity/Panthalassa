package com.github.sniffity.panthalassa.server.network.packets;

import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCameraSwitch {

    public PacketCameraSwitch() {}

    public static void handle(PacketCameraSwitch packet, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide().isClient()) {
            Minecraft.getInstance().options.setCameraType(PointOfView.THIRD_PERSON_BACK);
        }
    }

    public static PacketCameraSwitch decode(PacketBuffer buffer) {
        return new PacketCameraSwitch();
    }

    public static void encode(PacketCameraSwitch packet, PacketBuffer buffer) {}
}