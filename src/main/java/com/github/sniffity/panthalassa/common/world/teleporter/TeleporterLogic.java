package com.github.sniffity.panthalassa.common.world.teleporter;

import com.github.sniffity.panthalassa.common.world.PanthalassaWorldSavedData;
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
                PanthalassaWorldSavedData.get(targetWorld).addCompoundTP(entity, targetWorld.getDimensionKey(), initialWorld.getDimensionKey(), portalinfo.pos, portalinfo.rotationYaw, portalinfo.rotationPitch);
            } else if (entity instanceof PlayerEntity) {
                if(entity.getRidingEntity() != null) {
                    PanthalassaWorldSavedData.get(targetWorld).addCompoundTP(entity.getRidingEntity(), targetWorld.getDimensionKey(), initialWorld.getDimensionKey(), portalinfo.pos, portalinfo.rotationYaw, portalinfo.rotationPitch);
                } else {
                    PanthalassaWorldSavedData.get(targetWorld).addPlayerTP((PlayerEntity) entity, targetWorld.getDimensionKey(), portalinfo.pos, portalinfo.rotationYaw, portalinfo.rotationPitch);
                }
            } else {
                PanthalassaWorldSavedData.get(targetWorld).addEntityTP(entity, targetWorld.getDimensionKey(), initialWorld.getDimensionKey(), portalinfo.pos, portalinfo.rotationYaw, portalinfo.rotationPitch);
            }
        }
    }

}
