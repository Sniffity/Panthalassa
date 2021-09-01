package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.ai.*;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.*;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.*;

public class EntityKronosaurus extends PanthalassaEntity implements IAnimatable, IMob, ISchoolable {
    public static final int BLOCKED_DISTANCE = 6;
    public float prevYRot;
    public float deltaYRot;
    public float adjustYaw;
    public float adjustment = 0.25F;
    public float newRotationPitch;
    public float prevRotationPitch;
    public float[] deltaYRotList = new float[10];
    public float averageDeltaYRot;

    protected static final DataParameter<Integer> AIR_SUPPLY = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.INT);
    protected static final DataParameter<Boolean> LEADER = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.BOOLEAN);

    private AnimationFactory factory = new AnimationFactory(this);


    public EntityKronosaurus(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.moveControl = new PanthalassaSwimmingHelper(this);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        this.setPathfindingMalus(PathNodeType.WATER_BORDER, 0.0F);

    }


    @Override
    protected void defineSynchedData() {
        this.entityData.define(AIR_SUPPLY, 150);
        this.entityData.define(LEADER, Boolean.FALSE);

        super.defineSynchedData();
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if ((this.getDeltaMovement().length()>0 && this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kronosaurus.idle", true));
            return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
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

        deltaYRot = this.yRot - prevYRot;
        prevYRot = this.yRot;

        for (int i = 9; i > 0; i--) {
            deltaYRotList[i] = deltaYRotList[i-1];
        }

        deltaYRotList[0] = deltaYRot;
        float sum = 0;
        for (int i = 0; i < 10; i++) {
            sum = sum + deltaYRotList[i];
        }
        averageDeltaYRot = sum / 10;
        if (adjustYaw > averageDeltaYRot) {
            adjustYaw = adjustYaw - adjustment;
            adjustYaw = Math.max(adjustYaw, averageDeltaYRot);
        } else if (adjustYaw < averageDeltaYRot) {
            adjustYaw = adjustYaw + adjustment;
            adjustYaw = Math.min(adjustYaw, averageDeltaYRot);
        }

        int i = this.getAirSupplyLocal();
        this.handleAirSupply(i);

        prevRotationPitch = xRot;
        newRotationPitch = ( (float) (MathHelper.atan2((this.getDeltaMovement().y),MathHelper.sqrt((this.getDeltaMovement().x)*(this.getDeltaMovement().x)+(this.getDeltaMovement().z)*(this.getDeltaMovement().z)))));


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

    public static AttributeModifierMap.MutableAttribute kronosaurusAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 20)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.MOVEMENT_SPEED, (double) 1.3F);
    }


    public void registerGoals() {
        this.goalSelector.addGoal(0, new PanthalassaFindWaterGoal(this, 0.1F));
        this.goalSelector.addGoal(1, new PanthalassaMeleeAttackGoal(this, 2.0, false));
        //this.goalSelector.addGoal(2, new PanthalassaSchoolingGoal(this, SCHOOL_SPEED, SCHOOL_MAX_SIZE, SCHOOL_AVOID_RADIUS));
        this.goalSelector.addGoal(3, new PanthalassaEscapeGoal(this, 1.3));
        this.goalSelector.addGoal(4, new PanthalassaRandomSwimmingGoal(this, 0.7, 10, BLOCKED_DISTANCE));
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, entity -> (entity instanceof PlayerEntity && !(this.level.getDifficulty() == Difficulty.PEACEFUL) && (entity.isInWater() || entity.level.getFluidState(entity.blockPosition().below()).is(FluidTags.WATER)))));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof PlayerEntity) && !(entity instanceof EntityKronosaurus) && !(entity instanceof EntityArchelon) && (entity.isInWater() || entity.level.getFluidState(entity.blockPosition().below()).is(FluidTags.WATER))) );
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