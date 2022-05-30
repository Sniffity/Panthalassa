package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class EntityHelicoprion extends PanthalassaEntity implements IAnimatable, Enemy, IBreachable, IHungry {

    public static final int BLOCKED_DISTANCE = 3;

    private AnimationFactory factory = new AnimationFactory(this);
    protected static final EntityDataAccessor<Boolean> BREACH_STATE = SynchedEntityData.defineId(EntityHelicoprion.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_BREACHING = SynchedEntityData.defineId(EntityHelicoprion.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> BREACH_COOLDOWN = SynchedEntityData.defineId(EntityHelicoprion.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> HUNGER_COOLDOWN = SynchedEntityData.defineId(EntityHelicoprion.class, EntityDataSerializers.FLOAT);

    public EntityHelicoprion(EntityType<? extends PanthalassaEntity> type, Level worldIn) {
        super(type, worldIn);
        this.adjustment = 0.25F;
        this.canBreatheOutsideWater = false;
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (getBreachState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.helicoprion.breach", true));
            return PlayState.CONTINUE;
        }
        if (this.getAttackingState() && !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.helicoprion.bite", true));
            return PlayState.CONTINUE;
        }
        if ((this.isOnGround() && !this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.helicoprion.beached", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(BREACH_STATE, Boolean.FALSE);
        this.entityData.define(IS_BREACHING, Boolean.FALSE);
        this.entityData.define(BREACH_COOLDOWN, 0.00F);
        this.entityData.define(HUNGER_COOLDOWN, 0F);
        super.defineSynchedData();
    }

    public void tick() {
        if (this.getHungerCooldown()>-1){
            setHungerCooldown((getHungerCooldown())-1);
        }
        if (this.getBreachCooldown()>-1){
            setBreachCooldown((getBreachCooldown())-1);
        }
        super.tick();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.2D;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, CompoundTag compound) {
        return super.finalizeSpawn(world, difficulty, reason, livingdata, compound);
    }



    public static AttributeSupplier.Builder helicoprionAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 30)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.MAX_HEALTH, 140)
                .add(Attributes.MOVEMENT_SPEED, 1.3F);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new PanthalassaDisorientGoal(this, 0.70D));
        this.goalSelector.addGoal(1, new PanthalassaBreachAttackGoal(this, 2.0F));
        this.goalSelector.addGoal(2, new PanthalassaSmartAttackGoal(this, 2.0F, false));
        this.goalSelector.addGoal(3, new PanthalassaFindWaterGoal(this, 0.15F));
        this.goalSelector.addGoal(3, new PanthalassaEscapeGoal(this, 1.3F));
        this.goalSelector.addGoal(4, new PanthalassaRandomSwimmingGoal(this, 0.9F, 10, BLOCKED_DISTANCE));
        //Self-defense target selector
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)));
        //Necessary for IBreachable Entities
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 1, true, false, entity -> (entity.getVehicle() != null)));
        //Player target selector
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, entity -> (entity instanceof Player && !(this.level.getDifficulty() == Difficulty.PEACEFUL))));
        //Self-exclusive target selector
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof Player) && !(entity instanceof EntityHelicoprion) && (entity.isInWater() || entity.level.getFluidState(entity.blockPosition().below()).is(FluidTags.WATER))));
        super.registerGoals();
    }

    @Override
    public void setBreachState(boolean breaching) {
        this.entityData.set(BREACH_STATE,breaching);
    }

    @Override
    public boolean getBreachState() {
        return this.entityData.get(BREACH_STATE);
    }

    @Override
    public void setBreaching(boolean breaching) {
        this.entityData.set(IS_BREACHING,breaching);
    }

    @Override
    public boolean getBreaching() {
        return this.entityData.get(IS_BREACHING);
    }

    @Override
    public void setBreachCooldown(float breachCooldown) {
        this.entityData.set(BREACH_COOLDOWN,breachCooldown);
    }

    @Override
    public float getBreachCooldown() {
        return this.entityData.get(BREACH_COOLDOWN);
    }


    @Override
    public void setHungerCooldown(float hungerCooldown) {
        this.entityData.set(HUNGER_COOLDOWN, hungerCooldown);
    }

    @Override
    public float getHungerCooldown() {
        return this.entityData.get(HUNGER_COOLDOWN);
    }
}