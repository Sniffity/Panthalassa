package com.github.sniffity.panthalassa.common.entity.ai;

import java.util.EnumSet;
import javax.annotation.Nullable;

import com.github.sniffity.panthalassa.common.entity.PanthalassaEntity;
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
            System.out.println("Execution BLOCKED. condition 1");

            return false;
        } else {
            System.out.println("Checking for execution...");
            System.out.println("mustUpdate");
            System.out.println(this.mustUpdate);

            if (!this.mustUpdate) {
                if (this.checkNoActionTime && this.creature.getIdleTime() >= 100) {
                    System.out.println("Execution BLOCKED. condition 2");
                    System.out.println("IdleTime");
                    System.out.println(this.creature.getIdleTime());
                    System.out.println("NoActionTime");
                    System.out.println(this.checkNoActionTime);
                    return false;
                }
                if (this.creature.getRNG().nextInt(this.executionChance) != 0) {
                    System.out.println("Execution BLOCKED. condition 3");
                    return false;
                }
            }

            Vector3d vector3d = this.getPosition();
            if (vector3d == null) {
                System.out.println("Vector was NULL");
                System.out.println("Flagged?");
                //System.out.println(rotationFlagged);
                return false;
            } else {
                this.x = vector3d.x;
                this.y = vector3d.y;
                this.z = vector3d.z;
                this.mustUpdate = false;
                System.out.println("Execution APPROVED");
                System.out.println("Angle of Exeuction:");
                System.out.println(vectorAngle);
                return true;
            }
        }
    }

    @Nullable
    protected Vector3d getPosition() {
        Vector3d travelVector = new Vector3d(this.creature.getMotion().getX(), this.creature.getMotion().getY(), this.creature.getMotion().getZ());
        Vector3d vector = vector = RandomPositionGenerator.findRandomTargetBlockTowards(this.creature,30,20,travelVector);
        System.out.println("Running get position");
        System.out.println("Vector:");
        System.out.println(vector);
        System.out.println("Creature Position:");
        System.out.println(this.creature.getPosX());
        System.out.println(this.creature.getPosY());
        System.out.println(this.creature.getPosZ());


        for (int i = 0; vector != null && !this.creature.world.getBlockState(new BlockPos(vector)).allowsMovement(this.creature.world, new BlockPos(vector), PathType.WATER) && i++ < 15;
            vector = RandomPositionGenerator.findRandomTargetBlockTowards(this.creature,30,20,travelVector))
        {
            System.out.println("Printing loop:");
            System.out.println(i);
        }
        if (vector != null) {
            System.out.println("CHECKING FOR IF CONDITIONS");

            if (!this.creature.world.getFluidState(new BlockPos(vector).up(1)).isTagged(FluidTags.WATER)) {
                vector = vector.add(0, -3, 0);
                System.out.println("IF 3 CONDITION IS MET, ADJUSTING VECTOR");
            } else if (!this.creature.world.getFluidState(new BlockPos(vector).up(2)).isTagged(FluidTags.WATER)) {
                vector = vector.add(0, -2, 0);
                System.out.println("IF 4 CONDITION IS MET, ADJUSTING VECTOR");
            } else if (!this.creature.world.getFluidState(new BlockPos(vector).down(1)).isTagged(FluidTags.WATER)) {
                vector = vector.add(0, +3, 0);
                System.out.println("IF 5 CONDITION IS MET, ADJUSTING VECTOR");
            } else if (!this.creature.world.getFluidState(new BlockPos(vector).down(2)).isTagged(FluidTags.WATER)) {
                vector = vector.add(0, +2, 0);
                System.out.println("IF 6 CONDITION IS MET, ADJUSTING VECTOR");
            }
            if (abs(vector.x - this.creature.getPosX()) < 5) {
                vector = vector.add(5, 0, 0);
                System.out.println("IF 1 CONDITION IS MET, ADJUSTING VECTOR");
            }
            if (abs(vector.z - this.creature.getPosZ()) < 5) {
                vector = vector.add(0, 0, 5);
                System.out.println("IF  2 CONDITION IS MET, ADJUSTING VECTOR");
            }

/*
            System.out.println("vector");
            System.out.println(vector);
            System.out.println("Creature Position X");
            System.out.println(this.creature.getPosX());
            System.out.println("Creature Position Y");
            System.out.println(this.creature.getPosY());
            System.out.println("Creature Position Z");
            System.out.println(this.creature.getPosZ());

            Vector3d destinationVector = new Vector3d(vector.getX() - this.creature.getPosX(),vector.getY() - this.creature.getPosY(),vector.getZ()-this.creature.getPosZ());
            Vector3d currentTravelVector = new Vector3d(this.creature.getMotion().getX(), this.creature.getMotion().getY(), this.creature.getMotion().getZ());
            Vector3d test = new Vector3d(this.creature.getPosX()-this.creature.lastTickPosX,this.creature.getPosY()-this.creature.lastTickPosY,this.creature.getPosZ()-this.creature.lastTickPosZ);


            System.out.println("testVector");
            System.out.println(test);

            System.out.println("destinationVector");
            System.out.println(destinationVector);

            System.out.println("currentTravelVector");
            System.out.println(currentTravelVector);


            System.out.println("Vector Operations Started");
            Double dotProductX = currentTravelVector.getX() * destinationVector.getX();
            Double dotProductY = currentTravelVector.getY() * destinationVector.getY();
            Double dotProductZ = currentTravelVector.getZ() * destinationVector.getZ();
            Double dotProduct = dotProductX + dotProductY + dotProductZ;
            Double destinationVectorLength = sqrt(
                    destinationVector.getX() * destinationVector.getX()
                    +destinationVector.getY() * destinationVector.getY()
                    +destinationVector.getZ() * destinationVector.getZ());
            System.out.println("destinationVector Length");
            System.out.println(destinationVectorLength);
            Double currentTravelVectorLength = sqrt(
                    currentTravelVector.getX() * currentTravelVector.getX()
                    +currentTravelVector.getY()* currentTravelVector.getY()
                    + currentTravelVector.getZ()* currentTravelVector.getZ());

            System.out.println("currentTravelVector Length");
            System.out.println(currentTravelVectorLength);
            System.out.println("dotProduct");
            System.out.println(dotProduct);
            System.out.println("cosine of angle");
            System.out.println((dotProduct) /(destinationVectorLength*currentTravelVectorLength));
            System.out.println("angle");
            System.out.println(acos((dotProduct) / (destinationVectorLength*currentTravelVectorLength)));
            System.out.println("angle toDegrees");
            System.out.println(toDegrees(acos((dotProduct) / (destinationVectorLength*currentTravelVectorLength))));
            System.out.println("abs angle toDegrees");
            System.out.println(toDegrees(acos((dotProduct) / (destinationVectorLength*currentTravelVectorLength))));

            vectorAngle = abs(toDegrees(acos((dotProduct) / (destinationVectorLength*currentTravelVectorLength))));

            System.out.println("currentTravelVector Length");
            System.out.println(currentTravelVectorLength);

            System.out.println("ANGLE BETWEEN VECTORS");
            System.out.println(vectorAngle);
*/

            /*
            if(vectorAngle>60){
                System.out.println("ANGLE TOO LARGE, FLAGGING");
                rotationFlagged = true;
            } else {
                System.out.println("ANGLE APPROVED");
                rotationFlagged = false;
            }
        }

        if (rotationFlagged) {
            vector = null;
        }
        */

/*
        System.out.println("***RETURN VECTOR***");
        System.out.println(vector);
        System.out.println("***RETURN ANGLE***");
        System.out.println(vectorAngle);
        */
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

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int newchance) {
        this.executionChance = newchance;
    }
}