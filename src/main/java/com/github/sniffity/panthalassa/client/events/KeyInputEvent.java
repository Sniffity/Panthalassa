package com.github.sniffity.panthalassa.client.events;

import com.github.sniffity.panthalassa.client.ClientHandler;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyInputEvent {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event)
    {
        if (ClientHandler.KEY_VEHICLE_SPECIAL.isKeyDown())
        {
            System.out.println("Vehicle Lights Keybind recognized!");
        }
        if (ClientHandler.KEY_VEHICLE_LIGHTS.isPressed())
        {
            System.out.println("Vehicle Special Keybind recognized!");
        }
    }
}