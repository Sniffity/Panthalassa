package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import java.util.EnumSet;
import java.util.List;

public class PanthalassaBreachAttackGoal extends Goal {
    protected final IBreachable panthalassaBreachableEntity;
    protected final PanthalassaEntity attacker;
    private final double speedTowardsTarget;
    private boolean step1Done;
    private boolean step2Done;
    private boolean step3Done;
    private boolean step4Done;
    private double jumpStart;
    private double step1Ticks;
    private double step2Ticks;
    private double step3Ticks;

    public PanthalassaBreachAttackGoal(IBreachable creature, double speedIn) {
        this.panthalassaBreachableEntity = creature;
        this.attacker = (PanthalassaEntity) creature;
        this.speedTowardsTarget = speedIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = attacker.getTarget();
        if ((target == null)) {
            return false;
        }

        if (!this.attacker.level.getBlockState(new BlockPos(target.blockPosition().below())).isPathfindable(this.attacker.level, new BlockPos(target.blockPosition().below()), PathType.WATER)) {
            return false;
        }
        if (!attacker.isInWater()) {
            return false;
        }
        if (panthalassaBreachableEntity.getBreachCooldown()>0){
            return false;
        }

        if (attacker.level.dimension() == PanthalassaDimension.PANTHALASSA) {
            return false;
        }

        BlockPos targetAbove = new BlockPos(target.getX(),target.getY()+3,target.getZ());
        if (!attacker.level.getBlockState(targetAbove).is(Blocks.AIR)){
            return false;
        }
        if (!target.isAlive()) {
            return false;
        }
        return true;
        }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = attacker.getTarget();
        if ((!step3Done || step4Done) && target == null) {
            return false;
        }
        else if (target != null && attacker.distanceTo(target) > 30) {
            return false;
        }
        else if ((!step3Done || step4Done) && !target.isAlive()) {
            return false;
        }
        else if (target != null && !step1Done && !this.attacker.level.getBlockState(new BlockPos(target.blockPosition().below(10))).isPathfindable(this.attacker.level, new BlockPos(target.blockPosition().below()), PathType.WATER)) {
            return false;
        }
        else if (target != null && step1Done && !step2Done && !this.attacker.level.getBlockState(new BlockPos(target.blockPosition().below(1))).isPathfindable(this.attacker.level, new BlockPos(target.blockPosition().below()), PathType.WATER)) {
            return false;
        }

        else if (step1Ticks > 100) {
            return false;
        }
        else if (step2Ticks> 100) {
            return false;
        }
        else if (step1Done && step2Done && step3Done) {
            return false;

        } else if (!step1Done &&  (!attacker.isInWater())) {
            return false;

        } else if (target != null && step1Done && !step2Done && ((!attacker.isInWater()) || attacker.distanceTo(target) > 20 )) {
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        attacker.setAggressive(true);
        attacker.isTryingToBreach = true;
        step1Ticks = 0;
        step2Ticks = 0;
    }

    public boolean moveStep1() {
        step1Ticks = ++step1Ticks;
        LivingEntity target = attacker.getTarget();
        assert target != null;
        BlockPos strikePos = (target.blockPosition().below(10));
        attacker.getNavigation().moveTo(strikePos.getX(),strikePos.getY(),strikePos.getZ(),speedTowardsTarget);
        return (attacker.distanceToSqr(target.getX(), target.getY() - 15, target.getZ()) <= 15);
    }

    public boolean moveStep2(){
        step2Ticks = ++step2Ticks;
        LivingEntity target = attacker.getTarget();
        assert target != null;
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
        attacker.isTryingToBreach = false;
        panthalassaBreachableEntity.setIsBreaching(false);
        step1Ticks = 0;
        step2Ticks = 0;
        step3Ticks = 0;
        step1Done = false;
        step2Done = false;
        step3Done = false;
        step4Done = false;
        panthalassaBreachableEntity.setBreachCooldown(600);
        if (!attacker.getPassengers().isEmpty()) {
            attacker.ejectPassengers();
        }

        attacker.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = attacker.getTarget();
        if (!step1Done) {
            if (moveStep1()){
                assert target != null;
                attacker.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
                step1Done = true;
                panthalassaBreachableEntity.setIsBreaching(true);
            }
        }

        if (step1Done && !step2Done) {
            assert target != null;
            if (moveStep2()) {
                step2Done = true;
                jumpStart = target.getY();
                if (target.getVehicle() != null) {
                    target.getVehicle().startRiding(attacker);
                } else {
                    target.startRiding(attacker);

                }
                if (attacker.getDeltaMovement().y < 2.0D) {
                    attacker.setDeltaMovement(attacker.getDeltaMovement().x,2.0D,attacker.getDeltaMovement().z);
                }
            }

        }

        if (step1Done && step2Done && !step3Done) {
            step3Ticks = ++step3Ticks;
            if (attacker.getY() - jumpStart > 5) {
                crushVehicleandPassengers();
                if (!attacker.getPassengers().isEmpty()) {
                    attacker.ejectPassengers();
                }
                panthalassaBreachableEntity.setIsBreaching(false);
                step3Done = true;
            } else if (step3Ticks>30 && attacker.isInWater()){
                crushVehicleandPassengers();
                if (!attacker.getPassengers().isEmpty()) {
                    attacker.ejectPassengers();
                }
                step3Done = true;
            }
        }

        /*
        if (step1Done && step2Done && step3Done && !step4Done && attacker.isInWater()) {
            attacker.setDeltaMovement(attacker.getDeltaMovement().add(0,-2.0,0));
            step4Done = true;
        }
         */
    }

    protected void crushVehicleandPassengers() {
        List<Entity> entities = attacker.level.getEntities(attacker, new AxisAlignedBB(attacker.getX() - 5, attacker.getY() - 3, attacker.getZ() - 5, attacker.getX() + 5, attacker.getY() + 10, attacker.getZ() + 5));
        if (!entities.isEmpty()) {
                attacker.swing(Hand.MAIN_HAND);
                for (Entity entity : entities) {
                    attacker.doHurtTarget(entity);
                }
        }
    }
}
