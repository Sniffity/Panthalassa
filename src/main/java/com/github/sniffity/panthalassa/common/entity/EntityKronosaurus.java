package com.github.sniffity.panthalassa.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;


public class EntityKronosaurus extends PanthalassaEntity implements IAnimatable, IMob {

    private AnimationFactory factory = new AnimationFactory(this);

    public EntityKronosaurus(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.ignoreFrustumCheck = true;
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.kronosaurus.swimming", true));
            return PlayState.CONTINUE;
        } else {
            return PlayState.STOP;
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
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_HOGLIN_DEATH;
    }

    public static AttributeModifierMap.MutableAttribute kronosaurusAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 25)
                .createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 20)
                .createMutableAttribute(Attributes.MAX_HEALTH, 150)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, (double) 1.0F);
    }

    public void registerGoals() {
        Float speedGoals = 1.0F;
        super.registerGoals();
        this.goalSelector.addGoal(2, new RandomWalkingGoal(this, speedGoals, 70));
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, speedGoals, false));
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, speedGoals, 70));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof PlayerEntity || entity instanceof EntityKronosaurus)));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, true, entity -> (entity instanceof PlayerEntity && !(this.world.getDifficulty() == Difficulty.PEACEFUL))));
    }
}
/*
this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
                predicate -> {
                    predicate.getType();
                    return predicate.isInWater() == isInWater();
                }));
 */
