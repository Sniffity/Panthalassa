package com.github.sniffity.panthalassa.server.world.teleporter;

import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.server.world.PanthalassaWorldSavedData;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;

/**
 * Panthalassa Mod - Class: TeleporterLogic <br></br?>
 * <p>
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 * <p>
 * Acknowledgements: The following class was developed after studying how Atum 2, the Undergarden,
 * UltraAmplifiedDimension and The Twilight Forest mods implement their own respective teleportation systems.
 */

public class TeleporterLogic {

    public static void teleportAndCreatePortal(Entity entity, BlockPos portalBlockPos, ServerLevel targetWorld, ServerLevel initialWorld, PanthalassaTeleporter teleporter) {

        float offsetFromPortal = targetWorld.dimension().equals(PanthalassaDimension.PANTHALASSA) ? -5 : 5;
        PortalInfo portalinfo = teleporter.getPortalInfo(entity, targetWorld, portalBlockPos);

        if (portalinfo != null) {
            if (!entity.getPassengers().isEmpty()) {
                PanthalassaWorldSavedData.get(targetWorld).addCompoundTP(entity, targetWorld.dimension(), initialWorld.dimension(), portalinfo.pos.add(0, offsetFromPortal, 0), portalinfo.yRot, portalinfo.xRot);
            } else if (entity.getVehicle() != null) {
                PanthalassaWorldSavedData.get(targetWorld).addCompoundTP(entity.getVehicle(), targetWorld.dimension(), initialWorld.dimension(), portalinfo.pos.add(0, offsetFromPortal, 0), portalinfo.yRot, portalinfo.xRot);
            } else if (entity instanceof Player) {
                PanthalassaWorldSavedData.get(targetWorld).addPlayerTP((Player) entity, targetWorld.dimension(), portalinfo.pos.add(0, offsetFromPortal, 0), portalinfo.yRot, portalinfo.xRot);
            } else {
                PanthalassaWorldSavedData.get(targetWorld).addEntityTP(entity, targetWorld.dimension(), initialWorld.dimension(), portalinfo.pos.add(0, offsetFromPortal, 0), portalinfo.yRot, portalinfo.xRot);
            }
        }
    }

    public static void teleport(Entity entity, ServerLevel targetWorld, ServerLevel initialWorld, BlockPos targetPos) {
        float offsetFromPortal = targetWorld.dimension().equals(PanthalassaDimension.PANTHALASSA) ? -5 : 5;

        if (!entity.getPassengers().isEmpty()) {
            PanthalassaWorldSavedData.get(targetWorld).addCompoundTP(entity, targetWorld.dimension(), initialWorld.dimension(), new Vec3(targetPos.getX() + 0.5D, targetPos.getY() + offsetFromPortal, targetPos.getZ() + 0.5D), entity.yRot, entity.xRot);
        } else if (entity.getVehicle() != null) {
            PanthalassaWorldSavedData.get(targetWorld).addCompoundTP(entity.getVehicle(), targetWorld.dimension(), initialWorld.dimension(), new Vec3(targetPos.getX() + 0.5D, targetPos.getY() + offsetFromPortal, targetPos.getZ() + 0.5D), entity.yRot, entity.xRot);
        } else if (entity instanceof Player) {
            PanthalassaWorldSavedData.get(targetWorld).addPlayerTP((Player) entity, targetWorld.dimension(), new Vec3(targetPos.getX() + 0.5D, targetPos.getY() + offsetFromPortal, targetPos.getZ() + 0.5D), entity.yRot, entity.xRot);
        } else {
            PanthalassaWorldSavedData.get(targetWorld).addEntityTP(entity, targetWorld.dimension(), initialWorld.dimension(), new Vec3(targetPos.getX() + 0.5D, targetPos.getY() + offsetFromPortal, targetPos.getZ() + 0.5D), entity.yRot, entity.xRot);
        }
    }
}


