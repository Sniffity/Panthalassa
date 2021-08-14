package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.system.CallbackI;

import java.util.EnumSet;
import java.util.List;

import static java.lang.Math.PI;

public class PanthalassaBreachAttackGoal extends Goal {
    protected final CreatureEntity attacker;
    private final double speedTowardsTarget;
    private final boolean longMemory;
    private Path path2;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int delayCounter;
    private int ticksUntilNextAttack;
    private long lastCanUseCheck;
    private boolean step2 = false;
    private LivingEntity target;
    private BoatEntity boatTarget;
    private BlockPos step1Pos;
    private boolean step1Done;
    private boolean step2Done;
    private boolean step3Done;
    private boolean step4Done;
    private boolean step5Done;
    private Vector3d targetPosStep2;
    private double jumpStart;



    public PanthalassaBreachAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
        this.attacker = creature;
        this.speedTowardsTarget = speedIn;
        //TODO: Long memory?
        this.longMemory = useLongMemory;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    //TODO: Change target to BOAT itself, not PASSENGERS.
    //What happens if passengers abandon boat? Boat should still get destroyed.

    @Override
    public boolean canUse() {
            this.target = this.attacker.getTarget();
            if ((target == null)) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else if (target.getVehicle() == null) {
                return false;
            } else if (!(target.getVehicle() instanceof BoatEntity)) {
                return false;
            }
            boatTarget = (BoatEntity) target.getVehicle();
            return true;
        }



    @Override
    public boolean canContinueToUse() {
        if (boatTarget == null) {
            return false;
        }
        else if (!boatTarget.isAlive()) {
            return false;
        }
        //Change condition, distToTarget, not Sqr
        else if (this.attacker.distanceTo(boatTarget) > 40) {
            return false;
        }
        else if (!this.longMemory) {
            return !this.attacker.getNavigation().isDone();
        } else if (!this.attacker.isWithinRestriction(boatTarget.blockPosition())) {
            return false;
        }

        //If at any point, path is not travel-able, stop
        //If both step1 and step2 are done, stop

        return true;
    }


    @Override
    public void start() {
        this.attacker.setAggressive(true);
        this.delayCounter = 0;
        //TODO: Correctly configure times until next attack and delay counter
        this.ticksUntilNextAttack = 0;
        if (!step1Done) {
            this.moveStep1();
        }
        this.jumpStart = this.boatTarget.getY();
    }

    public void moveStep1() {
        this.attacker.getLookControl().setLookAt(boatTarget.getX(), boatTarget.getY() - 10, boatTarget.getZ());
        if ((this.attacker.distanceToSqr(boatTarget.getX(), boatTarget.getY() - 10, boatTarget.getZ())) > 4) {
            Vector3d strikePos = new Vector3d(this.boatTarget.getX(), this.boatTarget.getY() - 10, this.boatTarget.getZ());
            this.attacker.getNavigation().moveTo(strikePos.x,strikePos.y,strikePos.z,this.speedTowardsTarget*2);
        } else {
            step1Done = true;
        }
    }

    public boolean moveStep2(){
        this.attacker.getLookControl().setLookAt(boatTarget.getX(), boatTarget.getY(), boatTarget.getZ());
        Vector3d targetPos = new Vector3d (this.boatTarget.getX(),this.boatTarget.getY(),this.boatTarget.getZ());
        Vector3d attackerPos = new Vector3d (this.attacker.getX(),this.attacker.getY(),this.attacker.getZ());
        Vector3d trajectory = targetPos.subtract(attackerPos).normalize();
        this.attacker.setDeltaMovement(this.attacker.getDeltaMovement().add(trajectory.x, trajectory.y, trajectory.z));
        return this.attacker.distanceTo(boatTarget) < 2;
    }


    @Override
    public void stop() {
        if (!EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(boatTarget)) {
            this.attacker.setTarget(null);
        }
        this.attacker.setAggressive(false);
        this.attacker.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (!step1Done) {
            moveStep1();
        } else if (!step2Done) {
            if (moveStep2()) {
                step2Done = true;
            }
        } else if (!step3Done) {
            boatTarget.startRiding(attacker);
            this.attacker.setDeltaMovement(this.attacker.getDeltaMovement().add(0, 1.80D, 0));
            step3Done = true;
            this.attacker.xRot = (float)(this.attacker.xRot+(PI/2));
        } else if (!step4Done) {
            if (attacker.getY() - jumpStart > 5) {
                crushBoatandPassengers();
                this.attacker.xRot = (float)(this.attacker.xRot+(PI/2));
                step4Done = true;
            }
        } else if (!step5Done && !this.attacker.isInWater()) {
            this.attacker.xRot = (float) MathHelper.atan2((this.attacker.getDeltaMovement().y),MathHelper.sqrt((this.attacker.getDeltaMovement().x)*(this.attacker.getDeltaMovement().x)+(this.attacker.getDeltaMovement().z)*(this.attacker.getDeltaMovement().z)));
            if (this.attacker.isInWater()) {
                step5Done = true;
            }
        }
    }

    protected void crushBoatandPassengers() {
        List<Entity> entities = this.attacker.level.getEntities(attacker, new AxisAlignedBB(attacker.getX() - 5, attacker.getY() - 5, attacker.getZ() - 5, attacker.getX() + 5, attacker.getY() + 5, attacker.getZ() + 5));
        if (!entities.isEmpty()) {
                this.attacker.swing(Hand.MAIN_HAND);
                for (Entity entity : entities) {
                    this.attacker.doHurtTarget(entity);
                }
        }
    }

}
