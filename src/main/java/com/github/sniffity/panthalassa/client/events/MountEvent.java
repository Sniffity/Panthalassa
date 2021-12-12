package com.github.sniffity.panthalassa.client.events;


import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import com.github.sniffity.panthalassa.server.network.PanthalassaPacketHandler;
import com.github.sniffity.panthalassa.server.network.packets.PacketCameraSwitch;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class MountEvent {

    @SubscribeEvent
    public void onEntityMountEvent (EntityMountEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            Supplier<ServerPlayerEntity> player = (Supplier<ServerPlayerEntity>) event.getEntity();
            if (event.isMounting() && event.getEntityBeingMounted() instanceof PanthalassaVehicle) {
                PanthalassaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(player), new PacketCameraSwitch());

            }
        }
    }

}
