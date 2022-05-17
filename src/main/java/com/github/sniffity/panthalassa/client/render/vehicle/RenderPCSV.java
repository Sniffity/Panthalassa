package com.github.sniffity.panthalassa.client.render.vehicle;

import com.github.sniffity.panthalassa.client.model.vehicle.ModelPCSV;
import com.github.sniffity.panthalassa.client.render.PanthalassaVehicleRenderer;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehiclePCSV;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;


public class RenderPCSV extends PanthalassaVehicleRenderer<VehiclePCSV> {

    public RenderPCSV(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelPCSV());
        this.shadowRadius = 1.0F;
    }

    @Override
    protected int getBlockLightLevel(VehiclePCSV entityIn, BlockPos partialTicks) {
        return entityIn.getLightsOn() ? 15 : 1;
    }
}