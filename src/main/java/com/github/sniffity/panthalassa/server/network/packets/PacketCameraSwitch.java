package com.github.sniffity.panthalassa.server.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.CameraType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketCameraSwitch {

    public PacketCameraSwitch() {}

    public static void handle(PacketCameraSwitch packet, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide().isClient()) {
            Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
    }

    public static PacketCameraSwitch decode(FriendlyByteBuf buffer) {
        return new PacketCameraSwitch();
    }

    public static void encode(PacketCameraSwitch packet, FriendlyByteBuf buffer) {}
}