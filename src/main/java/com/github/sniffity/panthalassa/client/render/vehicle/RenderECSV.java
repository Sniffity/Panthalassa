package com.github.sniffity.panthalassa.client.render.vehicle;

import com.github.sniffity.panthalassa.client.model.vehicle.ModelECSV;
import com.github.sniffity.panthalassa.client.render.PanthalassaEntityRenderer;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleECSV;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;

public class RenderECSV extends PanthalassaEntityRenderer<VehicleECSV> {

    public RenderECSV(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelECSV());
        this.shadowRadius = 1.0F;
    }

    @Override
    protected int getBlockLightLevel(VehicleECSV entityIn, BlockPos partialTicks) {
        return entityIn.getLightsOn() ? 15 : 1;
    }
}