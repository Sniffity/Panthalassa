package com.github.sniffity.panthalassa.common.entity.ai;

import com.github.sniffity.panthalassa.common.entity.PanthalassaEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class PanthalassaSwimmingHelper extends MovementController {

    private final PanthalassaEntity entityPanthalassa;

    public PanthalassaSwimmingHelper(PanthalassaEntity entity) {
        super(entity);
        this.entityPanthalassa = entity;
    }

    public void tick() {
        if (this.entityPanthalassa.areEyesInFluid(FluidTags.WATER)) {
            this.entityPanthalassa.setMotion(this.entityPanthalassa.getMotion().add(0.0D, 0.005D, 0.0D));
        }

        if (this.action == MovementController.Action.MOVE_TO && !this.entityPanthalassa.getNavigator().noPath()) {
            double d0 = this.posX - this.entityPanthalassa.getPosX();
            double d1 = this.posY - this.entityPanthalassa.getPosY();
            double d2 = this.posZ - this.entityPanthalassa.getPosZ();

            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 < (double) 2.5000003E-7F) {
                this.mob.setMoveForward(0.0F);
            } else {
                float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                if ((this.entityPanthalassa.getAttackTarget() != null)) {
                    this.entityPanthalassa.rotationYaw = this.limitAngle(this.entityPanthalassa.rotationYaw, f, 5.0F);
                } else {
                    this.entityPanthalassa.rotationYaw = this.limitAngle(this.entityPanthalassa.rotationYaw, f, 1.0F);
                }
                this.entityPanthalassa.renderYawOffset = this.entityPanthalassa.rotationYaw;
                this.entityPanthalassa.rotationYawHead = this.entityPanthalassa.rotationYaw;
                float f1 = (float) (this.speed * this.entityPanthalassa.getAttributeValue(Attributes.MOVEMENT_SPEED));
                if (this.entityPanthalassa.isInWater()) {
                    this.entityPanthalassa.setAIMoveSpeed(f1 * 0.02F);
                    float f2 = -((float) (MathHelper.atan2(d1, (double) MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double) (180F / (float) Math.PI)));
                    f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                    this.entityPanthalassa.rotationPitch = this.limitAngle(this.entityPanthalassa.rotationPitch, f2, 5.0F);
                    float f3 = MathHelper.cos(this.entityPanthalassa.rotationPitch * ((float) Math.PI / 180F));
                    float f4 = MathHelper.sin(this.entityPanthalassa.rotationPitch * ((float) Math.PI / 180F));
                    this.entityPanthalassa.moveForward = f3 * f1;
                    this.entityPanthalassa.moveVertical = -f4 * f1;
                } else {
                    this.entityPanthalassa.setAIMoveSpeed(f1 * 0.1F);
                }

            }

        } else {
            this.entityPanthalassa.setAIMoveSpeed(1.0F);
            this.entityPanthalassa.setMoveStrafing(0.0F);
            this.entityPanthalassa.setMoveVertical(0.01F);
            this.entityPanthalassa.setMoveForward(0.01F);
        }

        if (!this.entityPanthalassa.world.getFluidState(new BlockPos(entityPanthalassa.getPosition()).up(1)).isTagged(FluidTags.WATER)) {
            this.entityPanthalassa.setMotion(this.entityPanthalassa.getMotion().add(0.0D, -0.05D, 0.0D));
        } else if (!this.entityPanthalassa.world.getFluidState(new BlockPos(entityPanthalassa.getPosition()).up(2)).isTagged(FluidTags.WATER)) {
            this.entityPanthalassa.setMotion(this.entityPanthalassa.getMotion().add(0.0D, -0.05D, 0.0D));
        } else if (!this.entityPanthalassa.world.getFluidState(new BlockPos(entityPanthalassa.getPosition()).down(1)).isTagged(FluidTags.WATER)) {
            this.entityPanthalassa.setMotion(this.entityPanthalassa.getMotion().add(0.0D, +0.05D, 0.0D));
        } else if (!this.entityPanthalassa.world.getFluidState(new BlockPos(entityPanthalassa.getPosition()).down(2)).isTagged(FluidTags.WATER)) {
            this.entityPanthalassa.setMotion(this.entityPanthalassa.getMotion().add(0.0D, +0.05D, 0.0D));

        }
    }
}