package com.github.sniffity.panthalassa.common.entity.ai;


import java.util.EnumSet;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;

public class PanthalassaMeleeAttackGoal extends Goal {
    protected final CreatureEntity attacker;
    private final double speedTowardsTarget;
    private final boolean longMemory;
    private Path path;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int delayCounter;
    private int ticksUntilNextAttack;
    private final int attackInterval = 20;
    private long lastCanUseCheck;
    private int failedPathFindingPenalty = 0;
    private boolean canPenalize = false;

    public PanthalassaMeleeAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
        this.attacker = creature;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean shouldExecute() {
        long i = this.attacker.world.getGameTime();
        //If less than 20 ticks have passed, do NOT execute.
        if (i - this.lastCanUseCheck < 20L) {
            return false;
            //If more than 20 ticks have passed...
        } else {
            //Mark the new time for execution.
            this.lastCanUseCheck = i;
            LivingEntity livingentity = this.attacker.getAttackTarget();
            //If the target is null, do NOT execute.
            if (livingentity == null) {
                return false;
                //If the target is dead, do NOT execute..
            } else if (!livingentity.isAlive()) {
                return false;
                //Note: non-executions STILL reeset the timer.
                //Hence, if on tick 0 I executed and on if on tick 20 I did not execute, it would still impede execution until tick 40. 40 ticks without execution.
                    //Each failed execution (due to target checks) means execution will be blocked for 20 ticks.
            } else {
                if (canPenalize) {
                    if (--this.delayCounter <= 0) {
                        this.path = this.attacker.getNavigator().getPathToEntity(livingentity, 0);
                        this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
                        return this.path != null;
                    } else {
                        return true;
                    }
                }
                this.path = this.attacker.getNavigator().getPathToEntity(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.getAttackReachSqr(livingentity) >= this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());
                }
            }
        }
    }

    public boolean shouldContinueExecuting() {
        //Here: It has a target, and the target is valid, and the target is alive.
        //If at any point either of these conditions become false, stop execution.
        LivingEntity livingentity = this.attacker.getAttackTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!this.longMemory) {
            //If longMemory is false....
                //Set noPath to false. End.
            return !this.attacker.getNavigator().noPath();
            //If long memory is true (and)BUT  the attacker's position relative to the living entity is NOT within its home distance....
        } else if (!this.attacker.isWithinHomeDistanceFromPosition(livingentity.getPosition())) {
                //Do not execute....
            return false;
        } else {
                    //If long memory is true (and) the attacker's position relative to the living entity IS within its home distance....
                        //Continue executing if the target is NOT an instance of player entity, OR the target is NOT either an spectator or in creative.
            return !(livingentity instanceof PlayerEntity) || !livingentity.isSpectator() && !((PlayerEntity)livingentity).isCreative();
            //Hence, longMemory = true makes it so a check is applied to verify that the target is within a certain distance and is a certain target.
        }
    }

    public void startExecuting() {
        //Set the target path towards the target.
        this.attacker.getNavigator().setPath(this.path, this.speedTowardsTarget);
        //Set the attacker to aggroed.
        this.attacker.setAggroed(true);
        //Set the delay counter to 0.
        this.delayCounter = 0;
        //Set the ticks until next attack to 0, it's attacking now.
        this.ticksUntilNextAttack = 0;
    }

    public void resetTask() {
        //Get the target entity.
        LivingEntity livingentity = this.attacker.getAttackTarget();
        //If it cannot target it, null the attack target. This will result in shouldContinueExecuting ending the task.
        if (!EntityPredicates.CAN_AI_TARGET.test(livingentity)) {
            this.attacker.setAttackTarget((LivingEntity)null);
        }

        // Upon resetting, clear the aggro condition and the path.
        this.attacker.setAggroed(false);
        this.attacker.getNavigator().clearPath();
    }

    public void tick() {
        //Once it's executing....
            //each tick, this batch will run.
                //Get the attack target.
        LivingEntity livingentity = this.attacker.getAttackTarget();

        //Look at the entity +- 30....
        this.attacker.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);

        //Get distance to the entity.
        double d0 = this.attacker.getDistanceSq(livingentity.getPosX(), livingentity.getPosY(), livingentity.getPosZ());

        //Subtract 1 from the delayCounter, then check:
            //If it's less than 0, pick 0.
            //Else, pick the delayCounter -1
                //Set the picked value as the new delay counter.
                    //0 4 is added to the delayCounter, meaning action will tick and then if condition will be skipped for 4-7 ticks.

                //Of note, if this is a new task, the delayCounter will start at 0.
                    //Hence, it will start by picking 0.
        this.delayCounter = Math.max(this.delayCounter - 1, 0);
        //IF the attacker can see the entity it's targetting
        // AND the delay counter is less than or equal to 0...
        // AND either:
            // the target vector from the attacker to the entity are all zero (attacker is at target)
            //OR if the distance is greater than 1.0
            //Or random event
        //Set the target vectors to the living entity.
        //Add to the delay counter.
            //See: //0: This IF condition is executed with a 4-7 tick gap in between.
        if ((this.longMemory || this.attacker.getEntitySenses().canSee(livingentity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
            this.targetX = livingentity.getPosX();
            this.targetY = livingentity.getPosY();
            this.targetZ = livingentity.getPosZ();
            this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
            //If canPenalize is true
            //ADD to the delay counter the failedPathFindingPenalty, hence delaying new attacks even further.
            if (this.canPenalize) {
                this.delayCounter += failedPathFindingPenalty;
                //PathFinding penalty will now be computed....
                //If the path to the target is null, do not attempt to execute another attack for 10 ticks.
                //If it's not null, get the finalPathPoint of the attack Path.
                //If the finalPathPoint is null OR the overall distance to target is greater than 1, PENALIZE
                    //Logic here being, failed to PathFind effectively if path is null, or if it's too far from target.
                        //Else, if target is both reachable and close to be reached, do not penalize.
                            //Penalization means that upon failing to PathFind to target,
                // or still PathFinding to target (far away) the entity will wait until attempting to PathFind to target again.

                if (this.attacker.getNavigator().getPath() != null) {
                    net.minecraft.pathfinding.PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
                    if (finalPathPoint != null && livingentity.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
                        failedPathFindingPenalty = 0;
                    else
                        failedPathFindingPenalty += 10;
                } else {
                    failedPathFindingPenalty += 10;
                }
            }

            //Analyze distance. Based on distance, set the delays for further attacks. If it's far, add a delay so it can reach it.
            //If it's closer, add another shorter delay so it can reach it.
            if (d0 > 1024.0D) {
                this.delayCounter += 10;
            } else if (d0 > 256.0D) {
                this.delayCounter += 5;
            }
            //If it's trying NOT to move to the entity, add a delay.
            if (!this.attacker.getNavigator().tryMoveToEntityLiving(livingentity, this.speedTowardsTarget)) {
                this.delayCounter += 15;
            }
        }

        //Advance a tick.
        this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        //Call next method
        this.checkAndPerformAttack(livingentity, d0);
    }

    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        //If it has reached the target, and an attack is permitted, ATTACK and reset the attack cooldown.
        if (distToEnemySqr <= d0 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.attacker.swingArm(Hand.MAIN_HAND);
            this.attacker.attackEntityAsMob(enemy);
        }

    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = 20;
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (double)(this.attacker.getWidth() * 2.0F * this.attacker.getWidth() * 2.0F + attackTarget.getWidth());
    }
}
