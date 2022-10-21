package com.github.sniffity.panthalassa.client.events;

import com.github.sniffity.panthalassa.server.entity.vehicle.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Panthalassa Mod - Class: CameraSetupEvent <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Wyrmroost
 * handles its mounted camera, as well as studying Vanilla Minecraft's third person
 * camera methods.
 */

public class CameraSetupEvent {

    @SubscribeEvent
    public void onCameraSetup(ViewportEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Entity vehicle = mc.player.getVehicle();
        if (!(vehicle instanceof PanthalassaVehicle))
            return;
        CameraType view = mc.options.getCameraType();
        float yCamera;

        if (vehicle.level.getBlockState(new BlockPos(vehicle.position()).above()).canOcclude()) {
            yCamera = -4.0F;
        }
        else {
            yCamera = 1.0F;
        }

        if (view == CameraType.THIRD_PERSON_BACK) {
            if (vehicle instanceof VehicleMRSV) {
                event.getCamera().move(-calcCameraDistance(8.0, vehicle), yCamera, 0);
            }
            if (vehicle instanceof VehicleAGII) {
                event.getCamera().move(-calcCameraDistance(12.0, vehicle), yCamera, 0);
            }
            if (vehicle instanceof VehiclePCSV || vehicle instanceof VehicleECSV) {
                event.getCamera().move(-calcCameraDistance(16.0, vehicle), yCamera, 0);
            }
        } else {
            if (vehicle instanceof VehicleMRSV) {
                event.getCamera().move(-calcCameraDistance(-1.5, vehicle), yCamera, 0);
            }
            if (vehicle instanceof VehicleAGII) {
                event.getCamera().move(-calcCameraDistance(-2.0, vehicle), yCamera, 0);
            }
            if (vehicle instanceof VehiclePCSV || vehicle instanceof VehicleECSV) {
                event.getCamera().move(-calcCameraDistance(-2.0, vehicle), yCamera, 0);
            }
        }
    }

    public static double calcCameraDistance(double startingDistance, Entity vehicle) {
        Camera info = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 position = info.getPosition().add(0, 1, 0);
        Vector3f view = info.getLookVector();

        for (int i = 0; i < 8; ++i) {
            float f = (float) ((i & 1) * 2 - 1);
            float f1 = (float) ((i >> 1 & 1) * 2 - 1);
            float f2 = (float) ((i >> 2 & 1) * 2 - 1);
            f = f * 0.1F;
            f1 = f1 * 0.1F;
            f2 = f2 * 0.1F;
            Vec3 vector3d = position.add((double) f, (double) f1, (double) f2);
            Vec3 vector3d1 = new Vec3(position.x - (double) view.x() * startingDistance + (double) f + (double) f2, position.y - (double) view.y() * startingDistance + (double) f1, position.z - (double) view.z() * startingDistance + (double) f2);
            HitResult raytraceresult = vehicle.level.clip(new ClipContext(vector3d, vector3d1, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, vehicle));
            if (raytraceresult.getType() != HitResult.Type.MISS) {
                double d0 = raytraceresult.getLocation().distanceTo(position);
                if (d0 < startingDistance) {
                    if (d0<0.2){
                        startingDistance = d0-3.0F;
                    } else {
                        startingDistance = d0-1.5F;
                    }
                }
            }
        }
        return startingDistance;
    }
}