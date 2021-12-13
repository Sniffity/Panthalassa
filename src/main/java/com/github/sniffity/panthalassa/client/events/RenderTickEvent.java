package com.github.sniffity.panthalassa.client.events;

import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleAGII;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleMRSV;
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
        if (!PanthalassaClientConfig.vehicleOverlayEnabled.get()) {
            return;
        }

        if (event.phase != TickEvent.Phase.END)
            return;


        Minecraft game = Minecraft.getInstance();
        if (!game.isWindowActive() || game.options.hideGui)
            return;

        PlayerEntity player = game.player;
        if (player == null)
            return;

        Entity playerVehicle = player.getVehicle();
        if (!(playerVehicle instanceof PanthalassaVehicle))
            return;

        MatrixStack matrixStack = new MatrixStack();
        PanthalassaVehicle vehicle = (PanthalassaVehicle) playerVehicle;

        String vehicleText;
        if (vehicle instanceof VehicleMRSV) {
            vehicleText = "MANTA RAY SUBMERSIBLE VEHICLE";
            game.font.drawShadow(matrixStack, TextFormatting.BOLD + vehicleText, 150, 10, Color.WHITE.getRGB());
        }
        if (vehicle instanceof VehicleAGII) {
            vehicleText = "ABYSS GLIDER II SUBMERSIBLE VEHICLE";
            game.font.drawShadow(matrixStack, TextFormatting.BOLD + vehicleText, 135, 10, Color.WHITE.getRGB());
        }

        String vehicleIntegrity;
        if (vehicle.getMaxHealth() != 0) {
            vehicleIntegrity = new DecimalFormat("00").format((vehicle.getHealth() / vehicle.getMaxHealth()) * 100);
            if (((vehicle.getHealth()/vehicle.getMaxHealth())*100)>50){
                game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Vehicle Integrity: " + TextFormatting.GREEN + vehicleIntegrity, 10, 25, Color.WHITE.getRGB());
            } else if (((vehicle.getHealth()/vehicle.getMaxHealth())*100)>20){
                game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Vehicle Integrity: " + TextFormatting.YELLOW + vehicleIntegrity, 10, 25, Color.WHITE.getRGB());
            } else if (((vehicle.getHealth()/vehicle.getMaxHealth())*100)>0){
                game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Vehicle Integrity: " + TextFormatting.RED + vehicleIntegrity, 10, 25, Color.WHITE.getRGB());
            } else {
                game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Vehicle Integrity: " + TextFormatting.DARK_PURPLE + "processing...", 10, 25, Color.WHITE.getRGB());
            }
        }

        String depth;
        if (vehicle.level.dimension() == PanthalassaDimension.PANTHALASSA) {
            depth = new DecimalFormat("0").format(vehicle.blockPosition().getY()-20100);
        } else {
            depth = new DecimalFormat("0").format(vehicle.blockPosition().getY());
        }
        game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Depth: " + TextFormatting.YELLOW + depth, 10, 35, Color.WHITE.getRGB());


        double x = vehicle.getEntryX();
        double z = vehicle.getEntryZ();
        String entryXText = new DecimalFormat("0").format(x);
        String entryZText = new DecimalFormat("0").format(z);

        if (x != 0) {
            game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Entry X: " + TextFormatting.YELLOW + entryXText, 10, 55, Color.WHITE.getRGB());
        } else {
            game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Entry X: " + TextFormatting.YELLOW + "???", 10, 55, Color.WHITE.getRGB());
        }
        if (z != 0){
            game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Entry Z" + TextFormatting.YELLOW + entryZText, 10, 65, Color.WHITE.getRGB());
        } else {
            game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Entry Z" + TextFormatting.YELLOW + "???", 10, 65, Color.WHITE.getRGB());
        }

        double nlfDistance;
        String nlfDistanceText;
        nlfDistance = vehicle.getNLFDistance();
        nlfDistanceText = new DecimalFormat("0").format(nlfDistance);

            if (nlfDistance>10) {
                game.font.drawShadow(matrixStack, TextFormatting.AQUA + "NLF Distance: " + TextFormatting.GREEN + nlfDistanceText, 10, 220, Color.WHITE.getRGB());
            } else if (nlfDistance>5){
                game.font.drawShadow(matrixStack, TextFormatting.AQUA + "NLF Distance: " + TextFormatting.YELLOW + nlfDistanceText, 10, 220, Color.WHITE.getRGB());
            } else if (nlfDistance>=0) {
                game.font.drawShadow(matrixStack, TextFormatting.AQUA + "NLF Distance: " + TextFormatting.RED + nlfDistanceText, 10, 220, Color.WHITE.getRGB());
            } else {
                game.font.drawShadow(matrixStack, TextFormatting.AQUA + "NLF Distance: " + TextFormatting.DARK_PURPLE + "processing...", 10, 220, Color.WHITE.getRGB());
        }

        int floorDistance;
        String floorDistanceText;
        floorDistance = vehicle.getFloorDistance();
        floorDistanceText = new DecimalFormat("0").format(floorDistance);

        if (floorDistance>20) {
            game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Floor Distance: " + TextFormatting.GREEN + floorDistanceText, 10, 230, Color.WHITE.getRGB());
        } else if (floorDistance>10) {
            game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Floor Distance: " + TextFormatting.YELLOW + floorDistanceText, 10, 230, Color.WHITE.getRGB());
        } else if (floorDistance>=0) {
            game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Floor Distance: " + TextFormatting.RED + floorDistanceText, 10, 230, Color.WHITE.getRGB());
        } else {
            game.font.drawShadow(matrixStack, TextFormatting.AQUA + "Floor Distance: " + TextFormatting.DARK_PURPLE + "processing...", 10, 230, Color.WHITE.getRGB());
        }
    }
}

