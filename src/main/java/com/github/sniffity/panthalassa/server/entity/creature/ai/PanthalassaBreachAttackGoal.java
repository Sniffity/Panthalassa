package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
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
    private double step4Ticks;
    private BlockPos strikePos;

    public PanthalassaBreachAttackGoal(IBreachable creature, double speedIn) {
        this.panthalassaBreachableEntity = creature;
        this.attacker = (PanthalassaEntity) creature;
        this.speedTowardsTarget = speedIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        //Goal is usable only if the entity has a target.
        LivingEntity target = attacker.getTarget();
        if ((target == null) || !target.isAlive()) {
            return false;
        }
        //...and this target is floating on the water surface.
        if (!this.attacker.level.getBlockState(new BlockPos(target.blockPosition().below())).isPathfindable(this.attacker.level, new BlockPos(target.blockPosition().below()), PathComputationType.WATER)) {
            return false;
        }
        if (!attacker.isInWater()) {
            return false;
        }
        //Cooldown must have reset for the Goal to be usable.
        if (panthalassaBreachableEntity.getBreachCooldown()>0){
            return false;
        }

        //Do not use the Goal in Panthalassa, as it has a ceiling. Extra check just in case.
        if (attacker.level.dimension() == PanthalassaDimension.PANTHALASSA) {
            return false;
        }

        //There must be air above the target for the Goal to be usable.
        BlockPos targetAbove = new BlockPos(target.getX(),target.getY()+3,target.getZ());
        if (!attacker.level.getBlockState(targetAbove).is(Blocks.AIR)){
            return false;
        }

        return true;
        }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = attacker.getTarget();
        //If it hasn't attacked the target yet, and the target is somehow lost, stop the Goal.
        if (!step3Done && (target == null || !target.isAlive())) {
            return false;
        }

        //If it hasn't attacked the target yet, and the entity somehow leaves the water, stop the Goal.
        if (!step1Done &&  (!attacker.isInWater())) {
            return false;

        }

        if (target != null) {
            //If it still has a target, but the target gets too far away, stop the Goal.
            if (attacker.distanceTo(target) > 30) {
                return false;
            }
            //If it still has a target, and the strike position below the target is unreachable, stop the Goal.
            else if (!step1Done && !this.attacker.level.getBlockState(new BlockPos(target.blockPosition().below(10))).isPathfindable(this.attacker.level, new BlockPos(target.blockPosition().below()), PathComputationType.WATER)) {
                return false;
            }
            //If it still has a target, and has reached its strike position, but the position at which the jump would begin is somehow unreachable, stop the Goal.
            else if (step1Done && !step2Done && !this.attacker.level.getBlockState(new BlockPos(target.blockPosition().below(1))).isPathfindable(this.attacker.level, new BlockPos(target.blockPosition().below()), PathComputationType.WATER)) {
                return false;
            }
            //If it has reached the strike position, but has not started its jump yet, and it somehow leaves the water or the target gets too far, stop the Goal.
            else if (step1Done && !step2Done && ((!attacker.isInWater()) || attacker.distanceTo(target) > 20 )) {
                return false;

            }

        }
        // If it takes too long reaching the strike position, stop the Goal.
        if (step1Ticks > 100) {
            return false;
        }
        // If it takes too long reaching the jump position, stop the Goal.
        if (step2Ticks> 100) {
            return false;
        }
        // If it has attacked the target but has not yet navigated to its end position, and it cannot reach the end position, stop the Goal.
        if (step3Done && !step4Done && !this.attacker.level.getBlockState(strikePos).isPathfindable(this.attacker.level, strikePos, PathComputationType.WATER)) {
            return false;
        }
        // If it takes too long reaching the end position after the jump, stop the Goal.
        if (step4Ticks > 50) {
            return false;
        }
        //If it has finished all the steps, stop the Goal.
        if (step4Done) {
            return false;
        }
        //Else continue the Goal.
        return true;
    }

    @Override
    public void start() {
        attacker.setAggressive(true);
        attacker.isTryingToBreach = true;
    }

    @Override
    public void stop() {
        LivingEntity target = attacker.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            attacker.setTarget(null);
        }
        attacker.setAggressive(false);
        attacker.isTryingToBreach = false;
        panthalassaBreachableEntity.setIsBreaching(false);
        panthalassaBreachableEntity.setBreachCooldown(600);
        if (!attacker.getPassengers().isEmpty()) {
            attacker.ejectPassengers();
        }
        attacker.getNavigation().stop();
    }

    @Override
    public void tick() {
        //Step1: Reach Strike Position, 12 blocks below the target.
        //Step 2: Reach Jump starting position, at water surface level, where the target's at.
        //Step 3: Perform grab and attack.
        //Step 4: Return to the water.

        //Update the target
        LivingEntity target = attacker.getTarget();
        if (!step1Done) {
            //Attempt step 1....
            if (moveStep1()){
                //If the strike position is reached, mark Step1 as complete.
                assert target != null;
                attacker.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
                step1Done = true;
                //Set the Breaching Data Parameter to true, for animations to work.
                panthalassaBreachableEntity.setIsBreaching(true);
            }
        }

        if (step1Done && !step2Done) {
            //Attempt step 2....
            assert target != null;
            if (moveStep2()) {
                //If the JumpStart position is reached, record this position (will be used to determine when to deal damage).
                step2Done = true;
                jumpStart = target.getY();
                //Mount targets to this entity. If targets are riding vehicles, mount the vehicle instead.
                //Target entities will be riding their vehicle, which will be riding this entity.
                if (target.getVehicle() != null) {
                    target.getVehicle().startRiding(attacker);
                } else {
                    target.startRiding(attacker);

                }
                //Apply an upwards boost to this entity, ensuring it jumps.
                if (attacker.getDeltaMovement().y < 1.50D) {
                    attacker.setDeltaMovement(attacker.getDeltaMovement().x,1.50D,attacker.getDeltaMovement().z);
                }
            }
        }
        //Perform step3...
        if (step1Done && step2Done && !step3Done) {
            //Once it goes 5 blocks above where the jump started, attack the targets.
            step3Ticks = ++step3Ticks;
            if (attacker.getY() - jumpStart > 5) {
                crushVehicleAndPassengers();
                //If it did not kill something, release it. Dismount.
                if (!attacker.getPassengers().isEmpty()) {
                    attacker.ejectPassengers();
                }
                panthalassaBreachableEntity.setIsBreaching(false);
                step3Done = true;
            }
            //Alternatively, if it somehow returns to the water after jumping out without reaching such a height, just perform the attack at this point.
            else if (step3Ticks>30 && attacker.isInWater()){
                crushVehicleAndPassengers();
                //If it did not kill something, release it. Dismount.
                if (!attacker.getPassengers().isEmpty()) {
                    attacker.ejectPassengers();
                }
                step3Done = true;
            }
        }
        //Attempt Step4....
        if (step1Done && step2Done && step3Done) {
            if (moveStep4()){
                step4Done = true;
            }
        }
    }
    public boolean moveStep1() {
        //Creature will attempt to move close to a position 12 blocks below the target.
        // If this position is unreachable, the Goal will be stopped in canContinueToUse().
        //A tick counter is implemented to stop the Goal if this takes too long. This avoids the creature getting stuck forever if something does not work out.
        step1Ticks = ++step1Ticks;
        LivingEntity target = attacker.getTarget();
        assert target != null;
        strikePos = (target.blockPosition().below(12));
        attacker.getNavigation().moveTo(strikePos.getX(),strikePos.getY(),strikePos.getZ(),speedTowardsTarget);
        return (attacker.distanceToSqr(target.getX(), target.getY() - 12, target.getZ()) <= 10);
    }

    public boolean moveStep2(){
        //Creature will attempt to move to the target, from the StrikePosition.
        // If this position is unreachable, the Goal will be stopped in canContinueToUse().
        //A tick counter is implemented to stop the Goal if this takes too long. This avoids the creature getting stuck forever if something does not work out.
        step2Ticks = ++step2Ticks;
        LivingEntity target = attacker.getTarget();
        assert target != null;
        attacker.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
        attacker.getNavigation().moveTo(target.getX(),target.getY(),target.getZ(),speedTowardsTarget*4);
        return attacker.distanceTo(target) < 2.0F;
    }

    public boolean moveStep4(){
        //Once done with the BreachAttack, return to the water and try to move back to the original StrikePosition.
        //This is done to avoid the entity trying to get to an odd place after the attack is performed.
        // If this position is unreachable, the Goal will be stopped in canContinueToUse().
        //A tick counter is implemented to stop the Goal if this takes too long. This avoids the creature getting stuck forever if something does not work out.
        step4Ticks = ++step4Ticks;
        attacker.getNavigation().moveTo(strikePos.getX(),strikePos.getY(),strikePos.getZ(),speedTowardsTarget);
        return (attacker.distanceToSqr(strikePos.getX(), strikePos.getY(), strikePos.getZ()) <= 20);
    }

    protected void crushVehicleAndPassengers() {
        //Deal damage in an area close to the attacking entity. Attacks everything within its reach.
        List<Entity> entities = attacker.level.getEntities(attacker, new AABB(attacker.getX() - 5, attacker.getY() - 3, attacker.getZ() - 5, attacker.getX() + 5, attacker.getY() + 10, attacker.getZ() + 5));
        if (!entities.isEmpty()) {
                attacker.swing(InteractionHand.MAIN_HAND);
                for (Entity entity : entities) {
                    attacker.doHurtTarget(entity);
                }
        }
    }
}