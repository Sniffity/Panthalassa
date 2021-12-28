package com.github.sniffity.panthalassa.client.model.vehicle;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleMRSV;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelMRSV extends AnimatedGeoModel<VehicleMRSV>
{
    @Override
    public ResourceLocation getModelLocation(VehicleMRSV object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/vehicle/mrsv/mrsv.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(VehicleMRSV object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(VehicleMRSV animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/vehicle/mrsv/mrsv.json");
    }
}