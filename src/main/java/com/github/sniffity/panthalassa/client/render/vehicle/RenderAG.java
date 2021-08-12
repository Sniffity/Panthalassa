package com.github.sniffity.panthalassa.client.render.vehicle;

import com.github.sniffity.panthalassa.client.model.vehicle.ModelAG;
import com.github.sniffity.panthalassa.client.render.PanthalassaVehicleRenderer;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleAG;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.BlockPos;

public class RenderAG extends PanthalassaVehicleRenderer<VehicleAG> {

    public RenderAG(EntityRendererManager renderManager) {
        super(renderManager, new ModelAG());
        this.shadowRadius = 1.0F;
    }

    @Override
    protected int getBlockLightLevel(VehicleAG entityIn, BlockPos partialTicks) {
        return entityIn.getLightsOn() ? 15 : 1;
    }

}
