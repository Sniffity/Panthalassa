package com.github.sniffity.panthalassa.client.render.projectile;

import com.github.sniffity.panthalassa.client.model.torpedo.ModelTorpedo;
import com.github.sniffity.panthalassa.client.render.PanthalassaEntityRenderer;
import com.github.sniffity.panthalassa.server.entity.projectile.ProjectileTorpedo;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class RenderTorpedo extends PanthalassaEntityRenderer<ProjectileTorpedo> {

    public RenderTorpedo(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelTorpedo());
        this.shadowRadius = 1.0F;
    }
}