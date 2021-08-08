package com.github.sniffity.panthalassa.server.entity.ai;

import java.util.EnumSet;
import javax.annotation.Nullable;

import com.github.sniffity.panthalassa.server.entity.PanthalassaEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import static java.lang.Math.*;

public class PanthalassaRandomSwimmingGoal extends Goal {

    protected final PanthalassaEntity creature;
    protected double x;
    protected double y;
    protected double z;
    protected final double speed;
    protected int executionChance;
    protected boolean mustUpdate;
    private boolean checkNoActionTime;

    public PanthalassaRandomSwimmingGoal(PanthalassaEntity creatureIn, double speedIn, int chance) {
        this(creatureIn, speedIn, chance, true);
    }

    public PanthalassaRandomSwimmingGoal(PanthalassaEntity creature, double speed, int chance, boolean checkNATime) {
        this.creature = creature;
        this.speed = speed;
        this.executionChance = chance;
        this.checkNoActionTime = checkNATime;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean shouldExecute() {
        if (this.creature.isBeingRidden()) {
            return false;
        } else {
            if (!this.mustUpdate) {
                if (this.checkNoActionTime && this.creature.getIdleTime() >= 100) {
                    return false;
                }
                if (this.creature.getRNG().nextInt(this.executionChance) != 0) {
                    return false;
                }
            }

            Vector3d vector3d = this.getPosition();
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
    protected Vector3d getPosition() {
        Vector3d travelVector = new Vector3d(this.creature.getMotion().getX(), this.creature.getMotion().getY(), this.creature.getMotion().getZ());
        Vector3d vector = vector = RandomPositionGenerator.findRandomTargetBlockTowards(this.creature,30,20,travelVector);

        for (int i = 0; vector != null && !this.creature.world.getBlockState(new BlockPos(vector)).allowsMovement(this.creature.world, new BlockPos(vector), PathType.WATER) && i++ < 15;
            vector = RandomPositionGenerator.findRandomTargetBlockTowards(this.creature,30,20,travelVector))
        {}
        if (vector != null) {
            if (!this.creature.world.getFluidState(new BlockPos(vector).up(1)).isTagged(FluidTags.WATER)) {
                vector = vector.add(0, -3, 0);
            } else if (!this.creature.world.getFluidState(new BlockPos(vector).up(2)).isTagged(FluidTags.WATER)) {
                vector = vector.add(0, -2, 0);
            } else if (!this.creature.world.getFluidState(new BlockPos(vector).down(1)).isTagged(FluidTags.WATER)) {
                vector = vector.add(0, +3, 0);
            } else if (!this.creature.world.getFluidState(new BlockPos(vector).down(2)).isTagged(FluidTags.WATER)) {
                vector = vector.add(0, +2, 0);
            }
            if (abs(vector.x - this.creature.getPosX()) < 5) {
                vector = vector.add(5, 0, 0);
            }
            if (abs(vector.z - this.creature.getPosZ()) < 5) {
                vector = vector.add(0, 0, 5);
            }
        }
        return vector;
    }

    public boolean shouldContinueExecuting() {
        return !this.creature.getNavigator().noPath() && !this.creature.isBeingRidden();
    }

    public void startExecuting() {
        this.creature.getNavigator().tryMoveToXYZ(this.x, this.y, this.z, this.speed);
    }

    public void resetTask() {
        this.creature.getNavigator().clearPath();
        super.resetTask();
    }
/*
    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int newchance) {
        this.executionChance = newchance;
    }

 */
}