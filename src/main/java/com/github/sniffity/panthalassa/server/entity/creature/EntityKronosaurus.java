package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaMeleeAttackGoal;
import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaRandomSwimmingGoal;
import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaSwimmingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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

public class EntityKronosaurus extends PanthalassaEntity implements IAnimatable, IMob {

    public static final int PASSIVE_ANGLE = 1;
    public static final int AGGRO_ANGLE = 15;
    protected ArrayList<EntityKronosaurus> school = new ArrayList<>();
    public static final int AVOID_DISTANCE = 6;


    protected static final DataParameter<Integer> AIR_SUPPLY = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.INT);
    protected static final DataParameter<Integer> SCHOOL_ID = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.INT);
    protected static final DataParameter<Boolean> SCHOOLING = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.BOOLEAN);

    private AnimationFactory factory = new AnimationFactory(this);


    public EntityKronosaurus(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.moveControl = new PanthalassaSwimmingHelper(this, AVOID_DISTANCE, PASSIVE_ANGLE, AGGRO_ANGLE);
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

    public static boolean canKronosaurusSpawn(EntityType<? extends PanthalassaEntity> type,IWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn) {
        for (int x = -3; x < 4; x++) {
            for (int y = -3; y < 4; y++) {
                for (int z = -3; z < 4; z++) {
                    if (!worldIn.getFluidState((pos.offset(x, y, z))).is(FluidTags.WATER)) {
                        return false;
                    }
                }
            }
        }
        return true;
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
        if (!this.getSchooling()) {
            this.tryAssignSchool(this);
        } //else {
//            this.schoolMovement(this);
//        }



    }

    protected void tryAssignSchool(EntityKronosaurus entityIn){
        //Each tick, for each Kronosaurus, get all entities within a 40*40*40 bounding box

        List<Entity> entities = level.getEntitiesOfClass(EntityKronosaurus.class, this.getBoundingBox().inflate(20));

        //With the acquired list, if it's size is bigger than 1 (meaning, if it's a "group" of Kronosaurus...
        // ...select the first 3 Kronosaurus or 2 Kronosaurus...
        if (entities.size()>1) {
            for (int k = 0; k < entities.size() && k < 3; k++) {
                Entity entity = entities.get(k);
                EntityKronosaurus kronosaurus = (EntityKronosaurus) entity;
                //...and add them to the List "school.
                if (!kronosaurus.getSchooling()){
                    school.add(kronosaurus);

                }
            }
        }

        //For each Kronosaurus in the school, set the SCHOOLING NBT tag to true.
        for (int m = 0; m < school.size(); m++) {
            EntityKronosaurus kronosaurus = (EntityKronosaurus) school.get(m);
            kronosaurus.setSchooling(true);
        }
    }

    protected void schoolMovement(EntityKronosaurus entityIn) {
        EntityKronosaurus kronosaurus1;
        EntityKronosaurus kronosaurus2;
        EntityKronosaurus kronosaurus3;
        int size = school.size();
        if (size ==3) {
            //Get each Kronosaurus...
            kronosaurus1 = school.get(0);
            kronosaurus2 = school.get(1);
            kronosaurus3 = school.get(2);

            //And the position of each Kronosaurus
            kronosaurus1.getPosition(0);
            Vector3d kronosaurus1Pos = (kronosaurus1.position());
            Vector3d kronosaurus2Pos = (kronosaurus2.position());
            Vector3d kronosaurus3Pos = (kronosaurus3.position());

            //Calculate the "average school position"...
            Vector3d averagePos = (kronosaurus1Pos.add(kronosaurus2Pos).add(kronosaurus3Pos)).multiply( 1.0F/3.0F,1.0F/3.0F,1.0F/3.0F);
            //The position of this Kronosaurus...
            Vector3d thisPosition = this.position();

            //Distance from this Kronosaurus to the average position...
            double distanceThisToAveragePosition = averagePos.subtract(thisPosition).length();

            //Target (unit) vector from this Kronosaurus to the average position...
            Vector3d targetAveragePosition = averagePos.subtract(thisPosition).normalize();

            //If this Kronosaurus moves too far away from the average position...
            while (distanceThisToAveragePosition>5) {
                //Push it back in towards the average position...
                this.setDeltaMovement(this.getDeltaMovement().add(targetAveragePosition));
                thisPosition = new Vector3d(this.getX(),this.getY(),this.getZ());
                distanceThisToAveragePosition = averagePos.subtract(thisPosition).length();
            }

        } else {
            //Same methods, but for a school of 2.
            kronosaurus1 = school.get(0);
            kronosaurus2 = school.get(1);

            Vector3d kronosaurus1Pos = new Vector3d(kronosaurus1.getX(),kronosaurus1.getY(),kronosaurus1.getZ());
            Vector3d kronosaurus2Pos = new Vector3d(kronosaurus2.getX(),kronosaurus2.getY(),kronosaurus2.getZ());

            Vector3d averagePos = new Vector3d((kronosaurus1Pos.x+kronosaurus2Pos.x)/2,(kronosaurus1Pos.y+kronosaurus2Pos.y)/2,(+kronosaurus1Pos.z+kronosaurus2Pos.z)/2);
            Vector3d thisPosition = new Vector3d(this.getX(),this.getY(),this.getZ());
            double distanceThisToAveragePosition = averagePos.subtract(thisPosition).length();
            Vector3d targetAveragePosition = averagePos.subtract(thisPosition).normalize();
            while (distanceThisToAveragePosition>5) {
                this.setDeltaMovement(this.getDeltaMovement().add(targetAveragePosition.x,targetAveragePosition.y,targetAveragePosition.z));
                thisPosition = new Vector3d(this.getX(),this.getY(),this.getZ());
                distanceThisToAveragePosition = averagePos.subtract(thisPosition).length();
            }
        }
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
        this.goalSelector.addGoal(1, new PanthalassaRandomSwimmingGoal(this, 0.7, 10, AVOID_DISTANCE));
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