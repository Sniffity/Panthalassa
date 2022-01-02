package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.*;
import com.github.sniffity.panthalassa.server.registry.PanthalassaSounds;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.*;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
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
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.List;

public class EntityKronosaurus extends PanthalassaEntity implements IAnimatable, Enemy, ISchoolable {
    public static final int BLOCKED_DISTANCE = 3;
    protected static final EntityDataAccessor<Integer> AIR_SUPPLY = SynchedEntityData.defineId(EntityKronosaurus.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> LEADER = SynchedEntityData.defineId(EntityKronosaurus.class, EntityDataSerializers.BOOLEAN);

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityKronosaurus(EntityType<? extends PanthalassaEntity> type, Level worldIn) {
        super(type, worldIn);
        this.adjustment = 0.25F;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(AIR_SUPPLY, 150);
        this.entityData.define(LEADER, Boolean.FALSE);

        super.defineSynchedData();
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getAttackingState() && !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kronosaurus.attack", true));
            return PlayState.CONTINUE;
        }
        if ((this.getDeltaMovement().length()>0 && this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kronosaurus.idle", true));
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

    protected SoundEvent getAmbientSound() {
        return PanthalassaSounds.KRONOSAURUS_AMBIENT.get();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, CompoundTag compound) {
        return super.finalizeSpawn(world, difficulty, reason, livingdata, compound);
    }

    @Override
    public void tick() {
        /*
        if (!this.getIsLeader()){
            List<? extends PanthalassaEntity> school = this.level.getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(10));
            boolean flag = false;
            if (school.isEmpty()){
                this.setLeader(true);
            } else {
                for (int i = 0; i < school.size(); i++) {
                    PanthalassaEntity testEntity = school.get(i);
                    if (((ISchoolable) testEntity).getIsLeader()) {
                        flag = true;
                        break;
                    }
                }
            }
            if (!flag){
                this.setLeader(true);
            }
        }
         */

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

    public static AttributeSupplier.Builder kronosaurusAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 20)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MAX_HEALTH, 125)
                .add(Attributes.MOVEMENT_SPEED, (double) 1.3F);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(0, new PanthalassaFindWaterGoal(this, 0.1F));
        this.goalSelector.addGoal(1, new PanthalassaMeleeAttackGoal(this, 2.0F, false));
        //this.goalSelector.addGoal(2, new PanthalassaSchoolingGoal(this, 0.7F, 5));
        this.goalSelector.addGoal(3, new PanthalassaEscapeGoal(this, 1.3F));
        this.goalSelector.addGoal(4, new PanthalassaRandomSwimmingGoal(this, 0.7F, 10, BLOCKED_DISTANCE));
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, entity -> (entity instanceof Player && !(this.level.getDifficulty() == Difficulty.PEACEFUL) && (entity.isInWater() || entity.level.getFluidState(entity.blockPosition().below()).is(FluidTags.WATER)))));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof Player) && !(entity instanceof EntityKronosaurus) && !(entity instanceof EntityArchelon) && (entity.isInWater() || entity.level.getFluidState(entity.blockPosition().below()).is(FluidTags.WATER))) );
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 40, true, false, entity -> (entity instanceof EntityArchelon) && (entity.isInWater() || entity.level.getFluidState(entity.blockPosition().below()).is(FluidTags.WATER)) ));
    }

    public void setAirSupplyLocal(int airSupply) {
        this.entityData.set(AIR_SUPPLY,airSupply);
    }

    public int getAirSupplyLocal() {
        return this.entityData.get(AIR_SUPPLY);
    }

    public void setLeader(boolean leaderStatus) {
        this.entityData.set(LEADER,leaderStatus);
    }

    public boolean getIsLeader() {
        return this.entityData.get(LEADER);
    }

}