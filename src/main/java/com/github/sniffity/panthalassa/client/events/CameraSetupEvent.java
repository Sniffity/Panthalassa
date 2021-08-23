package com.github.sniffity.panthalassa.client.events;

import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleMRSV;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehiclePCSV;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.system.CallbackI;

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
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        Minecraft mc = Minecraft.getInstance();
        Entity vehicle = mc.player.getVehicle();
        if (!(vehicle instanceof PanthalassaVehicle))
            return;
        PointOfView view = mc.options.getCameraType();

        if (view == PointOfView.THIRD_PERSON_BACK) {
            if (vehicle instanceof VehicleMRSV) {
                event.getInfo().move(-calcCameraDistance(8.0, vehicle), 1, 0);
            }
            if (vehicle instanceof VehiclePCSV) {
                event.getInfo().move(-calcCameraDistance(12.0, vehicle), 1, 0);

            }
        }
    }

    public static double calcCameraDistance(double startingDistance, Entity vehicle) {
        ActiveRenderInfo info = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vector3d position = info.getPosition();
        Vector3f view = info.getLookVector();

        for (int i = 0; i < 8; ++i) {
            float f = (float) ((i & 1) * 2 - 1);
            float f1 = (float) ((i >> 1 & 1) * 2 - 1);
            float f2 = (float) ((i >> 2 & 1) * 2 - 1);
            f = f * 0.1F;
            f1 = f1 * 0.1F;
            f2 = f2 * 0.1F;
            Vector3d vector3d = position.add((double) f, (double) f1, (double) f2);
            Vector3d vector3d1 = new Vector3d(position.x - (double) view.x() * startingDistance + (double) f + (double) f2, position.y - (double) view.y() * startingDistance + (double) f1, position.z - (double) view.z() * startingDistance + (double) f2);
            RayTraceResult raytraceresult = vehicle.level.clip(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.VISUAL, RayTraceContext.FluidMode.NONE, vehicle));
            if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
                double d0 = raytraceresult.getLocation().distanceTo(position);
                if (d0 < startingDistance) {
                    startingDistance = d0;
                }
            }
        }
        return startingDistance;
    }
}

