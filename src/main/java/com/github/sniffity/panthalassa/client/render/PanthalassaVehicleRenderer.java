package com.github.sniffity.panthalassa.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import java.awt.Color;
import java.util.Collections;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.AnimationUtils;

public class PanthalassaVehicleRenderer<T extends Entity & IAnimatable> extends EntityRenderer<T> implements IGeoRenderer<T> {
    private final AnimatedGeoModel<T> modelProvider;

    protected PanthalassaVehicleRenderer(EntityRendererManager renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager);
        this.modelProvider = modelProvider;
    }

    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelLocation(entityIn));
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees((-entityIn.rotationYaw)+180));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch)));
        Minecraft.getInstance().textureManager.bindTexture(this.getEntityTexture(entityIn));
        Color renderColor = this.getRenderColor(entityIn, partialTicks, matrixStackIn, bufferIn, (IVertexBuilder)null, packedLightIn);
        RenderType renderType = this.getRenderType(entityIn, partialTicks, matrixStackIn, bufferIn, (IVertexBuilder)null, packedLightIn, this.getEntityTexture(entityIn));
        this.render(model, entityIn, partialTicks, renderType, matrixStackIn, bufferIn, (IVertexBuilder)null, packedLightIn, getPackedOverlay(entityIn, 0.0F), (float)renderColor.getRed() / 255.0F, (float)renderColor.getGreen() / 255.0F, (float)renderColor.getBlue() / 255.0F, (float)renderColor.getAlpha() / 255.0F);
        float lastLimbDistance = 0.0F;
        float limbSwing = 0.0F;
        EntityModelData entityModelData = new EntityModelData();
        AnimationEvent<T> predicate = new AnimationEvent((IAnimatable)entityIn, limbSwing, lastLimbDistance, partialTicks, !(lastLimbDistance > -0.15F) || !(lastLimbDistance < 0.15F), Collections.singletonList(entityModelData));
        if (this.modelProvider instanceof IAnimatableModel) {
            this.modelProvider.setLivingAnimations(entityIn, this.getUniqueID(entityIn), predicate);
        }

        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public static int getPackedOverlay(Entity livingEntityIn, float uIn) {
        return OverlayTexture.getPackedUV(OverlayTexture.getU(uIn), OverlayTexture.getV(false));
    }

    public GeoModelProvider<T> getGeoModelProvider() {
        return this.modelProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(T instance) {
        return this.modelProvider.getTextureLocation(instance);
    }

    public ResourceLocation getEntityTexture(T instance) {
        return this.modelProvider.getTextureLocation(instance);
    }

    static {
        AnimationController.addModelFetcher((object) -> {
            return object instanceof Entity ? (IAnimatableModel)AnimationUtils.getGeoModelForEntity((Entity)object) : null;
        });
    }
}
