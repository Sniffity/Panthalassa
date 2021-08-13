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
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.system.CallbackI;

import java.util.EnumSet;
import java.util.List;

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
        //Change condition, distToTarget, not Sqr
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

        //If at any point, path is not travel-able, stop
        //If both step1 and step2 are done, stop
    }


    public void start() {
        this.attacker.setAggressive(true);
        this.delayCounter = 0;
        this.ticksUntilNextAttack = 0;
        if (!step1Done) {
            this.moveStep1();
        }
    }

    public void moveStep1() {
        this.attacker.getLookControl().setLookAt(target.getX(), target.getY() - 10, target.getZ());
        if ((this.attacker.distanceToSqr(target.getX(), target.getY() - 10, target.getZ())) > 3) {
            Vector3d strikePos = new Vector3d(this.target.getX(), this.target.getY() - 10, this.target.getZ());
            this.attacker.getNavigation().moveTo(strikePos.x,strikePos.y,strikePos.z,this.speedTowardsTarget);
        } else {
            step1Done = true;
        }
    }

    public boolean moveStep2(){
        this.attacker.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
        Vector3d targetPos = new Vector3d (this.target.getX()-2,this.target.getY(),this.target.getZ()-2);
        Vector3d attackerPos = new Vector3d (this.attacker.getX(),this.attacker.getY(),this.attacker.getZ());
        Vector3d attackTrajectory = targetPos.subtract(attackerPos);
        //Change to moveTo
        //step2Done flag
        this.attacker.setDeltaMovement(this.attacker.getDeltaMovement().add(attackTrajectory.normalize().x*0.5, attackTrajectory.normalize().y*0.5, attackTrajectory.normalize().z*0.5));
        return this.attacker.distanceTo(target) < 3;
    }


    public void stop() {
        if (!EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            this.attacker.setTarget(null);
        }
        this.attacker.setAggressive(false);
        this.attacker.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (!step1Done) {
            moveStep1();
        } else {
            //Make step2 flag, similar to step1. If step 2 has already been performed, do not perform again.
            if (moveStep2()){
                //Change back to distSqr? Condition on step 2 was what was failing
                double d0 = this.attacker.distanceTo(target);
                checkAndPerformAreaAttack(target, d0);
                this.attacker.setDeltaMovement(this.attacker.getDeltaMovement().add(0, 5.0D, 0));
            }
        }
    }


    protected void checkAndPerformAreaAttack(LivingEntity enemy, double distToTarget) {
        double d0 = this.getAttackReachSqr  (enemy);
        //Verify that calling this method with distSqr instead of dist actually works
        if (distToTarget <= d0 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            List<Entity> entities = this.attacker.level.getEntities(target, new AxisAlignedBB(target.getX() - 5, target.getY() - 5, target.getZ() - 5, target.getX() + 5, target.getY() + 5, target.getZ() + 5));
            if (!entities.isEmpty()) {
                this.attacker.swing(Hand.MAIN_HAND);
                for (Entity entity : entities) {
                    if (entity != attacker) {
                        //Verify correctly hurting all entities inside the bounding box.
                        //Second passenger not getting hit
                        this.attacker.doHurtTarget(entity);
                    }
                }
            }
        }
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = 20;
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (this.attacker.getBbWidth() * 2.0F * this.attacker.getBbWidth() * 2.0F + attackTarget.getBbWidth());
    }
}
