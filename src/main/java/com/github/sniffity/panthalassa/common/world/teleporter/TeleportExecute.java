package com.github.sniffity.panthalassa.common.world.teleporter;

import com.github.sniffity.panthalassa.common.world.PanthalassaWorldSavedData;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

public class TeleportExecute {

    public TeleportExecute(Entity entity, ServerWorld targetWorld, ServerWorld initialWorld, PanthalassaTeleporter teleporter) {


        PortalInfo portalinfo = teleporter.getPortalInfo(entity, targetWorld);

        if (portalinfo !=null) {
            if (!entity.getPassengers().isEmpty()) {
                PanthalassaWorldSavedData.get(targetWorld).addCompoundTP(entity, targetWorld.getDimensionKey(), initialWorld.getDimensionKey(), portalinfo.pos, portalinfo.rotationYaw, portalinfo.rotationPitch);
            } else if (entity instanceof PlayerEntity) {
                PanthalassaWorldSavedData.get(targetWorld).addPlayerTP((PlayerEntity) entity,targetWorld.getDimensionKey(),portalinfo.pos, portalinfo.rotationYaw, portalinfo.rotationPitch);
            } else {
                PanthalassaWorldSavedData.get(targetWorld).addEntityTP(entity, targetWorld.getDimensionKey(), initialWorld.getDimensionKey(), portalinfo.pos, portalinfo.rotationYaw, portalinfo.rotationPitch);
            }
        }
    }

}
