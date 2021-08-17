package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaMeleeAttackGoal;
import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaRandomSwimmingGoal;
import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaSwimmingHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.*;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class EntityKronosaurus extends PanthalassaEntity implements IAnimatable, IMob {

    public int blockDistance = 3;
    public int passiveAngle = 4;
    public int aggroAngle = 8;

    protected static final DataParameter<Integer> AIR_SUPPLY = EntityDataManager.defineId(EntityMegalodon.class, DataSerializers.INT);
    private AnimationFactory factory = new AnimationFactory(this);


    public EntityKronosaurus(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.moveControl = new PanthalassaSwimmingHelper(this, blockDistance, passiveAngle, aggroAngle);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        //If it's moving in the water, swimming, play swim.
        if ((this.isSwimming()) || (event.isMoving() && this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kronosaurus.swim", true));
            return PlayState.CONTINUE;
        }
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
        this.goalSelector.addGoal(0, new PanthalassaMeleeAttackGoal(this, 2.0, false));
        this.goalSelector.addGoal(1, new PanthalassaRandomSwimmingGoal(this, 0.7, 10, blockDistance));
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, entity -> (entity instanceof PlayerEntity && !(this.level.getDifficulty() == Difficulty.PEACEFUL))));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof PlayerEntity) && !(entity instanceof EntityKronosaurus) && !(entity instanceof EntityArchelon)));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> (entity instanceof EntityArchelon)));

    }

    public void setAirSupplyLocal(int airSupply) {
        this.entityData.set(AIR_SUPPLY,airSupply);
    }

    public int getAirSupplyLocal() {
        return this.entityData.get(AIR_SUPPLY);
    }
}