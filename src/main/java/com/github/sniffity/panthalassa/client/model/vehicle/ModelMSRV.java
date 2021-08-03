package com.github.sniffity.panthalassa.client.model.vehicle;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.vehicle.VehicleMSRV;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelMSRV extends AnimatedGeoModel<VehicleMSRV>
{
    @Override
    public ResourceLocation getModelLocation(VehicleMSRV object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/vehicle/msrv/msrv.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(VehicleMSRV object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/msrv/msrv.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(VehicleMSRV animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/vehicle/msrv/msrv.png");
    }
}