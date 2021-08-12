package com.github.sniffity.panthalassa.client.render.vehicle;

import com.github.sniffity.panthalassa.client.model.vehicle.ModelMRSV;
import com.github.sniffity.panthalassa.client.render.PanthalassaVehicleRenderer;
import com.github.sniffity.panthalassa.server.entity.vehicle.VehicleMRSV;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.BlockPos;


public class RenderMRSV extends PanthalassaVehicleRenderer<VehicleMRSV>{

        public RenderMRSV(EntityRendererManager renderManager) {
            super(renderManager, new ModelMRSV());
            this.shadowRadius = 1.0F;
        }

        @Override
        protected int getBlockLightLevel(VehicleMRSV entityIn, BlockPos partialTicks) {
        return entityIn.getLightsOn() ? 15 : 1;
    }

} 