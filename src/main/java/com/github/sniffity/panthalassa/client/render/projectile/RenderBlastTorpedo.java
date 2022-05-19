package com.github.sniffity.panthalassa.client.render.projectile;

import com.github.sniffity.panthalassa.client.model.projectile.ModelBlastTorpedo;
import com.github.sniffity.panthalassa.client.render.PanthalassaEntityRenderer;
import com.github.sniffity.panthalassa.server.entity.projectile.ProjectileBlastTorpedo;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;

public class RenderBlastTorpedo extends PanthalassaEntityRenderer<ProjectileBlastTorpedo> {

    public RenderBlastTorpedo(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelBlastTorpedo());
        this.shadowRadius = 1.0F;
    }

    @Override
    protected int getBlockLightLevel(ProjectileBlastTorpedo entityIn, BlockPos partialTicks) {
        return 15;
    }
}