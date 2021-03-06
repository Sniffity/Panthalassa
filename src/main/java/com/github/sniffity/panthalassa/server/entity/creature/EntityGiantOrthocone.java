package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class EntityGiantOrthocone extends PanthalassaEntity implements IAnimatable, Enemy, ICrushable, IHungry {
    public static final int BLOCKED_DISTANCE = 3;

    protected static final EntityDataAccessor<Float> CRUSH_COOLDOWN = SynchedEntityData.defineId(EntityGiantOrthocone.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Boolean> CRUSHING_STATE = SynchedEntityData.defineId(EntityGiantOrthocone.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> CRUSHING = SynchedEntityData.defineId(EntityGiantOrthocone.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> HUNGER_COOLDOWN = SynchedEntityData.defineId(EntityGiantOrthocone.class, EntityDataSerializers.FLOAT);

    private AnimationFactory factory = new AnimationFactory(this);


    public EntityGiantOrthocone(EntityType<? extends PanthalassaEntity> type, Level worldIn) {
        super(type, worldIn);
        this.canBreatheOutsideWater = false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CRUSH_COOLDOWN, 0.00F);
        this.entityData.define(CRUSHING_STATE, false);
        this.entityData.define(CRUSHING, false);
        this.entityData.define(HUNGER_COOLDOWN, 0F);
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getCrushingState()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.giant_orthocone.grabbing", true));
            return PlayState.CONTINUE;
        }
        if (this.getAttackingState() && !(this.dead || this.getHealth() < 0.01 || this.isDeadOrDying())) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.giant_orthocone.attack", true));
            return PlayState.CONTINUE;
        }
        if ((this.getDeltaMovement().length()>0 && this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.giant_orthocone.swimming", true));
            return PlayState.CONTINUE;
        }
        if ((this.isOnGround() && !this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.giant_orthocone.beached", true));
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
        if (this.getCrushCooldown()>-1){
            setCrushCooldown((getCrushCooldown())-1);
        }
        super.tick();
    }

    public static AttributeSupplier.Builder giantOrthoconeAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 15)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.ARMOR, 15)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.MOVEMENT_SPEED, (double) 1.3F);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(0, new PanthalassaDisorientGoal(this, 0.70D));
        this.goalSelector.addGoal(0, new PanthalassaCrushAttackGoal(this, 2.0F));
        this.goalSelector.addGoal(1, new PanthalassaSmartAttackGoal(this, 2.0F, false));
        this.goalSelector.addGoal(2, new PanthalassaEscapeGoal(this, 1.3F));
        this.goalSelector.addGoal(2, new PanthalassaFindWaterGoal(this, 0.15F));
        this.goalSelector.addGoal(3, new PanthalassaRandomSwimmingGoal(this, 0.7F, 10, BLOCKED_DISTANCE));
        //Self-defense target selector
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)));
        //Necessary for ICrushable Entities
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 1, true, false, entity -> (entity.getVehicle() != null)));
        //Player target selector
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, entity -> (entity instanceof Player && !(this.level.getDifficulty() == Difficulty.PEACEFUL) && (entity.isInWater() || entity.level.getFluidState(entity.blockPosition().below()).is(FluidTags.WATER)))));
        //Self-exclusive target selector
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof Player) && !(entity instanceof EntityGiantOrthocone) && (entity.isInWater() || entity.level.getFluidState(entity.blockPosition().below()).is(FluidTags.WATER))) );
    }

    @Override
    public void setCrushCooldown(float crushCooldown) {
        this.entityData.set(CRUSH_COOLDOWN,crushCooldown);
    }
    @Override
    public float getCrushCooldown() {
        return this.entityData.get(CRUSH_COOLDOWN);
    }

    @Override
    public void setCrushingState(boolean isCrushing) {
        this.entityData.set(CRUSHING_STATE,isCrushing);
    }

    @Override
    public boolean getCrushingState() {
        return this.entityData.get(CRUSHING_STATE);
    }

    @Override
    public void setCrushing(boolean isCrushing) {
        this.entityData.set(CRUSHING,isCrushing);
    }

    @Override
    public boolean getCrushing() {
        return this.entityData.get(CRUSHING);
    }

    @Override
    public void setHungerCooldown(float hungerCooldown) {
        this.entityData.set(HUNGER_COOLDOWN, hungerCooldown);
    }

    @Override
    public float getHungerCooldown() {
        return this.entityData.get(HUNGER_COOLDOWN);
    }

    @Override
    public void positionRider(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            float f = 1.0F;
            float f1 = (float)((!this.isAlive() ? (double)0.01F : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset());
            float f2 = 0.0F;

            Vec3 vector3d = (new Vec3((double)f, 0, 0.0D)).yRot(-this.yRot * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
            passenger.setPos(this.getX() + vector3d.x, this.getY() + (double)f1+f2, this.getZ() + vector3d.z);
        }
    }
}