package com.github.sniffity.panthalassa.client.model.vehicle;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleAGII;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelAGII extends AnimatedGeoModel<VehicleAGII>
{
    @Override
    public ResourceLocation getModelResource(VehicleAGII object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/vehicle/ag2/ag2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VehicleAGII object) {
        switch (object.getTextureVariant()) {
            case 0:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag20.png");
            case 1:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag21.png");
            case 2:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag22.png");
            case 3:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag23.png");
            case 4:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag24.png");
            case 5:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag25.png");
            case 6:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag26.png");
            case 7:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag27.png");
            case 8:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag28.png");
            case 9:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag29.png");
            case 10:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag210.png");
            case 11:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag211.png");
            case 12:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag212.png");
            case 13:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag213.png");
            case 14:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag214.png");
            case 15:
                return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag215.png");
        }
        return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/ag2/ag22.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(VehicleAGII animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/vehicle/ag2/ag2.json");
    }
}