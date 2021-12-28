package com.github.sniffity.panthalassa.client.model.vehicle;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleAGII;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelAGII extends AnimatedGeoModel<VehicleAGII>
{
    @Override
    public ResourceLocation getModelLocation(VehicleAGII object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/vehicle/ag2/ag2.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(VehicleAGII object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag2.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(VehicleAGII animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/vehicle/ag2/ag2.json");
    }
}
