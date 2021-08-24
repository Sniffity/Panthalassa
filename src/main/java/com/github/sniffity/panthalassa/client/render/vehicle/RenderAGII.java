package com.github.sniffity.panthalassa.client.render.vehicle;

import com.github.sniffity.panthalassa.client.model.vehicle.ModelAGII;
import com.github.sniffity.panthalassa.client.render.PanthalassaVehicleRenderer;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleAGII;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.BlockPos;

public class RenderAGII extends PanthalassaVehicleRenderer<VehicleAGII> {

    public RenderAGII(EntityRendererManager renderManager) {
        super(renderManager, new ModelAGII());
        this.shadowRadius = 1.0F;
    }

    @Override
    protected int getBlockLightLevel(VehicleAGII entityIn, BlockPos partialTicks) {
        return entityIn.getLightsOn() ? 15 : 1;
    }

}
