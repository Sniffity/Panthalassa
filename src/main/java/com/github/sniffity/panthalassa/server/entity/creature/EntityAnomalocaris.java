package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.monster.Enemy;
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

public class EntityAnomalocaris extends PanthalassaEntity implements IAnimatable, Enemy {

    public static final int BLOCKED_DISTANCE = 2;
    public static final float SCHOOL_SPEED = 1.0F;
    public static final float SCHOOL_AVOID_RADIUS = 10.0F;
    public static int SCHOOL_MAX_SIZE = 4;

    private AnimationFactory factory = new AnimationFactory(this);


    public EntityAnomalocaris(EntityType<? extends PanthalassaEntity> type, Level worldIn) {
        super(type, worldIn);
        this.adjustment = 0.10F;
        this.canBreatheOutsideWater = false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {

        if ((this.getDeltaMovement().length()>0 && this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.anomalocaris.swimming", true));
            return PlayState.CONTINUE;
        }
        if ((this.isOnGround() && !this.isInWater())) {
            return PlayState.STOP;
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


    public static AttributeSupplier.Builder anomalocarisAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 2)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, (double) 1.5F);
    }


    public void registerGoals() {
        this.goalSelector.addGoal(1, new PanthalassaPanicGoal(this, 0.7D));
        this.goalSelector.addGoal(1, new PanthalassaRandomSwimmingGoal(this, 0.9F, 10, BLOCKED_DISTANCE));
        this.goalSelector.addGoal(2, new PanthalassaEscapeGoal(this, 1.3F));
        this.goalSelector.addGoal(2, new PanthalassaFindWaterGoal(this, 0.15F));

    }
}
