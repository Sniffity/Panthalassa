package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import javax.annotation.Nullable;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;


public class EntityMegalodon extends PanthalassaEntity implements IAnimatable, Enemy, IBreachable {

    public static final int BLOCKED_DISTANCE = 3;
    public float prevYRot;
    public float deltaYRot;
    public float adjustYaw;
    public float adjustment = 0.35F;

    private AnimationFactory factory = new AnimationFactory(this);
    protected static final EntityDataAccessor<Boolean> BREACH_STATE = SynchedEntityData.defineId(EntityMegalodon.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_BREACHING = SynchedEntityData.defineId(EntityMegalodon.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> BREACH_COOLDOWN = SynchedEntityData.defineId(EntityMegalodon.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Integer> AIR_SUPPLY = SynchedEntityData.defineId(EntityMegalodon.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> TEXTURE_VARIANT = SynchedEntityData.defineId(EntityMegalodon.class, EntityDataSerializers.INT);

    public EntityMegalodon(EntityType<? extends PanthalassaEntity> type, Level worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.STOP;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(BREACH_STATE, Boolean.FALSE);
        this.entityData.define(IS_BREACHING, Boolean.FALSE);
        this.entityData.define(BREACH_COOLDOWN, 0.00F);
        this.entityData.define(AIR_SUPPLY, 150);
        this.entityData.define(TEXTURE_VARIANT, 0);

        super.defineSynchedData();
    }

    public void tick() {
        super.tick();
        setBreachCooldown((getBreachCooldown())-1);

        deltaYRot = this.yRot - prevYRot;
        prevYRot = this.yRot;
        if (adjustYaw > deltaYRot) {
            adjustYaw = adjustYaw - adjustment;
            adjustYaw = Math.max(adjustYaw, deltaYRot);
        } else if (adjustYaw < deltaYRot) {
            adjustYaw = adjustYaw + adjustment;
            adjustYaw = Math.min(adjustYaw, deltaYRot);
        }


        int i = this.getAirSupplyLocal();
        this.handleAirSupply(i);
    }

    protected void handleAirSupply(int p_209207_1_) {
        if (this.isAlive() && !this.isInWaterOrBubble()) {
            this.setAirSupplyLocal(p_209207_1_ - 1);
            if (this.getAirSupplyLocal() == -20) {
                this.setAirSupplyLocal(0);
                this.hurt(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAirSupplyLocal(150);
        }
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
        int textureVariant = (int) (Math.random()*3);
        this.setTextureVariant(textureVariant);
        return super.finalizeSpawn(world, difficulty, reason, livingdata, compound);
    }

    public static AttributeSupplier.Builder megalodonAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 40)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.MAX_HEALTH, 175)
                .add(Attributes.MOVEMENT_SPEED, 1.3F);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new PanthalassaFindWaterGoal(this, 0.1F));
        this.goalSelector.addGoal(1, new PanthalassaBreachAttackGoal(this, 2.0F));
        this.goalSelector.addGoal(2, new PanthalassaMeleeAttackGoal(this, 2.0F, false));
        this.goalSelector.addGoal(3, new PanthalassaEscapeGoal(this, 1.3F));
        this.goalSelector.addGoal(4, new PanthalassaRandomSwimmingGoal(this, 0.9F, 10, BLOCKED_DISTANCE));
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 1, true, false, entity -> (entity.getVehicle() != null)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, entity -> (entity instanceof Player && !(this.level.getDifficulty() == Difficulty.PEACEFUL))));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof Player) && !(entity instanceof EntityMegalodon) && !(entity instanceof EntityArchelon)));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 40, true, false, entity -> (entity instanceof EntityArchelon)));
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

    public void setAirSupplyLocal(int airSupply) {
        this.entityData.set(AIR_SUPPLY,airSupply);
    }

    public int getAirSupplyLocal() {
        return this.entityData.get(AIR_SUPPLY);
    }

    public void setTextureVariant(int textureVariant) {
        this.entityData.set(TEXTURE_VARIANT,textureVariant);
    }

    public int getTextureVariant() {
        return this.entityData.get(TEXTURE_VARIANT);
    }

}