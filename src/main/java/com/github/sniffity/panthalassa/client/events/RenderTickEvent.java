package com.github.sniffity.panthalassa.client.events;

import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.server.vehicle.PanthalassaVehicle;
import com.github.sniffity.panthalassa.server.vehicle.VehicleMRSV;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * Panthalassa Mod - Class: RenderTickEvent <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Mr. Crayfish's Vehicle Mod
 * implements its own Vehicle Overlay.
 */

public class RenderTickEvent {

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        Minecraft game = Minecraft.getInstance();
        if (!game.isGameFocused() || game.gameSettings.hideGUI)
            return;

        PlayerEntity player = game.player;
        if (player == null)
            return;

        Entity playerVehicle = player.getRidingEntity();
        if (!(playerVehicle instanceof PanthalassaVehicle))
            return;

        MatrixStack matrixStack = new MatrixStack();
        PanthalassaVehicle vehicle = (PanthalassaVehicle) playerVehicle;

        String vehicleText;
        if (vehicle instanceof VehicleMRSV) {
            vehicleText = "MANTA RAY SUBMERSIBLE VEHICLE";
            game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.BOLD + vehicleText, 150, 10, Color.WHITE.getRGB());
        }

        String vehicleIntegrity;
        if (vehicle.getMaxHealth() != 0) {
            vehicleIntegrity = new DecimalFormat("00").format((vehicle.getHealth() / vehicle.getMaxHealth()) * 100);
            if (((vehicle.getHealth()/vehicle.getMaxHealth())*100)>50){
                game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "Vehicle Integrity: " + TextFormatting.GREEN + vehicleIntegrity, 10, 25, Color.WHITE.getRGB());
            } else if (((vehicle.getHealth()/vehicle.getMaxHealth())*100)>20){
                game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "Vehicle Integrity: " + TextFormatting.YELLOW + vehicleIntegrity, 10, 25, Color.WHITE.getRGB());
            } else if (((vehicle.getHealth()/vehicle.getMaxHealth())*100)>0){
                game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "Vehicle Integrity: " + TextFormatting.RED + vehicleIntegrity, 10, 25, Color.WHITE.getRGB());
            } else {
                game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "Vehicle Integrity: " + TextFormatting.DARK_PURPLE + "processing...", 10, 25, Color.WHITE.getRGB());
            }
        }

        String depth;
        if (vehicle.world.getDimensionKey() == PanthalassaDimension.PANTHALASSA) {
            depth = new DecimalFormat("0").format(vehicle.getPosition().getY()-20000);
        } else {
            depth = new DecimalFormat("0").format(vehicle.getPosition().getY());
        }
        game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "Depth: " + TextFormatting.YELLOW + depth, 10, 35, Color.WHITE.getRGB());

        Float speed;
        String speedText;
        speed = vehicle.getTravelSpeed();
        speedText = new DecimalFormat("0").format(speed);
        if (speed !=null) {
            game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "Speed: " + TextFormatting.GOLD + speedText, 10, 45, Color.WHITE.getRGB());
        }

        String nlfDistance;
        if (vehicle.getNLFDistance() != null) {
            nlfDistance = new DecimalFormat("0").format(vehicle.getNLFDistance());
            if (vehicle.getNLFDistance()>10) {
                game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "NLF Distance: " + TextFormatting.GREEN + nlfDistance, 10, 220, Color.WHITE.getRGB());
            } else if (vehicle.getNLFDistance()>5){
                game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "NLF Distance: " + TextFormatting.YELLOW + nlfDistance, 10, 220, Color.WHITE.getRGB());
            } else if (vehicle.getNLFDistance()>=0) {
                game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "NLF Distance: " + TextFormatting.RED + nlfDistance, 10, 220, Color.WHITE.getRGB());
            } else {
                game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "NLF Distance: " + TextFormatting.DARK_PURPLE + "processing...", 10, 220, Color.WHITE.getRGB());
            }
        }

        int floorDistance;
        String floorDistanceText;
        floorDistance = vehicle.getFloorDistance();
        floorDistanceText = new DecimalFormat("0").format(floorDistance);

        if (floorDistance>20) {
            game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "Floor Distance: " + TextFormatting.GREEN + floorDistanceText, 10, 230, Color.WHITE.getRGB());
        } else if (floorDistance>10) {
            game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "Floor Distance: " + TextFormatting.YELLOW + floorDistanceText, 10, 230, Color.WHITE.getRGB());
        } else if (floorDistance>=0) {
            game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "Floor Distance: " + TextFormatting.RED + floorDistanceText, 10, 230, Color.WHITE.getRGB());
        } else {
            game.fontRenderer.drawStringWithShadow(matrixStack, TextFormatting.AQUA + "Floor Distance: " + TextFormatting.DARK_PURPLE + "processing...", 10, 230, Color.WHITE.getRGB());
        }
    }
}

