package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
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

public class EntityArchelon extends PanthalassaEntity implements IAnimatable, Enemy, IHungry {
    public static final int BLOCKED_DISTANCE = 2;
    protected static final EntityDataAccessor<Float> HUNGER_COOLDOWN = SynchedEntityData.defineId(EntityArchelon.class, EntityDataSerializers.FLOAT);

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityArchelon(EntityType<? extends PanthalassaEntity> type, Level worldIn) {
        super(type, worldIn);
        this.canBreatheOutsideWater = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HUNGER_COOLDOWN, 0F);
        super.defineSynchedData();
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getAttackingState() && !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.archelon.attack", true));
            return PlayState.CONTINUE;
        }
        if (event.isMoving() && this.isInWater()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.archelon.swimming", true));
            return PlayState.CONTINUE;
        }
        if (event.isMoving() && this.isOnGround()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.archelon.land", true));
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
        if (this.getHungerCooldown()>-1){
            setHungerCooldown((getHungerCooldown())-1);
        }
        super.tick();
    }

    public static AttributeSupplier.Builder archelonAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.ARMOR, 30)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MAX_HEALTH, 60)
                .add(Attributes.MOVEMENT_SPEED, (double) 1.3F);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(0, new PanthalassaPanicGoal(this, 0.2D));
        this.goalSelector.addGoal(1, new PanthalassaRandomSwimmingGoal(this, 0.7, 10, BLOCKED_DISTANCE));
        this.goalSelector.addGoal(2, new PanthalassaEscapeGoal(this, 1.3));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.2D, 30));
        this.goalSelector.addGoal(3, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(4, new PanthalassaSmartAttackGoal(this, 1.3F, false));
        //Small fish target selector
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false,
                entity -> (entity instanceof Cod
                        || entity instanceof Salmon
                        || entity instanceof TropicalFish
                        || entity instanceof EntityAcrolepis
                        || entity instanceof EntityCeratodus
                        || entity instanceof EntityCoelacanth)));
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
