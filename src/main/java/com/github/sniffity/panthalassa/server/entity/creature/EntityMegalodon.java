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
import net.minecraft.util.math.vector.Vector3d;
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

public class EntityMegalodon extends PanthalassaEntity implements IAnimatable, IMob, IBreachable {

    public static final int PASSIVE_ANGLE = 1;
    public static final int AGGRO_ANGLE = 15;
    public float prevYRot;
    public float deltaYRot;
    public float adjustYaw;
    public float adjustment = 0.25F;
    public static final int BLOCKED_DISTANCE = 5;


    private AnimationFactory factory = new AnimationFactory(this);
    protected static final DataParameter<Boolean> IS_BREACHING = EntityDataManager.defineId(EntityMegalodon.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> BREACH_COOLDOWN = EntityDataManager.defineId(EntityMegalodon.class, DataSerializers.FLOAT);
    protected static final DataParameter<Integer> AIR_SUPPLY = EntityDataManager.defineId(EntityMegalodon.class, DataSerializers.INT);


    public EntityMegalodon(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.moveControl = new PanthalassaSwimmingHelper(this, BLOCKED_DISTANCE, PASSIVE_ANGLE, AGGRO_ANGLE);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
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
        this.entityData.define(AIR_SUPPLY, 300);

        super.defineSynchedData();
    }

    @Override
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
            this.setAirSupplyLocal(300);
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

    /*
    @Override
    public double getPassengersRidingOffset() {
        return 5.0D;
    }
*/
    @Override
    public void positionRider(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            Vector3d vector3d = (new Vector3d((double)0, 0, 0.0D)).yRot(-this.yRot * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
            passenger.setPos(this.getX() + vector3d.x, this.getY() + 5.0D, this.getZ() + vector3d.z);
        }
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
        this.goalSelector.addGoal(3, new PanthalassaRandomSwimmingGoal(this, 0.9, 10, BLOCKED_DISTANCE));
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 1, true, false, entity -> (entity.getVehicle() != null)));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, entity -> (entity instanceof PlayerEntity && !(this.level.getDifficulty() == Difficulty.PEACEFUL))));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof PlayerEntity) && !(entity instanceof EntityMegalodon) && !(entity instanceof EntityArchelon)));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 40, true, false, entity -> (entity instanceof EntityArchelon)));
        super.registerGoals();
    }

    @Override
    public void setIsBreaching(boolean breaching) {
        this.entityData.set(IS_BREACHING,breaching);
    }

    @Override
    public boolean getIsBreaching() {
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
}