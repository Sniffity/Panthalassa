package com.github.sniffity.panthalassa.server.entity.creature.ai;

import java.util.EnumSet;
import javax.annotation.Nullable;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
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
    protected int avoidDistance;

    public PanthalassaRandomSwimmingGoal(PanthalassaEntity creatureIn, double speedIn, int chance, int avoidDistance) {
        this(creatureIn, speedIn, chance, avoidDistance, true);
    }

    public PanthalassaRandomSwimmingGoal(PanthalassaEntity creature, double speed, int chance, int avoidDistance, boolean checkNATime) {
        this.creature = creature;
        this.speed = speed;
        this.executionChance = chance;
        this.checkNoActionTime = checkNATime;
        this.avoidDistance = avoidDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.creature.isVehicle()) {
            return false;
        } if (this.creature.getTarget() != null){
            return false;
        }
        else {
            if (!this.mustUpdate) {
                if (this.checkNoActionTime && this.creature.getNoActionTime() >= 100) {
                    return false;
                }
                if (this.creature.getRandom().nextInt(this.executionChance) != 0) {
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
        Vector3d travelVector = new Vector3d(this.creature.getDeltaMovement().x(), this.creature.getDeltaMovement().y(), this.creature.getDeltaMovement().z());
        Vector3d vector = vector = RandomPositionGenerator.getPosTowards(this.creature,30,20,travelVector);

        for (int i = 0; vector != null && !this.creature.level.getBlockState(new BlockPos(vector)).isPathfindable(this.creature.level, new BlockPos(vector), PathType.WATER) && i++ < 15;
            vector = RandomPositionGenerator.getPosTowards(this.creature,30,20,travelVector))
        {}
        if (vector != null) {
            for (int i = 1; i <= avoidDistance; i ++) {
                assert vector != null;
                if (this.creature.level.getBlockState(new BlockPos(vector).north(i)).canOcclude()) {
                    vector = null;
                    break;
                }

                if (this.creature.level.getBlockState(new BlockPos(vector).south(i)).canOcclude()) {
                    vector = null;
                    break;
                }
                if (this.creature.level.getBlockState(new BlockPos(vector).east(i)).canOcclude()) {
                    vector = null;
                }
                if (this.creature.level.getBlockState(new BlockPos(vector).west(i)).canOcclude()) {
                    vector = null;
                    break;
                }
            }

            assert vector != null;
            if (!this.creature.level.getFluidState(new BlockPos(vector).above(1)).is(FluidTags.WATER)) {
                vector = vector.add(0, -3, 0);
            }
            else if (!this.creature.level.getFluidState(new BlockPos(vector).above(2)).is(FluidTags.WATER)) {
                vector = vector.add(0, -2, 0);
            }
            else if (!this.creature.level.getFluidState(new BlockPos(vector).below(1)).is(FluidTags.WATER)) {
                vector = vector.add(0, +3, 0);
            }
            else if (!this.creature.level.getFluidState(new BlockPos(vector).below(2)).is(FluidTags.WATER)) {
                vector = vector.add(0, +2, 0);
            }
        }
        return vector;
    }

    @Override

    public boolean canContinueToUse() {
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