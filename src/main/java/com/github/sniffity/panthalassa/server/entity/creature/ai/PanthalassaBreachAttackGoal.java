package com.github.sniffity.panthalassa.server.entity.creature.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

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
    private boolean step1 = false;
    private boolean step2 = false;
    private LivingEntity target;
    private BlockPos step1Pos;
    private boolean step1Done;
    private boolean step2Done;
    private Vector3d targetPosStep2;



    public PanthalassaBreachAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
        this.attacker = creature;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }


    public boolean canUse() {
        long i = this.attacker.level.getGameTime();
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
            else if (step1) {
                return false;
            }

        this.lastCanUseCheck = i;
        return true;
        }



    public boolean canContinueToUse() {
        if (target == null) {
            return false;
        }
        else if (!target.isAlive()) {
            return false;
        }
        else if (this.attacker.distanceToSqr(target.getX(), target.getY(), target.getZ()) > 20) {
            return false;
        }
        else if (!this.longMemory) {
            return !this.attacker.getNavigation().isDone();
        } else if (!this.attacker.isWithinRestriction(target.blockPosition())) {
            return false;
        } else {
            return !(target instanceof PlayerEntity) && (target.isSpectator() || ((PlayerEntity)target).isCreative());
        }
        //If STUCK, stop
    }


    public void start() {
        this.attacker.setAggressive(true);
        this.delayCounter = 0;
        this.ticksUntilNextAttack = 0;
        if (!step1Done) {
            this.moveStep1();
        }
    }

    public void moveStep1(){
        this.attacker.getLookControl().setLookAt(target.getX(), target.getY()-10, target.getZ());
        this.attacker.getNavigation().moveTo(target.getX(),(target.getY()-10),target.getZ(), this.speedTowardsTarget);
    }

    public boolean moveStep2(){
        this.attacker.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
        Vector3d targetPos = new Vector3d (this.target.getX(),this.target.getY(),this.target.getZ());
        Vector3d attackerPos = new Vector3d (this.attacker.getX(),this.attacker.getY(),this.attacker.getZ());
        Vector3d attackTrajectory = targetPos.subtract(attackerPos);
        this.attacker.setDeltaMovement(this.attacker.getDeltaMovement().add(attackTrajectory.normalize().x*0.5, attackTrajectory.normalize().y*0.5, attackTrajectory.normalize().z*0.5));
        //< attack reach
        return this.attacker.distanceToSqr(target) < 1;
        //Hit all enemies in a box around the boat
    }


    public void stop() {
        if (!EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            this.attacker.setTarget(null);
        }
        this.attacker.setAggressive(false);
        this.attacker.getNavigation().stop();
        this.step1 = false;
    }

    @Override
    public void tick() {
        if (!step1Done) {
            if ((this.attacker.distanceToSqr(target.getX(), target.getY() - 10, target.getZ())) < 3) {
                step1Done = true;
            }
        } else {
            if (moveStep2()){



            }
        }




        //Get distance to step 1 position, if too much do nothing
        //If true, call moveStep2



    }







    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= d0 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.attacker.swing(Hand.MAIN_HAND);
            this.attacker.doHurtTarget(enemy);
        }

    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = 20;
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (double)(this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth());
    }
}
