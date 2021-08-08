package com.github.sniffity.panthalassa.client.events;

import com.github.sniffity.panthalassa.client.ClientHandler;
import com.github.sniffity.panthalassa.server.network.PanthalassaPacketHandler;
import com.github.sniffity.panthalassa.server.network.packets.PacketVehicleLights;
import com.github.sniffity.panthalassa.server.network.packets.PacketVehicleSonar;
import com.github.sniffity.panthalassa.server.network.packets.PacketVehicleSpecial;
import com.github.sniffity.panthalassa.server.vehicle.PanthalassaVehicle;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Panthalassa Mod - Class: KeyInputEvent <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Mr. Crayfish's Vehicle Mod
 * and Wyrmroost implement their own Keybindings.
 */

public class KeyInputEvent {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft game = Minecraft.getInstance();
        if (game.player != null) {
            Entity vehicle = getVehicle(game.player);
            if (vehicle instanceof PanthalassaVehicle) {
                if (ClientHandler.KEY_VEHICLE_SPECIAL.isKeyDown()) {
                    PanthalassaPacketHandler.INSTANCE.sendToServer(new PacketVehicleSpecial());
                }
                if (ClientHandler.KEY_VEHICLE_LIGHTS.isPressed()) {
                    PanthalassaPacketHandler.INSTANCE.sendToServer(new PacketVehicleLights());
                }
                if (ClientHandler.KEY_VEHICLE_SONAR.isPressed()) {
                    PanthalassaPacketHandler.INSTANCE.sendToServer(new PacketVehicleSonar());
                }
            }
        }
    }

    public static Entity getVehicle(Entity player) {
        return player.getRidingEntity();
    }
}