package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.List;

public class PanthalassaBreachAttackGoal extends Goal {
    protected final EntityMegalodon attacker;
    private final double speedTowardsTarget;
    private LivingEntity target;
    private BoatEntity boatTarget;
    private boolean step1Done;
    private boolean step2Done;
    private boolean step3Done;
    private boolean step4Done;
    private boolean step5Done;
    private double jumpStart;
    private double step1Ticks;
    private double step2Ticks;

    public PanthalassaBreachAttackGoal(EntityMegalodon creature, double speedIn) {
        this.attacker = creature;
        this.speedTowardsTarget = speedIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

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
        else if (this.attacker.distanceTo(boatTarget) > 40) {
            return false;
        }
        else if (!step2Done) {
            return !this.attacker.getNavigation().isDone();
        } else if (!this.attacker.isWithinRestriction(boatTarget.blockPosition())) {
            return false;
        } else if (step1Ticks > 200){
            return false;
        } else if (step2Ticks > 100){
            return false;
        } else if (step1Done && step2Done && step3Done && step4Done && step5Done){
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        this.attacker.setAggressive(true);
        if (!step1Done) {
            this.moveStep1();
        }
        this.jumpStart = this.boatTarget.getY();
        this.attacker.isTryingToBreach = true;
        step1Ticks = 0;
        step2Ticks = 0;
    }

    public void moveStep1() {
        step1Ticks = ++step1Ticks;
        if ((this.attacker.distanceToSqr(boatTarget.getX(), boatTarget.getY() - 10, boatTarget.getZ())) >= 4) {
            Vector3d strikePos = new Vector3d(this.boatTarget.getX(), this.boatTarget.getY() - 10, this.boatTarget.getZ());
            this.attacker.getNavigation().moveTo(strikePos.x,strikePos.y,strikePos.z,this.speedTowardsTarget*3);
        } else {
            step1Done = true;
        }
    }

    public boolean moveStep2(){
        step2Ticks = ++step2Ticks;
        this.attacker.getLookControl().setLookAt(boatTarget.getX(), boatTarget.getY(), boatTarget.getZ());
        this.attacker.getNavigation().moveTo(this.boatTarget.getX(),this.boatTarget.getY(),this.boatTarget.getZ(),this.speedTowardsTarget*5);
        return this.attacker.distanceTo(boatTarget) < 4.0F;
    }

    @Override
    public void stop() {
        if (!EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(boatTarget)) {
            this.attacker.setTarget(null);
        }
        this.attacker.setAggressive(false);
        this.attacker.getNavigation().stop();
        this.attacker.isTryingToBreach = false;
        this.attacker.setIsBreaching(false);
        step1Ticks = 0;
        step2Ticks = 0;
        step1Done = false;
        step2Done = false;
        step3Done = false;
        step4Done = false;
        step5Done = false;
    }

    @Override
    public void tick() {
        if (!step1Done) {
            moveStep1();
        } else if (!step2Done) {
            if (this.attacker.isInWater() && this.attacker.distanceTo(boatTarget)<12) {
                this.attacker.setIsBreaching(true);
                if (moveStep2()) {
                    //TODO: Either Start Riding here or set distance closer
                    step2Done = true;
                }
            } else {
                this.stop();
            }
        } else if (!step3Done) {
            boatTarget.startRiding(attacker);
            this.attacker.setDeltaMovement(this.attacker.getDeltaMovement().add(0.0, 1.5D, 0));
            if (attacker.getPassengers().isEmpty()) {
                this.stop();
            }
            step3Done = true;

        } else if (!step4Done) {
            if (attacker.getY() - jumpStart > 5) {
                crushBoatandPassengers();
                this.attacker.setIsBreaching(false);
                step4Done = true;
            } else if (this.attacker.isInWater()){
                if (!this.attacker.getPassengers().isEmpty()) {
                    this.attacker.ejectPassengers();
                }
                step4Done = true;
                step5Done= true;
                this.attacker.setIsBreaching(false);
                stop();
            }
        } else if (!step5Done && !this.attacker.isInWater()) {
            if (this.attacker.isInWater()) {
                if (!this.attacker.getPassengers().isEmpty()) {
                    this.attacker.ejectPassengers();
                }
                step5Done = true;
                this.attacker.setIsBreaching(false);
                stop();
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
