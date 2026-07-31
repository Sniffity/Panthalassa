package com.github.sniffity.panthalassa.entity.creature.behaviour.goals;

import java.util.EnumSet;
import javax.annotation.Nullable;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.entity.creature.PanthalassaCreature;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class PanthalassaRandomSwimmingGoal extends RandomStrollGoal {

    protected final PathfinderMob creature;
    protected double x;
    protected double y;
    protected double z;
    protected final int radius;
    protected final int height;
    protected final int arrivalDistanceSqr;
    protected int minimumDistanceSqr;

    protected final double speed;

    protected boolean mustUpdate;

    public PanthalassaRandomSwimmingGoal(PathfinderMob creatureIn, double speedIn, int radiusIn, int heightIn, int arrivalDistanceIn) {
        this(creatureIn,speedIn,radiusIn,heightIn,arrivalDistanceIn, 0);
    }

    public PanthalassaRandomSwimmingGoal(PathfinderMob creatureIn, double speedIn, int radiusIn, int heightIn, int arrivalDistanceIn, int minimumDistanceIn) {
        super(creatureIn,speedIn);
        this.creature = creatureIn;
        this.speed = speedIn;
        this.radius = radiusIn;
        this.height = heightIn;
        this.arrivalDistanceSqr = arrivalDistanceIn * arrivalDistanceIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.minimumDistanceSqr = minimumDistanceIn * minimumDistanceIn;
    }


    @Override
    public boolean canUse() {
        if (this.creature.isVehicle()) {
            return false;
        }

        if (this.creature.getTarget() != null) {
            return false;
        }

        if (!this.creature.isInWater()) {
            return false;
        }

        BlockPos pos = this.getBlockPos();

        if (pos == null) {
            return false;
        }

        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        this.mustUpdate = false;

        return true;
    }

    @Nullable
    protected BlockPos getBlockPos() {
        Vec3 targetVec3 = BehaviorUtils.getRandomSwimmablePos(this.creature, radius, height);
        if (targetVec3 != null) {
            BlockPos targetBlockPos = BlockPos.containing(targetVec3);

            Vec3 creaturePos = this.creature.position();

            if (minimumDistanceSqr != 0) {
                if (creaturePos.distanceToSqr(targetVec3) < minimumDistanceSqr) {
                    return null;
                }
            }
            return targetBlockPos;
        }
        return null;
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }
    @Override
    public void start() {
        BlockPos target = BlockPos.containing(this.x, this.y, this.z);
        if (Panthalassa.DEBUG){
            Path path = this.creature.getNavigation().getPath();
            ((PanthalassaCreature)this.creature).setSwimTarget(target);
            ((PanthalassaCreature) this.creature).setPathNodes(path);
        }
                this.creature.getNavigation().moveTo(this.x, this.y, this.z, this.speed);
    }

    @Override
    public void stop() {
        ((PanthalassaCreature)this.creature).setSwimTarget(null);
        this.creature.getNavigation().stop();
        super.stop();
    }
}