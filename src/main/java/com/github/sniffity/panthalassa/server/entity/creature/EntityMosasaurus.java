package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class EntityMosasaurus extends PanthalassaEntity implements IAnimatable, IMob {
    public int passiveAngle = 4;
    public int aggroAngle = 15;

    public float prevYRot;
    public float deltaYRot;
    public float prevXRot;
    public float deltaXRot;
    public float adjustYaw;
    public float adjustPitch;
    public float adjustment = 0.15F;

    private AnimationFactory factory = new AnimationFactory(this);

    protected static final DataParameter<Integer> AIR_SUPPLY = EntityDataManager.defineId(EntityMosasaurus.class, DataSerializers.INT);

    public EntityMosasaurus(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.moveControl = new PanthalassaSwimmingHelper(this, getAvoidDistance(), passiveAngle, aggroAngle);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (!this.isInWater()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mosaurus.test", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }




    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

/*
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_HOGLIN_DEATH;
    }
*/

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingdata, CompoundNBT compound) {
        return super.finalizeSpawn(world, difficulty, reason, livingdata, compound);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(AIR_SUPPLY, 300);
        this.entityData.define(AVOID_DISTANCE, 5);
        super.defineSynchedData();
    }


    @Override
    public void tick() {
        super.tick();
        deltaYRot = this.yRot - prevYRot;
        prevYRot = this.yRot;
        if (adjustYaw > deltaYRot) {
            adjustYaw = adjustYaw - adjustment;
            adjustYaw = Math.max(adjustYaw, deltaYRot);
        } else if (adjustYaw < deltaYRot) {
            adjustYaw = adjustYaw + adjustment;
            adjustYaw = Math.min(adjustYaw, deltaYRot);
        }
        deltaXRot = this.xRot - prevXRot;
        prevXRot = this.xRot;
        if (adjustPitch > deltaXRot) {
            adjustPitch = adjustPitch - adjustment;
            adjustPitch = Math.max(adjustPitch, deltaXRot);
        } else if (adjustPitch < deltaXRot) {
            adjustPitch = adjustPitch + adjustment;
            adjustPitch = Math.min(adjustPitch, deltaXRot);
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
            this.setAirSupplyLocal(300);
        }
    }

    public static AttributeModifierMap.MutableAttribute archelonAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 35)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.MAX_HEALTH, 130)
                .add(Attributes.MOVEMENT_SPEED, (double) 1.3F);
    }

    public void registerGoals() {
//        this.goalSelector.addGoal(0, new PanthalassaBreachAttackGoal(this, 2.0));
        this.goalSelector.addGoal(1, new PanthalassaMeleeAttackGoal(this, 2.2, false));
        this.goalSelector.addGoal(2, new PanthalassaEscapeGoal(this, 1.6));
        this.goalSelector.addGoal(3, new PanthalassaRandomSwimmingGoal(this, 0.9, 10, getAvoidDistance()));
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 1, true, false, entity -> (entity.getVehicle() != null)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, entity -> (entity instanceof PlayerEntity && !(this.level.getDifficulty() == Difficulty.PEACEFUL))));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof PlayerEntity) && !(entity instanceof EntityMosasaurus) && !(entity instanceof EntityArchelon)));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 40, true, false, entity -> (entity instanceof EntityArchelon)));
        super.registerGoals();

    }

    public void setAirSupplyLocal(int airSupply) {
        this.entityData.set(AIR_SUPPLY,airSupply);
    }

    public int getAirSupplyLocal() {
        return this.entityData.get(AIR_SUPPLY);
    }
}