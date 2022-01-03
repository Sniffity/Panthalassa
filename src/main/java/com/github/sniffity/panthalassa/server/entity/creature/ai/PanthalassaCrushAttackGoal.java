package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class PanthalassaCrushAttackGoal extends Goal {
    protected final PanthalassaEntity attacker;
    protected final ICrushable crushingEntity;
    private final double speedTowardsTarget;
    private Path path;
    private double targetX;
    private double targetY;
    private double targetZ;
    private double initialHealth;
    private boolean step1Done;
    private int step1Ticks;
    private int step2Ticks;
    private Entity target;

    public PanthalassaCrushAttackGoal(PanthalassaEntity creature, double speedIn) {
        this.attacker = creature;
        this.crushingEntity = (ICrushable) creature;
        this.speedTowardsTarget = speedIn;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }


    @Override
    public boolean canUse() {
        //Do not attempt Goal if creature is not in water...
        if (!this.attacker.isInWater() && !this.attacker.level.getBlockState(new BlockPos(attacker.position()).below()).is(Blocks.WATER)) {
            return false;
        }

        //Or if, by any chance, it's using the land navigator..
        if (this.attacker.isLandNavigator) {
            return false;
        }

        //Only use after the crushCooldown has reset, this ensures it won't loop this attack...
        if (this.crushingEntity.getCrushCooldown()>0){
            return false;
        }

        //Same conditions as the MeleeAttackGoal...
        long i = this.attacker.level.getGameTime();

        LivingEntity livingentity = this.attacker.getTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else {
            this.path = this.attacker.getNavigation().createPath(livingentity, 0);
            if (this.path != null) {
                return true;
            } else {
                return this.attacker.distanceTo(livingentity) < 4.0;
            }
        }
    }



    @Override
    public boolean canContinueToUse() {
        //If it loses its target, stop using the goal...
        LivingEntity livingentity = this.attacker.getTarget();
        if (livingentity == null) {
            return false;
        }
        if (!livingentity.isAlive()) {
            return false;
        }
        //Copy conditions from MeleeAttackGoal...
        if (!this.attacker.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        }
        //If by any chance it's using the land navigator, stop the goal...
        if (this.attacker.isLandNavigator) {
            return false;
        }
        //If it gets out of water, stop the goal....
        if (!this.attacker.isInWater()) {
            return false;
        }
        //If it takes too much damage during the goal, stop the goal...
        if (this.initialHealth - this.attacker.getHealth() > 20) {
            return false;
        }
        //If step1 is done and for some reason it is not crushing something, stop the goal...
        if (step1Done && this.attacker.getPassengers().isEmpty()) {
            return false;
        }
        //Only try to reach the target for a reasonable amount of time. If it cannot reach target after this time, stop the goal...
        if (step1Ticks > 150) {
            return false;
        }
        //Only perform actual crush attack for a total of 5 seconds...
        if (step2Ticks > 100) {
           return false;
        }
        //Do not target players in creative or spectator...
        return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
    }

    @Override
    public void start() {
        this.attacker.getNavigation().moveTo(this.path, this.speedTowardsTarget);
        this.attacker.setAggressive(true);
        this.initialHealth = this.attacker.getHealth();
    }

    @Override
    public void stop() {
        //Clear target
        LivingEntity livingentity = this.attacker.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.attacker.setTarget((LivingEntity)null);
        }
        //Clear agressive state
        this.attacker.setAggressive(false);
        //Clear crushing state (used for animations)
        this.crushingEntity.setCrushingState(false);
        //Set crush cooldown to 600 ticks, this will count down on the entity's tick method
        this.crushingEntity.setCrushCooldown(600);
        //Release whatever it was crushing
        this.attacker.ejectPassengers();
        //Stop the navigation, clear the target position
        this.attacker.getNavigation().stop();
    }


    @Override
    public void tick() {
        if (!this.step1Done) {
            LivingEntity target = this.attacker.getTarget();
            if (target != null) {
                if (moveStep1()) {
                    if (target.getVehicle() != null) {
                        target.getVehicle().startRiding(attacker);
                        this.target = target.getVehicle();
                    } else {
                        target.startRiding(attacker);
                        this.target = target;
                    }
                    this.crushingEntity.setCrushingState(true);
                    this.step1Done = true;


                }
            }
        }
        //This section will get called a total of 6 times. Meaning, while crushing, the entity will deal damage 6 times, with intervals in between...
        if (step1Done) {
            ++step2Ticks;
            if (step2Ticks == 0 || step2Ticks == 20 || step2Ticks == 40 || step2Ticks == 60 || step2Ticks == 80 || step2Ticks == 100) {
                this.attacker.swing(InteractionHand.MAIN_HAND);
                this.attacker.doHurtTarget(target);
            }
        }
    }

    public boolean moveStep1(){
        //Creature will attempt to move to the target, from the StrikePosition.
        // If this position is unreachable, the Goal will be stopped in canContinueToUse().
        //A tick counter is implemented to stop the Goal if this takes too long. This avoids the creature getting stuck forever if something does not work out.
        step1Ticks = ++step1Ticks;
        LivingEntity target = attacker.getTarget();
        assert target != null;
        attacker.getLookControl().setLookAt(target.getX(), target.getY(), target.getZ());
        attacker.getNavigation().moveTo(target.getX(),target.getY(),target.getZ(),speedTowardsTarget*4);
        return attacker.distanceTo(target) < 4.0F;
    }

}