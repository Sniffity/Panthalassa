package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaMeleeAttackGoal;
import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaRandomSwimmingGoal;
import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaSwimmingHelper;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.EntityAnchorArgument;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class EntityKronosaurus extends PanthalassaEntity implements IAnimatable, IMob {

    public static final int PASSIVE_ANGLE = 1;
    public static final int AGGRO_ANGLE = 15;
    public static final int BLOCKED_DISTANCE = 6;
    public static final float SCHOOL_SPEED = 1.0F;
    public static final float SCHOOL_AVOID_RADIUS = 2.0F;
    public static final float MAX_MOVE_SPEED = 0.5F;
    public static int SCHOOL_MAX_SIZE = 4;


    protected static final DataParameter<Integer> AIR_SUPPLY = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.INT);
    protected static final DataParameter<Integer> SCHOOL_ID = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.INT);
    protected static final DataParameter<Boolean> SCHOOLING = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.BOOLEAN);

    private AnimationFactory factory = new AnimationFactory(this);


    public EntityKronosaurus(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.moveControl = new PanthalassaSwimmingHelper(this, BLOCKED_DISTANCE, PASSIVE_ANGLE, AGGRO_ANGLE);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(SCHOOL_ID, 0);
        this.entityData.define(SCHOOLING, Boolean.FALSE);
        this.entityData.define(AIR_SUPPLY, 300);
        super.defineSynchedData();
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
        if (this.isInWater()){
            this.assignMovement(this);
            /*
            this.assignSchool(this);
            if (this.getSchooling()){

            }*/
        }

        new Vector2f((float) this.getDeltaMovement().x, (float) this.getDeltaMovement().z);
        RayTraceContext rayTrace = new RayTraceContext(this.position(), this.position().add((this.getLookAngle()).scale(10)), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null);
        BlockRayTraceResult result = level.clip(rayTrace);
        System.out.println(result.getType());
    }

    protected void assignMovement(EntityKronosaurus entityIn) {

        Vector3d attract = new Vector3d(0,0,0);
        Vector3d repel = new Vector3d(0,0,0);
        Vector3d follow = new Vector3d(0,0,0);
        Vector3d avoid = new Vector3d(0,0,0);
        Vector3d newMovement = new Vector3d(0,0,0);
        //Try to assign a school to this Kronosaurus...
        List<EntityKronosaurus> school = level.getEntitiesOfClass(EntityKronosaurus.class, entityIn.getBoundingBox().inflate(8));
        //Remove this Kronosaurus from the school for operations...
        school.remove(this);
        if (!school.isEmpty()) {
//            attract = (processAttract(school));
//            repel = (processRepel(school));
            follow = (processFollow(school));

            // Now normalize the vectors to get just the directions, and scale by the amount you want each force to have. These values will need fine-tuning
//            attract = attract.normalize().scale(0.6);
//            repel = repel.normalize().scale(1);
            follow = follow.normalize().scale(0.2);

            newMovement = entityIn.getDeltaMovement().add(attract).add(repel).add(follow);
        }

        avoid = processAvoid();
        if (avoid !=null){
            newMovement = avoid.subtract(newMovement);
        }

        // CLAMP THE VECTOR IF ITS MOVING TOO FAST. OTHERWISE THEY WILL ACCELERATE TO INFINITE SPEEDS
        if (newMovement.length() > MAX_MOVE_SPEED){
            newMovement = newMovement.normalize().scale(MAX_MOVE_SPEED);
        }

        this.setDeltaMovement(newMovement);
        this.lookAt(EntityAnchorArgument.Type.EYES, newMovement);
    }



    protected Vector3d processAttract(List<EntityKronosaurus> school) {
        //Calculate the average position of the school, excluding THIS Kronosaurus
        int size = school.size();
        Vector3d positionVector = new Vector3d(0, 0, 0);
        for (int i = 0; i < size && i < SCHOOL_MAX_SIZE; i++) {
            EntityKronosaurus kronosaurus = school.get(i);
            positionVector = positionVector.add(kronosaurus.position());
        }
        Vector3d averagePos = positionVector.scale(1.0f / (float) size);
        //Calculate a new vector... pointing from THIS Kronosaurus, to the center of the school.
        //This vector will be scaled by SCHOOL_SPEED, which is a factor that scales all school "operations"
        Vector3d target = averagePos.subtract(this.position()).normalize().scale(SCHOOL_SPEED);
        //From this vector, subtract the velocity vector of THIS Kronosaurus...
        //This creates a new Vector, attraction, that will attempt to adjust position towards center of group...
        //Taking the speed of THIS Kronosaurus into account
        return target.subtract(this.getDeltaMovement());
    }

    protected Vector3d processRepel(List<EntityKronosaurus> school) {

        Vector3d separation = new Vector3d(0, 0, 0);
        //Create a new list to store Kronosaurus that are too close to THIS Kronosaurus....
        List<EntityKronosaurus> closeKronosaurus = new ArrayList<>();
        int schoolSize = school.size();
        for (int i = 0; i < schoolSize && i < 4; i++) {
            //Get the position of each Kronosaurus in school...
            EntityKronosaurus kronosaurus = school.get(i);
            float distanceToKronosaurus = (float) this.position().subtract(kronosaurus.position()).length();
            //Add those that Kronosaurus are too close to THIS Kronosaurus to the list specified above.
            if (distanceToKronosaurus < SCHOOL_AVOID_RADIUS) {
                closeKronosaurus.add(kronosaurus);
            }
        }
        //If there are no close Kronosaurus, return a zero-vector. No adjustment will be made for avoidance.
        if (closeKronosaurus.isEmpty()) {
            return new Vector3d(0, 0, 0);
        }

        //Given that the list is not empty...
        int closeSize = closeKronosaurus.size();
        for (int i = 0; i < closeSize; i++) {
            EntityKronosaurus kronosaurus = school.get(i);
            //For each Kronosaurus create a vector...
            //This vector points from the CURRENT Kronosaurus being analyzed to THIS Kronosaurus
            Vector3d difference = this.position().subtract(kronosaurus.position());
            //For this vector, normalize it to get a direction..
            //Then scale it down, its length will depend inversely on the distance to THIS Kronosaurus
            separation = separation.add(difference.normalize().scale(1.0f / difference.length()));
        }
        //All of these vectors have been added together...
        //Each Kronosaurus analyzed contributed to these vectors..
        //The resulting vector will be one that pushes THIS Kronosaurus away from all OTHER Kronosaurus
        //Get an average to scale this vector...
        separation = separation.scale(1.0f / closeSize);
        //This vector will be scaled by SCHOOL_SPEED, which is a factor that scales all school "operations"
        Vector3d target = separation.normalize().scale(SCHOOL_SPEED);
        //From this vector, subtract the velocity vector of the Kronosaurus...

        //This creates a new Vector, attraction, that will attempt to adjust position away from center of group...
        //Taking the speed of THIS Kronosaurus into account
        return target.subtract(this.getDeltaMovement());
    }



    protected Vector3d processFollow(List<EntityKronosaurus> school) {
        //Calculate the average speed of the school, excluding THIS Kronosaurus...
        int size = school.size();
        Vector3d speedVector = new Vector3d(0, 0, 0);
        for (int i = 0; i < size && i < 4; i++) {
            EntityKronosaurus kronosaurus = school.get(i);
            speedVector = speedVector.add(kronosaurus.getDeltaMovement());
        }
        Vector3d averageSpeed = speedVector.scale(1.0f / (float) size);
        //This vector will be scaled by SCHOOL_SPEED, which is a factor that scales all school "operations"
        Vector3d target = averageSpeed.subtract(this.position()).normalize().scale(SCHOOL_SPEED);
        //From this vector, subtract the velocity vector of the Kronosaurus...

        //This creates a new Vector, follow, that will attempt to adjust speed of THIS Kronosaurus to the speed of the group...
        //Taking the speed of THIS Kronosaurus into account
        return target.subtract(this.getDeltaMovement());
    }

    protected Vector3d processAvoid() {
        //Once all school vectors have been calculated, or if the Kronosaurus is alone, perform a check around it, 7 block radius.
        AxisAlignedBB searchArea = new AxisAlignedBB(this.getX() - 7, this.getY() - 7, this.getZ() - 7, this.getX() + 7, this.getY() + 7, this.getZ() + 7);
        //Filter only the blocks that canOcclude, solid blocks...
        Set<BlockPos> set = BlockPos.betweenClosedStream(searchArea)
                .map(pos -> new BlockPos(pos))
                .filter(state -> (level.getBlockState(state).canOcclude()))
                .collect(Collectors.toSet());
        Iterator<BlockPos> it = set.iterator();

        //If there's solid blocks around, if the set is not empty, we will perform all the methods for avoidance...
        if (!set.isEmpty()) {
            //Initialize closestPos and distanceToClosestPos
            // The values assigned to closastPos and distanceToClosestPos will always be replaced by something.
            //This is because if we have got to this point, the iterator will run at least once
            BlockPos closestPos = new BlockPos(0, 0, 0);
            float distanceToClosestPos = 100;

            while (it.hasNext()) {
                BlockPos testPos = it.next();
                float distanceToPos = (float) this.position().subtract(testPos.getX(), testPos.getY(), testPos.getZ()).length();
                if (distanceToPos < distanceToClosestPos) {
                    distanceToClosestPos = distanceToPos;
                    closestPos = new BlockPos(testPos.getX(), testPos.getY(), testPos.getZ());
                }
            }

            Vector3d targetAwayFromClosestPos = (this.position()).subtract(closestPos.getX(), closestPos.getY(), closestPos.getZ());
            targetAwayFromClosestPos = targetAwayFromClosestPos.normalize();
            return targetAwayFromClosestPos.subtract(this.getDeltaMovement());
        }
        return null;
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
        this.goalSelector.addGoal(1, new PanthalassaRandomSwimmingGoal(this, 0.7, 10, BLOCKED_DISTANCE));
        this.targetSelector.addGoal(0, (new HurtByTargetGoal(this)));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, entity -> (entity instanceof PlayerEntity && !(this.level.getDifficulty() == Difficulty.PEACEFUL))));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, entity -> !(entity instanceof PlayerEntity) && !(entity instanceof EntityKronosaurus) && !(entity instanceof EntityArchelon)));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 40, true, false, entity -> (entity instanceof EntityArchelon)));

    }


    public void setAirSupplyLocal(int airSupply) {
        this.entityData.set(AIR_SUPPLY,airSupply);
    }

    public int getAirSupplyLocal() {
        return this.entityData.get(AIR_SUPPLY);
    }

    public void setSchooling(boolean schoolStatus) {
        this.entityData.set(SCHOOLING,schoolStatus);
    }

    public boolean getSchooling() {
        return this.entityData.get(SCHOOLING);
    }


}