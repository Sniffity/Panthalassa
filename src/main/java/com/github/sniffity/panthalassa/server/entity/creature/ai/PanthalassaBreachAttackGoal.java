package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.entity.item.BoatEntity;

public class PanthalassaBreachAttackGoal extends Goal {
    protected final EntityMegalodon attacker;
    private final double speedTowardsTarget;
    private boolean step1Done;
    private boolean step2Done;
    private boolean step3Done;
    private double jumpStart;
    private double step1Ticks;
    private double step2Ticks;
    private double step3Ticks;

    public PanthalassaBreachAttackGoal(EntityMegalodon creature, double speedIn) {
        this.attacker = creature;
        this.speedTowardsTarget = speedIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = attacker.getTarget();
        if (attacker.getBreachCooldown()>0){
            return false;
        }

        if (attacker.level.dimension() == PanthalassaDimension.PANTHALASSA) {
            return false;
        }
        if ((target == null)) {
            return false;
        }
        BlockPos targetAbove = new BlockPos(target.getX(),target.getY()+3,target.getZ());
        if (!attacker.level.getBlockState(targetAbove).is(Blocks.AIR)){
            return false;
        }
        if (!target.isAlive()) {
            return false;
        }
        if (!attacker.isInWater()) {
            return false;
        }
        return true;
        }


    @Override
    public boolean canContinueToUse() {
        LivingEntity target = attacker.getTarget();
        if (target == null) {
            return false;
        }
        else if (!target.isAlive()) {
            return false;
        }
        else if (attacker.distanceTo(target) > 30) {
            return false;
        }
        else if (step1Ticks > 200) {
            return false;
        }
        else if (step2Ticks>200) {
            return false;
        }
        else if (step1Done && step2Done && step3Done) {
            return false;
        } else if (!step1Done &&  (!attacker.isInWater())) {
            return false;
        } else if (step1Done && !step2Done && ((!attacker.isInWater()) || attacker.distanceTo(target) > 12)) {
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        LivingEntity target = attacker.getTarget();
        attacker.setAggressive(true);
        attacker.isTryingToBreach = true;
        step1Ticks = 0;
        step2Ticks = 0;
    }

    public boolean moveStep1() {
        step1Ticks = ++step1Ticks;
        LivingEntity target = attacker.getTarget();
        Vector3d strikePos = new Vector3d(target.getX(), target.getY() - 10, target.getZ());
        attacker.getNavigation().moveTo(strikePos.x,strikePos.y,strikePos.z,speedTowardsTarget);
        return (attacker.distanceToSqr(target.getX(), target.getY() - 10, target.getZ()) <= 2);
    }


    public boolean moveStep2(){
        step2Ticks = ++step2Ticks;
        LivingEntity target = attacker.getTarget();
        attacker.setIsBreaching(true);
        attacker.getNavigation().moveTo(target.getX(),target.getY(),target.getZ(),speedTowardsTarget*4);
        return attacker.distanceTo(target) < 2.0F;
    }

    @Override
    public void stop() {
        LivingEntity target = attacker.getTarget();
        if (!EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            attacker.setTarget(null);
        }
        attacker.setAggressive(false);
        attacker.getNavigation().stop();
        attacker.isTryingToBreach = false;
        attacker.setIsBreaching(false);
        step1Ticks = 0;
        step2Ticks = 0;
        step3Ticks = 0;
        step1Done = false;
        step2Done = false;
        step3Done = false;
        attacker.setBreachCooldown(600);
        if (!attacker.getPassengers().isEmpty()) {
            attacker.ejectPassengers();
        }
    }

    @Override
    public void tick() {
        LivingEntity target = attacker.getTarget();
        if (!step1Done) {
            if (moveStep1()){
                attacker.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
                step1Done = true;
            }
        }
        if (step1Done && !step2Done) {
            if (attacker.distanceTo(target) > 4.0F) {
                if (moveStep2()) {
                    step2Done = true;
                    jumpStart = target.getY();
                    if (target.getVehicle() != null) {
                        target.getVehicle().startRiding(attacker);
                    } else {
                        target.startRiding(attacker);
                    }
                    if (attacker.getDeltaMovement().y < 1.00) {
                        attacker.setDeltaMovement(attacker.getDeltaMovement().add(0,1.00,0));
                    }
                }
            } else {
                step2Done = true;
                jumpStart = target.getY();
                if (target.getVehicle() != null) {
                    target.getVehicle().startRiding(attacker);
                } else {
                    target.startRiding(attacker);
                }
                if (attacker.getDeltaMovement().y < 1.00) {
                    attacker.setDeltaMovement(attacker.getDeltaMovement().add(0,1.00,0));
                }          }
        }
        if (step1Done && step2Done && !step3Done) {
            step3Ticks = ++step3Ticks;
            if (attacker.getY() - jumpStart > 5) {
                crushVehicleandPassengers();
                if (!attacker.getPassengers().isEmpty()) {
                    attacker.ejectPassengers();
                }
                attacker.setIsBreaching(false);
                step3Done = true;
            } else if (step3Ticks>30 && attacker.isInWater()){
                crushVehicleandPassengers();
                if (!attacker.getPassengers().isEmpty()) {
                    attacker.ejectPassengers();
                }
                step3Done = true;
            }
        }
    }

    protected void crushVehicleandPassengers() {
        List<Entity> entities = attacker.level.getEntities(attacker, new AxisAlignedBB(attacker.getX() - 5, attacker.getY() - 5, attacker.getZ() - 5, attacker.getX() + 5, attacker.getY() + 5, attacker.getZ() + 5));
        if (!entities.isEmpty()) {
                attacker.swing(Hand.MAIN_HAND);
                for (Entity entity : entities) {
                    attacker.doHurtTarget(entity);
                }
        }
    }
}
