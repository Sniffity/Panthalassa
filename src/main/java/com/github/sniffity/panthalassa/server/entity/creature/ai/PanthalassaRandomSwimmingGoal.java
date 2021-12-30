package com.github.sniffity.panthalassa.server.entity.creature.ai;

import java.util.EnumSet;
import java.util.Random;
import javax.annotation.Nullable;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class PanthalassaRandomSwimmingGoal extends Goal {

    protected final PanthalassaEntity creature;
    protected double x;
    protected double y;
    protected double z;
    protected final double speed;
    protected int executionChance;
    protected boolean mustUpdate;
    private boolean checkNoActionTime;
    protected final int avoidDistance;

    public PanthalassaRandomSwimmingGoal(PanthalassaEntity creatureIn, double speedIn, int chance, int avoidDistance) {
        this(creatureIn, speedIn, chance, avoidDistance, false);
    }

    public PanthalassaRandomSwimmingGoal(PanthalassaEntity creature, double speed, int chance, int avoidDistance, boolean checkNATime) {
        this.creature = creature;
        this.speed = speed;
        this.executionChance = chance;
        this.avoidDistance = avoidDistance;
        this.checkNoActionTime = checkNATime;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.creature.isVehicle()) {
            return false;
        } if (this.creature.getTarget() != null){
            return false;
        }
        if (!this.creature.isInWater()) {
            return false;
        } else {
            if (!this.mustUpdate) {
                if (this.checkNoActionTime && this.creature.getNoActionTime() >= 100) {
                    return false;
                }
                if (this.creature.getRandom().nextInt(this.executionChance) != 0) {
                    return false;
                }
            }
            Vec3 vector3d = this.getPosition();
            if (vector3d == null) {
                return false;
            } else {
                this.x = vector3d.x;
                this.y = vector3d.y;
                this.z = vector3d.z;
                this.mustUpdate = false;
                return true;
            }
        }
    }

    @Nullable
    protected Vec3 getPosition() {
        Vec3 targetVec =  BehaviorUtils.getRandomSwimmablePos(this.creature, 10, 7);

        BlockPos targetBlockPos = new BlockPos(BehaviorUtils.getRandomSwimmablePos(this.creature, 10, 7));

        for (int i = 0; targetBlockPos != null && !this.creature.level.getBlockState(new BlockPos(targetBlockPos)).isPathfindable(this.creature.level, new BlockPos(targetBlockPos), PathComputationType.WATER) && i++ < 15;
             targetBlockPos = new BlockPos(BehaviorUtils.getRandomSwimmablePos(this.creature, 10, 7)));
        {}
        if (targetBlockPos != null) {

            Vec3 creaturePos = this.creature.position();
            double distance = creaturePos.subtract(Vec3.atCenterOf(targetBlockPos)).length();

            if (distance < 7) {
                return null;
            }

            for (int i = 0; i <= avoidDistance; i++) {
                if (!this.creature.level.getFluidState(new BlockPos(targetBlockPos).north(i)).is(FluidTags.WATER)) {
                    targetBlockPos = null;
                    break;
                }
                if (!this.creature.level.getFluidState(new BlockPos(targetBlockPos).south(i)).is(FluidTags.WATER)) {
                    targetBlockPos = null;
                    break;
                }
                if (!this.creature.level.getFluidState(new BlockPos(targetBlockPos).east(i)).is(FluidTags.WATER)) {
                    targetBlockPos = null;
                    break;
                }
                if (!this.creature.level.getFluidState(new BlockPos(targetBlockPos).west(i)).is(FluidTags.WATER)) {
                    targetBlockPos = null;
                    break;
                }
            }
/*
            if (vector != null) {
                if (!this.creature.level.getFluidState(new BlockPos(vector).above(1)).is(FluidTags.WATER)) {
                    vector = vector.add(0, -3, 0);
                } else if (!this.creature.level.getFluidState(new BlockPos(vector).above(2)).is(FluidTags.WATER)) {
                    vector = vector.add(0, -2, 0);
                } else if (!this.creature.level.getFluidState(new BlockPos(vector).below(1)).is(FluidTags.WATER)) {
                    vector = vector.add(0, +3, 0);
                } else if (!this.creature.level.getFluidState(new BlockPos(vector).below(2)).is(FluidTags.WATER)) {
                    vector = vector.add(0, +2, 0);
                }
            }

 */
        }
        return Vec3.atCenterOf(targetBlockPos);
    }

    @Override
    public boolean canContinueToUse() {
        if (this.creature.distanceToSqr(this.x,this.y,this.z) < 50) {
            return false;
        }

        return !this.creature.getNavigation().isDone() && !this.creature.isVehicle();
    }
    @Override
    public void start() {
        this.creature.getNavigation().moveTo(this.x, this.y, this.z, this.speed);
    }

    @Override
    public void stop() {
        this.creature.getNavigation().stop();
        super.stop();
    }
}