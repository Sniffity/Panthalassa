package com.github.sniffity.panthalassa.server.entity.ai;

import com.github.sniffity.panthalassa.server.entity.PanthalassaEntity;
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
        if (this.entityPanthalassa.isEyeInFluid(FluidTags.WATER)) {
            this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
        }

        if (this.operation == MovementController.Action.MOVE_TO && !this.entityPanthalassa.getNavigation().isDone()) {
            double d0 = this.wantedX - this.entityPanthalassa.getX();
            double d1 = this.wantedY - this.entityPanthalassa.getY();
            double d2 = this.wantedZ - this.entityPanthalassa.getZ();

            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 < (double) 2.5000003E-7F) {
                this.mob.setZza(0.0F);
            } else {
                float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                if ((this.entityPanthalassa.getTarget() != null)) {
                    this.entityPanthalassa.yRot = this.rotlerp(this.entityPanthalassa.yRot, f, 5.0F);
                } else {
                    this.entityPanthalassa.yRot = this.rotlerp(this.entityPanthalassa.yRot, f, 1.0F);
                }
                this.entityPanthalassa.yBodyRot = this.entityPanthalassa.yRot;
                this.entityPanthalassa.yHeadRot = this.entityPanthalassa.yRot;
                float f1 = (float) (this.speedModifier * this.entityPanthalassa.getAttributeValue(Attributes.MOVEMENT_SPEED));
                if (this.entityPanthalassa.isInWater()) {
                    this.entityPanthalassa.setSpeed(f1 * 0.02F);
                    float f2 = -((float) (MathHelper.atan2(d1, (double) MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double) (180F / (float) Math.PI)));
                    f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                    this.entityPanthalassa.xRot = this.rotlerp(this.entityPanthalassa.xRot, f2, 5.0F);
                    float f3 = MathHelper.cos(this.entityPanthalassa.xRot * ((float) Math.PI / 180F));
                    float f4 = MathHelper.sin(this.entityPanthalassa.xRot * ((float) Math.PI / 180F));
                    this.entityPanthalassa.zza = f3 * f1;
                    this.entityPanthalassa.yya = -f4 * f1;
                } else {
                    this.entityPanthalassa.setSpeed(f1 * 0.1F);
                }

            }

        } else {
            this.entityPanthalassa.setSpeed(1.0F);
            this.entityPanthalassa.setXxa(0.0F);
            this.entityPanthalassa.setYya(0.01F);
            this.entityPanthalassa.setZza(0.01F);
        }

        if (!this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).above(1)).is(FluidTags.WATER)) {
            this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, -0.05D, 0.0D));
        } else if (!this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).above(2)).is(FluidTags.WATER)) {
            this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, -0.05D, 0.0D));
        } else if (!this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).below(1)).is(FluidTags.WATER)) {
            this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, +0.05D, 0.0D));
        } else if (!this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).below(2)).is(FluidTags.WATER)) {
            this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, +0.05D, 0.0D));

        }
    }
}