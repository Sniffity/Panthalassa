package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
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


public class EntityCoelacanth extends PanthalassaEntity implements IAnimatable, IMob, ISchoolable {

    public static final int PASSIVE_ANGLE = 1;
    public static final int AGGRO_ANGLE = 15;
    public static final int BLOCKED_DISTANCE = 6;
    public static final float SCHOOL_SPEED = 1.0F;
    public static final float SCHOOL_AVOID_RADIUS = 10.0F;
    public static final float MAX_MOVE_SPEED = 0.8F;
    public static int SCHOOL_MAX_SIZE = 4;

    protected static final DataParameter<Integer> AIR_SUPPLY = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.INT);
    protected static final DataParameter<Boolean> LEADER = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.BOOLEAN);

    private AnimationFactory factory = new AnimationFactory(this);


    public EntityCoelacanth(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.moveControl = new PanthalassaSwimmingHelper(this, BLOCKED_DISTANCE, PASSIVE_ANGLE, AGGRO_ANGLE);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(AIR_SUPPLY, 300);
        this.entityData.define(LEADER, Boolean.FALSE);

        super.defineSynchedData();
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        //If it's moving in the water, swimming, play swim.
        if ((this.isDeadOrDying())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.coelacanth.test", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;


        /*
        //If it's out of the water, play bounce
        if ((this.isOnGround()) && !(this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kronosaurus.bounce", true));
            return PlayState.CONTINUE;
        }

        //If it's attacking, play attack
        if (this.isAggressive() && !(this.dead)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kronosaurus.attack", true));
            return PlayState.CONTINUE;
        }

        //If it's dying, play death
        if ((this.dead || this.getHealth() < 0.01)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kronosaurus.death", false));
            return PlayState.CONTINUE;
        }

        //IF it's just in water, play float
        if (this.isInWater()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kronosaurus.float", true));
            return PlayState.CONTINUE;
        }*/

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
    public void tick() {
        super.tick();
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

    public static AttributeModifierMap.MutableAttribute coelacanthAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, (double) 1.5F);
    }


    public void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(0, new PanthalassaSchoolingGoal(this, SCHOOL_SPEED, SCHOOL_MAX_SIZE, SCHOOL_AVOID_RADIUS));
        this.goalSelector.addGoal(1, new PanthalassaRandomSwimmingGoal(this, 0.9, 10, BLOCKED_DISTANCE));
        this.goalSelector.addGoal(2, new PanthalassaEscapeGoal(this, 1.3));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> (entity instanceof AbstractFishEntity)));
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