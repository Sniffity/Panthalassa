package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.*;
import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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

public class EntityMegalodon extends PanthalassaEntity implements IAnimatable, IMob {

    public int blockDistance = 5;
    public int passiveAngle = 4;
    public int aggroAngle = 8;
    public float prevYRot;
    public float deltaYRot;
    public float adjustRotation;
    public float adjustment = 0.25F;


    private AnimationFactory factory = new AnimationFactory(this);
    protected static final DataParameter<Boolean> IS_BREACHING = EntityDataManager.defineId(EntityMegalodon.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> BREACH_COOLDOWN = EntityDataManager.defineId(EntityMegalodon.class, DataSerializers.FLOAT);


    public EntityMegalodon(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.moveControl = new PanthalassaSwimmingHelper(this, blockDistance, passiveAngle, aggroAngle);
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (getIsBreaching()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.megalodon.breach", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(IS_BREACHING, Boolean.FALSE);
        this.entityData.define(BREACH_COOLDOWN, 0.00F);
        super.defineSynchedData();
    }

    @Override
    public void tick() {
        super.tick();
        setBreachCooldown((getBreachCooldown())-1);
        deltaYRot = this.yRot - prevYRot;
        prevYRot = this.yRot;
        if (adjustRotation > deltaYRot) {
            adjustRotation = adjustRotation - adjustment;
            adjustRotation = Math.max(adjustRotation, deltaYRot);
        } else if (adjustRotation < deltaYRot) {
            adjustRotation = adjustRotation + adjustment;
            adjustRotation = Math.min(adjustRotation, deltaYRot);
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
    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingdata, CompoundNBT compound) {
        return super.finalizeSpawn(world, difficulty, reason, livingdata, compound);
    }


    public static AttributeModifierMap.MutableAttribute megalodonAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 40)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.MAX_HEALTH, 175)
                .add(Attributes.MOVEMENT_SPEED, (double) 1.3F);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(0, new PanthalassaBreachAttackGoal(this, 2.0));
        this.goalSelector.addGoal(1, new PanthalassaMeleeAttackGoal(this, 2.0, false));
        this.goalSelector.addGoal(2, new PanthalassaEscapeGoal(this, 1.3));
        this.goalSelector.addGoal(3, new PanthalassaRandomSwimmingGoal(this, 0.9, 10, blockDistance));
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 1, true, false, entity -> (entity.getVehicle() != null)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, entity -> (entity instanceof PlayerEntity && !(this.level.getDifficulty() == Difficulty.PEACEFUL))));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof PlayerEntity) && !(entity instanceof EntityMegalodon) && !(entity instanceof EntityArchelon)));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> (entity instanceof EntityArchelon)));
        super.registerGoals();
    }

    public void setIsBreaching(boolean breaching) {
        this.entityData.set(IS_BREACHING,breaching);
    }

    public boolean getIsBreaching() {
        return this.entityData.get(IS_BREACHING);
    }

    public void setBreachCooldown(float breachCooldown) {
        this.entityData.set(BREACH_COOLDOWN,breachCooldown);
    }

    public float getBreachCooldown() {
        return this.entityData.get(BREACH_COOLDOWN);
    }

}