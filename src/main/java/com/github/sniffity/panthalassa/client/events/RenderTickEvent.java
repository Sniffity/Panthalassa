package com.github.sniffity.panthalassa.client.events;

import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.server.entity.vehicle.*;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.ChatFormatting;
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

        Player player = game.player;
        if (player == null)
            return;

        Entity playerVehicle = player.getVehicle();
        if (!(playerVehicle instanceof PanthalassaVehicle))
            return;

        PoseStack matrixStack = new PoseStack();
        PanthalassaVehicle vehicle = (PanthalassaVehicle) playerVehicle;

        String vehicleText;
        if (vehicle instanceof VehicleMRSV) {
            vehicleText = "MANTA RAY SUBMERSIBLE VEHICLE";
            game.font.drawShadow(matrixStack, ChatFormatting.BOLD + vehicleText, 135, 10, Color.WHITE.getRGB());
            float boostCooldown = ((VehicleMRSV)vehicle).getBoostCooldown();
            String boostCooldownText;

            if (boostCooldown < 0) {
                boostCooldownText = "BOOST READY";
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Boost Cooldown: " + ChatFormatting.GREEN + boostCooldownText, 10, 85, Color.WHITE.getRGB());

            } else {
                boostCooldownText = new DecimalFormat("00").format(boostCooldown);
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Boost Cooldown: " + ChatFormatting.YELLOW + boostCooldownText, 10, 85, Color.WHITE.getRGB());
            }

        }
        if (vehicle instanceof VehicleAGII) {
            vehicleText = "ABYSS GLIDER II SUBMERSIBLE VEHICLE";
            game.font.drawShadow(matrixStack, ChatFormatting.BOLD + vehicleText, 135, 10, Color.WHITE.getRGB());
        }
        if (vehicle instanceof VehiclePCSV) {
            vehicleText = "PROTEUS-CLASS SUBMERSIBLE VEHICLE";
            game.font.drawShadow(matrixStack, ChatFormatting.BOLD + vehicleText, 135, 10, Color.WHITE.getRGB());
            int torpedoCount = ((VehiclePCSV)vehicle).getTorpedoCount();
            String torpedoCountText;
            if (torpedoCount <= 0) {
                torpedoCountText = "EMPTY";
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Torpedo Count: " + ChatFormatting.RED + torpedoCountText, 10, 85, Color.WHITE.getRGB());
            } else {
                torpedoCountText = new DecimalFormat("00").format(torpedoCount);
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Torpedo Count: " + ChatFormatting.YELLOW + torpedoCountText, 10, 85, Color.WHITE.getRGB());
            }

            float torpedoCooldown = ((VehiclePCSV)vehicle).getTorpedoCooldown();
            String torpedoCooldownText;
            if (torpedoCount > 0) {
                if (torpedoCooldown < 0) {
                    torpedoCooldownText = "TORPEDO READY";
                    game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Torpedo Cooldown: " + ChatFormatting.GREEN + torpedoCooldownText, 10, 95, Color.WHITE.getRGB());

                } else {
                    torpedoCooldownText = new DecimalFormat("00").format(torpedoCooldown);
                    game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Torpedo Cooldown: " + ChatFormatting.YELLOW + torpedoCooldownText, 10, 95, Color.WHITE.getRGB());
                }
            } else {
                torpedoCooldownText = "EMPTY";
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Torpedo Cooldown: " + ChatFormatting.RED + torpedoCooldownText, 10, 95, Color.WHITE.getRGB());
            }
        }
        if (vehicle instanceof VehicleECSV) {
            vehicleText = "EPIMETHEUS-CLASS SUBMERSIBLE VEHICLE";
            game.font.drawShadow(matrixStack, ChatFormatting.BOLD + vehicleText, 135, 10, Color.WHITE.getRGB());
            int torpedoCount = ((VehicleECSV)vehicle).getTorpedoCount();
            String torpedoCountText;
            if (torpedoCount <= 0) {
                torpedoCountText = "EMPTY";
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Torpedo Count: " + ChatFormatting.RED + torpedoCountText, 10, 85, Color.WHITE.getRGB());
            } else {
                torpedoCountText = new DecimalFormat("00").format(torpedoCount);
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Torpedo Count: " + ChatFormatting.YELLOW + torpedoCountText, 10, 85, Color.WHITE.getRGB());
            }

            float torpedoCooldown = ((VehicleECSV)vehicle).getTorpedoCooldown();
            String torpedoCooldownText;
            if (torpedoCount > 0) {
                if (torpedoCooldown < 0) {
                    torpedoCooldownText = "TORPEDO READY";
                    game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Torpedo Cooldown: " + ChatFormatting.GREEN + torpedoCooldownText, 10, 95, Color.WHITE.getRGB());

                } else {
                    torpedoCooldownText = new DecimalFormat("00").format(torpedoCooldown);
                    game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Torpedo Cooldown: " + ChatFormatting.YELLOW + torpedoCooldownText, 10, 95, Color.WHITE.getRGB());
                }
            } else {
                torpedoCooldownText = "EMPTY";
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Torpedo Cooldown: " + ChatFormatting.RED + torpedoCooldownText, 10, 95, Color.WHITE.getRGB());
            }
        }

        String vehicleIntegrity;
        if (vehicle.getMaxHealth() != 0) {
            vehicleIntegrity = new DecimalFormat("00").format((vehicle.getHealth() / vehicle.getMaxHealth()) * 100);
            if (((vehicle.getHealth()/vehicle.getMaxHealth())*100)>50){
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Vehicle Integrity: " + ChatFormatting.GREEN + vehicleIntegrity + ChatFormatting.GREEN + "%", 10, 25, Color.WHITE.getRGB());
            } else if (((vehicle.getHealth()/vehicle.getMaxHealth())*100)>20){
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Vehicle Integrity: " + ChatFormatting.YELLOW + vehicleIntegrity + ChatFormatting.YELLOW + "%", 10, 25, Color.WHITE.getRGB());
            } else if (((vehicle.getHealth()/vehicle.getMaxHealth())*100)>0){
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Vehicle Integrity: " + ChatFormatting.RED + vehicleIntegrity + ChatFormatting.RED + "%", 10, 25, Color.WHITE.getRGB());
            } else {
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Vehicle Integrity: " + ChatFormatting.DARK_PURPLE + "processing...", 10, 25, Color.WHITE.getRGB());
            }
        }

        String depth;
        if (vehicle.level.dimension() == PanthalassaDimension.PANTHALASSA) {
            depth = new DecimalFormat("0").format(vehicle.blockPosition().getY()-20100);
        } else {
            depth = new DecimalFormat("0").format(vehicle.blockPosition().getY());
        }
        game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Depth: " + ChatFormatting.YELLOW + depth, 10, 35, Color.WHITE.getRGB());


        double x = vehicle.getEntryX();
        double z = vehicle.getEntryZ();
        String entryXText = new DecimalFormat("0").format(x);
        String entryZText = new DecimalFormat("0").format(z);

        if (x != 0) {
            game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Entry X: " + ChatFormatting.YELLOW + entryXText, 10, 55, Color.WHITE.getRGB());
        } else {
            game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Entry X: " + ChatFormatting.YELLOW + "???", 10, 55, Color.WHITE.getRGB());
        }
        if (z != 0){
            game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Entry Z: " + ChatFormatting.YELLOW + entryZText, 10, 65, Color.WHITE.getRGB());
        } else {
            game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Entry Z: " + ChatFormatting.YELLOW + "???", 10, 65, Color.WHITE.getRGB());
        }

        float sonarCooldown;
        String sonarCooldownText;
        sonarCooldown = vehicle.getSonarCooldown();
        sonarCooldownText = new DecimalFormat ("00").format(sonarCooldown);
        if (sonarCooldown<0) {
            sonarCooldownText = "SONAR READY";
            game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Sonar Cooldown: " + ChatFormatting.YELLOW + sonarCooldownText, 10, 210, Color.WHITE.getRGB());
        } else {
            game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Sonar Cooldown: " + ChatFormatting.YELLOW + sonarCooldownText, 10, 210, Color.WHITE.getRGB());
        }

        double nlfDistance;
        String nlfDistanceText;
        nlfDistance = vehicle.getNLFDistance();
        nlfDistanceText = new DecimalFormat("0").format(nlfDistance);

            if (nlfDistance>10) {
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "NLF Distance: " + ChatFormatting.GREEN + nlfDistanceText, 10, 220, Color.WHITE.getRGB());
            } else if (nlfDistance>5){
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "NLF Distance: " + ChatFormatting.YELLOW + nlfDistanceText, 10, 220, Color.WHITE.getRGB());
            } else if (nlfDistance>=0) {
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "NLF Distance: " + ChatFormatting.RED + nlfDistanceText, 10, 220, Color.WHITE.getRGB());
            } else {
                game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "NLF Distance: " + ChatFormatting.DARK_PURPLE + "processing...", 10, 220, Color.WHITE.getRGB());
        }

        int floorDistance;
        String floorDistanceText;
        floorDistance = vehicle.getFloorDistance();
        floorDistanceText = new DecimalFormat("0").format(floorDistance);

        if (floorDistance>20) {
            game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Floor Distance: " + ChatFormatting.GREEN + floorDistanceText, 10, 230, Color.WHITE.getRGB());
        } else if (floorDistance>10) {
            game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Floor Distance: " + ChatFormatting.YELLOW + floorDistanceText, 10, 230, Color.WHITE.getRGB());
        } else if (floorDistance>=0) {
            game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Floor Distance: " + ChatFormatting.RED + floorDistanceText, 10, 230, Color.WHITE.getRGB());
        } else {
            game.font.drawShadow(matrixStack, ChatFormatting.AQUA + "Floor Distance: " + ChatFormatting.DARK_PURPLE + "processing...", 10, 230, Color.WHITE.getRGB());
        }
    }
}

