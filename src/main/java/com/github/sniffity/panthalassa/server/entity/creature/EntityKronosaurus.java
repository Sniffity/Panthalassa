package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.*;
import com.github.sniffity.panthalassa.server.registry.PanthalassaSounds;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
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

public class EntityKronosaurus extends PanthalassaEntity implements IAnimatable, Enemy, ISchoolable, IHungry {
    public static final int BLOCKED_DISTANCE = 3;
    protected static final EntityDataAccessor<Boolean> LEADER = SynchedEntityData.defineId(EntityKronosaurus.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> HUNGER_COOLDOWN = SynchedEntityData.defineId(EntityKronosaurus.class, EntityDataSerializers.FLOAT);

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityKronosaurus(EntityType<? extends PanthalassaEntity> type, Level worldIn) {
        super(type, worldIn);
        this.adjustment = 0.25F;
        this.canBreatheOutsideWater = false;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(LEADER, Boolean.FALSE);
        this.entityData.define(HUNGER_COOLDOWN, 0F);
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
        if ((this.isOnGround() && !this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kronosaurus.beached", true));
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
        if (this.getHungerCooldown()>-1){
            setHungerCooldown((getHungerCooldown())-1);
        }
        super.tick();
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
        this.goalSelector.addGoal(0, new PanthalassaDisorientGoal(this, 0.70D));
        this.goalSelector.addGoal(1, new PanthalassaSmartAttackGoal(this, 2.0F, false));
        //this.goalSelector.addGoal(2, new PanthalassaSchoolingGoal(this, 0.7F, 5));
        this.goalSelector.addGoal(3, new PanthalassaEscapeGoal(this, 1.3F));
        this.goalSelector.addGoal(3, new PanthalassaFindWaterGoal(this, 0.15F));
        this.goalSelector.addGoal(4, new PanthalassaRandomSwimmingGoal(this, 0.7F, 10, BLOCKED_DISTANCE));
        //Self-defense target selector
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)));
        //PLayer target selector
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, entity -> (entity instanceof Player && !(this.level.getDifficulty() == Difficulty.PEACEFUL) && (entity.isInWater() || entity.level.getFluidState(entity.blockPosition().below()).is(FluidTags.WATER)))));
        //Self-exclusive target selector
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof Player) && !(entity instanceof EntityKronosaurus) && (entity.isInWater() || entity.level.getFluidState(entity.blockPosition().below()).is(FluidTags.WATER))) );
    }

    public void setLeader(boolean leaderStatus) {
        this.entityData.set(LEADER,leaderStatus);
    }

    public boolean getIsLeader() {
        return this.entityData.get(LEADER);
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