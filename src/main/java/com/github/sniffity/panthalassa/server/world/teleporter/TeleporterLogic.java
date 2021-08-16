package com.github.sniffity.panthalassa.server.world.teleporter;

import com.github.sniffity.panthalassa.server.world.PanthalassaWorldSavedData;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

/**
 * Panthalassa Mod - Class: TeleporterLogic <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Atum 2, the Undergarden,
 * UltraAmplifiedDiemsnion and The Twilight Forest mods implement their own respective teleportation systems.
 */

public class TeleporterLogic {

    public TeleporterLogic(Entity entity, ServerWorld targetWorld, ServerWorld initialWorld, PanthalassaTeleporter teleporter) {

        PortalInfo portalinfo = teleporter.getPortalInfo(entity, targetWorld);

        if (portalinfo !=null) {
            if (!entity.getPassengers().isEmpty()) {
                PanthalassaWorldSavedData.get(targetWorld).addCompoundTP(entity, targetWorld.dimension(), initialWorld.dimension(), portalinfo.pos, portalinfo.yRot, portalinfo.xRot);
            } else if (entity.getVehicle() != null) {
                PanthalassaWorldSavedData.get(targetWorld).addCompoundTP(entity.getVehicle(), targetWorld.dimension(), initialWorld.dimension(), portalinfo.pos, portalinfo.yRot, portalinfo.xRot);
            } else if (entity instanceof PlayerEntity) {
                PanthalassaWorldSavedData.get(targetWorld).addPlayerTP((PlayerEntity) entity, targetWorld.dimension(), portalinfo.pos, portalinfo.yRot, portalinfo.xRot);
            } else {
                PanthalassaWorldSavedData.get(targetWorld).addEntityTP(entity, targetWorld.dimension(), initialWorld.dimension(), portalinfo.pos, portalinfo.yRot, portalinfo.xRot);
            }
        }
    }
}


