package com.github.sniffity.panthalassa.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import java.util.Collections;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import com.mojang.math.Vector3f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.AnimationUtils;

public class PanthalassaEntityRenderer<T extends Entity & IAnimatable> extends EntityRenderer<T> implements IGeoRenderer<T> {
    private final AnimatedGeoModel<T> modelProvider;

    protected PanthalassaEntityRenderer(EntityRendererProvider.Context renderManager, AnimatedGeoModel<T> modelProvider) {
        super(renderManager);
        this.modelProvider = modelProvider;
    }

    public void render(T entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelLocation(entityIn));
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees((-entityIn.yRot)+180));
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(-entityIn.xRot));
        Minecraft.getInstance().textureManager.bindForSetup(this.getTextureLocation(entityIn));
        Color renderColor = this.getRenderColor(entityIn, partialTicks, matrixStackIn, bufferIn, (VertexConsumer)null, packedLightIn);
        RenderType renderType = this.getRenderType(entityIn, partialTicks, matrixStackIn, bufferIn, (VertexConsumer)null, packedLightIn, this.getTextureLocation(entityIn));
        this.render(model, entityIn, partialTicks, renderType, matrixStackIn, bufferIn, (VertexConsumer)null, packedLightIn, getPackedOverlay(entityIn, 0.0F), (float)renderColor.getRed() / 255.0F, (float)renderColor.getGreen() / 255.0F, (float)renderColor.getBlue() / 255.0F, (float)renderColor.getAlpha() / 255.0F);
        float lastLimbDistance = 0.0F;
        float limbSwing = 0.0F;
        EntityModelData entityModelData = new EntityModelData();
        AnimationEvent<T> predicate = new AnimationEvent((IAnimatable)entityIn, limbSwing, lastLimbDistance, partialTicks, !(lastLimbDistance > -0.15F) || !(lastLimbDistance < 0.15F), Collections.singletonList(entityModelData));
        if (this.modelProvider instanceof IAnimatableModel) {
            this.modelProvider.setLivingAnimations(entityIn, this.getUniqueID(entityIn), predicate);
        }

        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public static int getPackedOverlay(Entity livingEntityIn, float uIn) {
        return OverlayTexture.pack(OverlayTexture.u(uIn), OverlayTexture.v(false));
    }

    public GeoModelProvider<T> getGeoModelProvider() {
        return this.modelProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(T instance) {
        return this.modelProvider.getTextureLocation(instance);
    }


    static {
        AnimationController.addModelFetcher((object) -> {
            return object instanceof Entity ? (IAnimatableModel)AnimationUtils.getGeoModelForEntity((Entity)object) : null;
        });
    }
    @Override
    protected int getBlockLightLevel(T entityIn, BlockPos partialTicks) {
        return 0;
    }
}
