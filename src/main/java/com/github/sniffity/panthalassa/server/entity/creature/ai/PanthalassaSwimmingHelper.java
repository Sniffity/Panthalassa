package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class PanthalassaSwimmingHelper extends MovementController {


    int blockedDistance;
    boolean flag = false;

    private final PanthalassaEntity entityPanthalassa;

    public PanthalassaSwimmingHelper(PanthalassaEntity entity, int blockDistance) {
        super(entity);
        this.entityPanthalassa = entity;
        this.blockedDistance = blockDistance;
    }

    public boolean getBlockedAbove(int distance) {
        return (!this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).above(distance)).is(FluidTags.WATER) && !this.entityPanthalassa.level.getBlockState(new BlockPos(entityPanthalassa.blockPosition().above(distance))).is(Blocks.AIR));
    }

    public boolean getBlockedNorth(int distance) {
        return !this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).north(distance)).is(FluidTags.WATER);
    }

    public boolean getBlockedSouth(int distance) {
        return !this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).south(distance)).is(FluidTags.WATER);
    }

    public boolean getBlockedEast(int distance) {
        return !this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).east(distance)).is(FluidTags.WATER);
    }

    public boolean getBlockedWest(int distance) {
        return !this.entityPanthalassa.level.getFluidState(new BlockPos(entityPanthalassa.blockPosition()).west(distance)).is(FluidTags.WATER);
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
            this.entityPanthalassa.setYya(0.00F);
            this.entityPanthalassa.setZza(0.01F);
        }

        for (int i = 1; i <= blockedDistance; i++) {
            if (this.entityPanthalassa.isInWater()) {
                if (getBlockedAbove(i)) {
                    this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, -0.05D, 0.0D));
                }

                if (getBlockedNorth(i)) {
                    this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, 0.0D, +0.05D));
                }

                if (getBlockedSouth(i)) {
                    this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(0.0D, 0.0D, -0.05D));
                }

                if (getBlockedWest(i)) {
                    this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(-0.05D, 0.0D, 0.0D));
                }

                if (getBlockedEast(i)) {
                    this.entityPanthalassa.setDeltaMovement(this.entityPanthalassa.getDeltaMovement().add(+0.05D, 0.0D, 0.0D));
                }

            }
        }
    }
}