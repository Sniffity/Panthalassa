package com.github.sniffity.panthalassa.client.model.vehicle;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehiclePCSV;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelPCSV extends AnimatedGeoModel<VehiclePCSV>
{
    @Override
    public ResourceLocation getModelLocation(VehiclePCSV object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/vehicle/mrsv/mrsv.geo.json");
    }

    public ResourceLocation texture_lightsOff = new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv.png");

    public ResourceLocation texture_lightsOn = new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv_lon.png");

    @Override
    public ResourceLocation getTextureLocation(VehiclePCSV object) {
        return object.getLightsOn() ? texture_lightsOn : texture_lightsOff;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(VehiclePCSV animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/vehicle/mrsv/mrsv.png");
    }
}
