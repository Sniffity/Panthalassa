package com.github.sniffity.panthalassa.client.render.vehicle;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.client.model.vehicle.ModelMSRV;
import com.github.sniffity.panthalassa.client.render.PanthalassaVehicleRenderer;
import com.github.sniffity.panthalassa.vehicle.VehicleMSRV;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;


public class RenderMSRV extends GeoProjectilesRenderer<VehicleMSRV> {

    public RenderMSRV(EntityRendererManager renderManager) {
        super(renderManager, new ModelMSRV());
        this.shadowSize = 1.0F;
    }

    @Override
    public ResourceLocation getEntityTexture(VehicleMSRV entity) {
        return new ResourceLocation(Panthalassa.MODID,"textures/vehicle/msrv/msrv.png");    }
}