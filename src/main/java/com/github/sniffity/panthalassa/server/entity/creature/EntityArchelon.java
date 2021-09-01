package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaEscapeGoal;
import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaMeleeAttackGoal;
import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaRandomSwimmingGoal;
import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaSwimmingHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.FindWaterGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.fish.CodEntity;
import net.minecraft.entity.passive.fish.SalmonEntity;
import net.minecraft.entity.passive.fish.TropicalFishEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
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
    public static final int BLOCKED_DISTANCE = 2;


    private AnimationFactory factory = new AnimationFactory(this);

    public EntityArchelon(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        this.setPathfindingMalus(PathNodeType.WATER_BORDER, 0.0F);
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
        this.goalSelector.addGoal(0, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(1, new PanthalassaRandomSwimmingGoal(this, 0.7, 10, BLOCKED_DISTANCE));
        this.goalSelector.addGoal(2, new PanthalassaEscapeGoal(this, 1.3));
        this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 0.1, 30));
        this.goalSelector.addGoal(3, new FindWaterGoal(this));
        this.goalSelector.addGoal(4, new PanthalassaMeleeAttackGoal(this, 1.3, false));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> (entity instanceof CodEntity || entity instanceof SalmonEntity || entity instanceof TropicalFishEntity)));

    }

}
