package com.github.sniffity.panthalassa.client.model.vehicle;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleECSV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class ModelECSV extends AnimatedGeoModel<VehicleECSV>
{
    @Override
    public ResourceLocation getModelLocation(VehicleECSV object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/vehicle/ecsv/ecsv.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(VehicleECSV object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ecsv/ecsv.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(VehicleECSV animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/vehicle/ecsv/ecsv.json");
    }
}