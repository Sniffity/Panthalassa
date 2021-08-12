package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class PanthalassaSwimmingHelper extends MovementController {

    boolean blockedNorth = false;
    boolean blockedSouth = false;
    boolean blockedEast = false;
    boolean blockedWest = false;
    boolean blockedAbove = false;
    boolean blockedBelow = false;

    private final PanthalassaEntity entityPanthalassa;

    public PanthalassaSwimmingHelper(PanthalassaEntity entity) {
        super(entity);
        this.entityPanthalassa = entity;
    }

    public void tick() {
        if (this.entityPanthalassa.isEyeInFluid(FluidTags.WATER)) {
            this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
        }

        if (!this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).above(1)).is(FluidTags.WATER) || !this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).above(2)).is(FluidTags.WATER)) {
            blockedAbove = true;
        }

        if (!this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).below(1)).is(FluidTags.WATER) || !this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).below(2)).is(FluidTags.WATER)) {
            blockedBelow = true;
        }

        if (!this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).north(1)).is(FluidTags.WATER) || !this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).north(2)).is(FluidTags.WATER)) {
            blockedNorth = true;
        }

        if (!this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).south(1)).is(FluidTags.WATER) || !this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).south(2)).is(FluidTags.WATER)) {
            blockedSouth = true;
        }

        if (!this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).east(1)).is(FluidTags.WATER) || !this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).east(2)).is(FluidTags.WATER)) {
            blockedEast = true;
        }

        if (!this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).west(1)).is(FluidTags.WATER) || !this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).west(2)).is(FluidTags.WATER)) {
            blockedWest = true;
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

        } else if(!blockedAbove && !blockedBelow && !blockedEast && !blockedWest && !blockedNorth && !blockedSouth ) {
            this.entityPanthalassa.setSpeed(1.0F);
            this.entityPanthalassa.setXxa(0.0F);
            this.entityPanthalassa.setYya(0.01F);
            this.entityPanthalassa.setZza(0.01F);
        }

        if (this.entityPanthalassa.isInWater()) {
            if (blockedAbove){
                this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, -0.05D, 0.0D));
            }

            if (blockedBelow) {
                this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, +0.05D, 0.0D));
            }

            if (blockedNorth) {
                this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, 0, 0.01D));

            }

            if (blockedSouth) {
                this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, 0, -0.01D));
            }

            if (blockedEast) {
                this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(-0.01D, 0, 0));
            }

            if (blockedWest) {
                this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(+0.01D, 0, 0));
            }
        }
    }
}