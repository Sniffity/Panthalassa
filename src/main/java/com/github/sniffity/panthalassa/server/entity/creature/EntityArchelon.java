package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaRandomSwimmingGoal;
import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaSwimmingHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.FindWaterGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
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

public class EntityArchelon extends PanthalassaEntity implements IAnimatable, IMob {
    public static final int PASSIVE_ANGLE = 4;
    public static final int AGGRO_ANGLE = 8;
    public static final int AVOID_DISTANCE = 3;


    private AnimationFactory factory = new AnimationFactory(this);

    public EntityArchelon(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.moveControl = new PanthalassaSwimmingHelper(this, AVOID_DISTANCE, PASSIVE_ANGLE, AGGRO_ANGLE);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        //If it's out of the water, play bounce
        if ((this.isOnGround()) && !(this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.archelon.test", true));
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
    }

    public static AttributeModifierMap.MutableAttribute archelonAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.ARMOR, 20)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MAX_HEALTH, 60)
                .add(Attributes.MOVEMENT_SPEED, (double) 1.0F);
    }

    public void registerGoals() {
        this.goalSelector.addGoal(1, new PanthalassaRandomSwimmingGoal(this, 0.7, 10, AVOID_DISTANCE));
        this.goalSelector.addGoal(1, new RandomWalkingGoal(this, 0.3, 10));
        this.goalSelector.addGoal(2, new FindWaterGoal(this));

    }
}