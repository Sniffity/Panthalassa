package com.github.sniffity.panthalassa.client.render.projectile;

import com.github.sniffity.panthalassa.client.model.projectile.ModelTranquilizingTorpedo;
import com.github.sniffity.panthalassa.client.render.PanthalassaEntityRenderer;
import com.github.sniffity.panthalassa.server.entity.projectile.ProjectileTranquilizingTorpedo;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;

public class RenderTranquilizingTorpedo extends PanthalassaEntityRenderer<ProjectileTranquilizingTorpedo> {

    public RenderTranquilizingTorpedo(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelTranquilizingTorpedo());
        this.shadowRadius = 1.0F;
    }

    @Override
    protected int getBlockLightLevel(ProjectileTranquilizingTorpedo entityIn, BlockPos partialTicks) {
        return 15;
    }
}