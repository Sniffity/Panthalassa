package com.github.sniffity.panthalassa.server.entity.creature.ai;

import java.util.EnumSet;

import com.github.sniffity.panthalassa.server.entity.creature.*;
import com.github.sniffity.panthalassa.server.registry.PanthalassaEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.InteractionHand;

public class PanthalassaSmartAttackGoal extends Goal {
    private IHungry hungryAttacker;
    private final double speedTowardsTarget;
    private final boolean longMemory;
    private Path path;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int delayCounter;
    private int ticksUntilNextAttack;
    private long lastCanUseCheck;
    private PanthalassaEntity panthalassaEntity;
    private boolean killFlag;
    private boolean isHungryAttacker = false;



    public PanthalassaSmartAttackGoal(PanthalassaEntity creature, double speedIn, boolean useLongMemory) {
        this.panthalassaEntity = creature;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (creature instanceof IHungry) {
            hungryAttacker = (IHungry) creature;
            isHungryAttacker = true;
        }
    }

    @Override
    public boolean canUse() {
        //Only attack creatures while this entity is in water...
        if (!this.panthalassaEntity.isInWater() && !this.panthalassaEntity.level.getBlockState(new BlockPos(panthalassaEntity.position()).below()).is(Blocks.WATER)){
            return false;
        }
        //Do not use if disoriented...
        if (this.panthalassaEntity.hasEffect(PanthalassaEffects.DISORIENT.get())) {
            return false;
        }
        //Do not use if landNavigator...
        if (this.panthalassaEntity.isLandNavigator) {
            return false;
        }
        //Perform a check for last attack...
        long i = this.panthalassaEntity.level.getGameTime();
        if (i - this.lastCanUseCheck < 20L) {
            return false;
        } else {
            this.lastCanUseCheck = i;
            //Verify the target is valid...
            LivingEntity livingentity = this.panthalassaEntity.getTarget();
            if (livingentity == null) {
                return false;
            }
            //Verify the target is alive...
            if (!livingentity.isAlive()) {
                return false;
            }
            //Was this Goal called for an attacker that has hunger as a parameter?
            //If so, is the creature being targeted NOT considered a threat?
            if (isHungryAttacker && !isEntityThreat(livingentity)) {
                //IF this is a hungry attacker, and the creature being targeted is NOT a threat...
                //Check for hunger and only attack if hungry...
                if (hungryAttacker.getHungerCooldown()<0){
                    //Creature is indeed hungry, proceed with the attack...
                    this.path = this.panthalassaEntity.getNavigation().createPath(livingentity, 0);
                    if (this.path != null) {
                        return true;
                    } else {
                        return this.getAttackReachSqr(livingentity) >= this.panthalassaEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                    }
                }
                //Creature was not hungry, and target is not a threat, do not attack.
                return false;
            }
            //If not, just proceed with the attack...
            //This will basically proceed for all creatures that only attack as self defense...
            //As well as permanently aggressive creatures...
            else {
                this.path = this.panthalassaEntity.getNavigation().createPath(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.getAttackReachSqr(livingentity) >= this.panthalassaEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity livingentity = this.panthalassaEntity.getTarget();
        if (livingentity == null) {
            return false;
        }
        if (this.panthalassaEntity.hasEffect(PanthalassaEffects.DISORIENT.get())) {
            return false;
        }
        if (!livingentity.isAlive()) {
            return false;
        }
        if (!this.longMemory) {
            return !this.panthalassaEntity.getNavigation().isDone();
        }
        if (!this.panthalassaEntity.isWithinRestriction(livingentity.blockPosition())) {
            return false;
        }
        if (this.panthalassaEntity.isLandNavigator) {
            return false;
        }
        return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player)livingentity).isCreative();
    }

    @Override
    public void start() {
        this.panthalassaEntity.getNavigation().moveTo(this.path, this.speedTowardsTarget);
        this.panthalassaEntity.setAggressive(true);
        this.delayCounter = 0;
        this.ticksUntilNextAttack = 0;
    }

    @Override
    public void stop() {
        LivingEntity livingentity = this.panthalassaEntity.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.panthalassaEntity.setTarget((LivingEntity)null);
        }

        if (killFlag) {
            hungryAttacker.setHungerCooldown(500);
        }

        this.panthalassaEntity.setAggressive(false);
        this.panthalassaEntity.setAttackingState(false);

        this.panthalassaEntity.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.panthalassaEntity.getTarget();

        this.panthalassaEntity.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);

        double d0 = this.panthalassaEntity.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());

        this.delayCounter = Math.max(this.delayCounter - 1, 0);

        if ((this.longMemory || this.panthalassaEntity.getSensing().hasLineOfSight(livingentity)) && this.delayCounter <= 0 && (this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D || livingentity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.panthalassaEntity.getRandom().nextFloat() < 0.05F)) {
            this.targetX = livingentity.getX();
            this.targetY = livingentity.getY();
            this.targetZ = livingentity.getZ();
            this.delayCounter = 4 + this.panthalassaEntity.getRandom().nextInt(7);
            if (d0 > 1024.0D) {
                this.delayCounter += 10;
            } else if (d0 > 256.0D) {
                this.delayCounter += 5;
            }
            if (!this.panthalassaEntity.getNavigation().moveTo(livingentity, this.speedTowardsTarget)) {
                this.delayCounter += 15;
            }
        }

        this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        this.checkAndPerformAttack(livingentity, d0);
    }

    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
        double d0 = this.getAttackReachSqr(enemy);
        if (distToEnemySqr <= d0 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.panthalassaEntity.swing(InteractionHand.MAIN_HAND);
            this.panthalassaEntity.setAttackingState(true);
            this.panthalassaEntity.doHurtTarget(enemy);
            if (enemy.isDeadOrDying()) {
                killFlag = true;
            }
        }

    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = 20;
    }

    protected double getAttackReachSqr(LivingEntity attackTarget) {
        return (double)(this.panthalassaEntity.getBbWidth() * 2.0F * this.panthalassaEntity.getBbWidth() * 2.0F + attackTarget.getBbWidth());
    }

    public boolean isEntityThreat(LivingEntity test) {
        if (test instanceof EntityBasilosaurus) {
            return true;
        }
        if (test instanceof EntityDunkleosteus) {
            return true;
        }
        if (test instanceof EntityGiantOrthocone) {
            return true;
        }
        if (test instanceof EntityKronosaurus) {
            return true;
        }
        if (test instanceof EntityLeedsichthys) {
            return true;
        }
        if (test instanceof EntityMegalodon) {
            return true;
        }
        if (test instanceof EntityMosasaurus) {
            return true;
        }
        if (test instanceof EntityThalassomedon) {
            return true;
        }
        if (test instanceof EntityHelicoprion) {
            return true;
        }
        if (test instanceof EntityAnglerfish) {
            return true;
        }
        if (test instanceof Player) {
            return true;
        }
        return false;
    }
}