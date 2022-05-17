package com.github.sniffity.panthalassa.client.model.vehicle;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehiclePCSV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelPCSV extends AnimatedGeoModel<VehiclePCSV>
{
    @Override
    public ResourceLocation getModelLocation(VehiclePCSV object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/vehicle/pcsv/pcsv.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(VehiclePCSV object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/pcsv/pcsv.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(VehiclePCSV animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/vehicle/pcsv/pcsv.json");
    }
}
