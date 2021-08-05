package com.github.sniffity.panthalassa.client.vehicle;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.vehicle.PanthalassaVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class VehicleCamera {

    @SubscribeEvent
    private static void vehicleCamera(EntityViewRenderEvent.CameraSetup event) {
        Minecraft mc = Minecraft.getInstance();
        Entity vehicle = mc.player.getRidingEntity();
        if (!(vehicle instanceof PanthalassaVehicle)) return;
        PointOfView view = mc.gameSettings.getPointOfView();

        if (view != PointOfView.FIRST_PERSON) {
            event.getInfo().movePosition();
        }

    }

}

