package com.github.sniffity.panthalassa.client.render.vehicle;

import com.github.sniffity.panthalassa.client.model.vehicle.ModelMSRV;
import com.github.sniffity.panthalassa.client.render.PanthalassaVehicleRenderer;
import com.github.sniffity.panthalassa.vehicle.VehicleMSRV;
import net.minecraft.client.renderer.entity.EntityRendererManager;


public class RenderMSRV extends PanthalassaVehicleRenderer<VehicleMSRV> {

    public RenderMSRV(EntityRendererManager renderManager) {
        super(renderManager, new ModelMSRV());
        this.shadowSize = 1.0F;
    }
}