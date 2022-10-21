package com.github.sniffity.panthalassa.client.model.vehicle;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleECSV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class ModelECSV extends AnimatedGeoModel<VehicleECSV>
{
    @Override
    public ResourceLocation getModelResource(VehicleECSV object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/vehicle/ecsv/ecsv.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VehicleECSV object) {
        if (object.getTorpedoCount() > 0 && object.getTorpedoCooldown() < 0){
            return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ecsv/ecsv.png");
        } else {
            return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ecsv/ecsv0.png");
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(VehicleECSV animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/vehicle/ecsv/ecsv.json");
    }
}