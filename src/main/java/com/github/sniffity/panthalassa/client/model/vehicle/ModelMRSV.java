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
        switch (object.getTextureVariant()) {
            case 0:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv0.png");
            case 1:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv1.png");
            case 2:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv2.png");
            case 3:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv3.png");
            case 4:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv4.png");
            case 5:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv5.png");
            case 6:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv6.png");
            case 7:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv7.png");
            case 8:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv8.png");
            case 9:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv9.png");
            case 10:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv10.png");
            case 11:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv11.png");
            case 12:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv12.png");
            case 13:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv13.png");
            case 14:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv14.png");
            case 15:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv15.png");
        }
        return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/mrsv/mrsv11.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(VehicleMRSV animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/vehicle/mrsv/mrsv_vehicle.json");
    }
}