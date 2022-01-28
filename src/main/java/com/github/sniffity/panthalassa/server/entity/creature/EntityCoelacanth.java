package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class EntityCoelacanth extends PanthalassaEntity implements IAnimatable, Enemy, ISchoolable {

    public static final int BLOCKED_DISTANCE = 2;
    public static final float SCHOOL_SPEED = 1.0F;
    public static final float SCHOOL_AVOID_RADIUS = 10.0F;
    public static int SCHOOL_MAX_SIZE = 4;
    protected static final EntityDataAccessor<Boolean> LEADER = SynchedEntityData.defineId(EntityCoelacanth.class, EntityDataSerializers.BOOLEAN);

    private AnimationFactory factory = new AnimationFactory(this);


    public EntityCoelacanth(EntityType<? extends PanthalassaEntity> type, Level worldIn) {
        super(type, worldIn);
        this.adjustment = 0.10F;
        this.canBreatheOutsideWater = false;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LEADER, Boolean.FALSE);


        super.defineSynchedData();
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getAttackingState() && !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.coelacanth.attack", true));
            return PlayState.CONTINUE;
        }
        if ((this.isOnGround() && !this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.coelacanth.beached", true));
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

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, CompoundTag compound) {
        return super.finalizeSpawn(world, difficulty, reason, livingdata, compound);
    }


    @Override
    public void tick() {
        super.tick();

    }



    public static AttributeSupplier.Builder coelacanthAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, (double) 1.5F);
    }


    public void registerGoals() {
        this.goalSelector.addGoal(1, new PanthalassaPanicGoal(this, 0.7D));
        //this.goalSelector.addGoal(2, new PanthalassaSchoolingGoal(this, SCHOOL_SPEED, SCHOOL_MAX_SIZE, SCHOOL_AVOID_RADIUS));
        this.goalSelector.addGoal(1, new PanthalassaRandomSwimmingGoal(this, 0.9F, 10, BLOCKED_DISTANCE));
        this.goalSelector.addGoal(2, new PanthalassaEscapeGoal(this, 1.3F));
        this.goalSelector.addGoal(4, new PanthalassaMeleeAttackGoal(this, 1.3F, false));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> (entity instanceof Cod || entity instanceof Salmon || entity instanceof TropicalFish)));
    }

    public void setLeader(boolean leaderStatus) {
        this.entityData.set(LEADER,leaderStatus);
    }

    public boolean getIsLeader() {
        return this.entityData.get(LEADER);
    }

}