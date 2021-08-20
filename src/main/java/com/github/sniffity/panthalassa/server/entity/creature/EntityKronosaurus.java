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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EntityKronosaurus extends PanthalassaEntity implements IAnimatable, IMob {

    public int blockDistance = 3;
    public int passiveAngle = 4;
    public int aggroAngle = 8;

    protected static final DataParameter<Integer> AIR_SUPPLY = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.INT);
    protected static final DataParameter<Integer> SCHOOL_ID = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.INT);
    protected static final DataParameter<Boolean> SCHOOL = EntityDataManager.defineId(EntityKronosaurus.class, DataSerializers.BOOLEAN);

    private AnimationFactory factory = new AnimationFactory(this);


    public EntityKronosaurus(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.moveControl = new PanthalassaSwimmingHelper(this, blockDistance, passiveAngle, aggroAngle);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(SCHOOL_ID, 0);
        this.entityData.define(SCHOOL, Boolean.FALSE);
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
//        if (!this.getSchooling()) {
//            this.tryAssignSchool(this);
//        } else {
//            this.schoolMovement(this);
//        }

//       System.out.println("My School ID is: " +this.getSchoolId());
//       System.out.println("Is Schooling? " +this.getSchooling());


    }
/*
    protected void tryAssignSchool(EntityKronosaurus entityIn){
        //Each tick, for each Kronosaurus, get all entities within a 40*40*40 bounding box

        List<Entity> entities = level.getEntities(entityIn, new AxisAlignedBB(this.getX() - 20, this.getY() - 20, this.getZ() - 20, this.getX() + 20, this.getY() + 20, this.getZ() + 20));

        //Then, filter that list to select only other EntityKronosaurus, that are not schooling
        ArrayList<EntityKronosaurus> school = new ArrayList<>();

        for (int k = 0; k < entities.size(); k++) {
            Entity entity = entities.get(k);
            if (entity instanceof EntityKronosaurus && !((EntityKronosaurus) entity).getSchooling()) {
                EntityKronosaurus kronosaurus = (EntityKronosaurus) entity;
                school.add(kronosaurus);
            }
        }

        //And cap the list size at 3. Remove anything that would make the list greater than 3.
        if (school.size()>3){
            school.subList(3, school.size()).clear();
        }

        //Only create a new school if the list of potential candidates is 3. Do not creates schools of 2, or greater than 3.
        if (school.size() != 3) {
            school = null;
        }

        //Assign a school ID if the filters are passed. These 3 Kronosaurus will now be treated as a school.
        int potentialSchoolID = this.getRandom().nextInt(1000);
        if (school != null) {
            for (int m = 0; m < school.size(); m++) {
                EntityKronosaurus kronosaurus = (EntityKronosaurus) school.get(m);
                kronosaurus.setSchooling(true);
                kronosaurus.setSchoolId(potentialSchoolID);
            }
        }
    }


    protected void schoolMovement(EntityKronosaurus entityIn) {

        int schoolId = this.getSchoolId();
        List<Entity> entities = level.getEntities(entityIn, new AxisAlignedBB(this.getX() - 20, this.getY() - 20, this.getZ() - 20, this.getX() + 20, this.getY() + 20, this.getZ() + 20));
        ArrayList school = new ArrayList<EntityKronosaurus>();
        for (int k = 0; k < entities.size(); k++) {
            Entity entity = entities.get(k);
            if (entity instanceof EntityKronosaurus && ((EntityKronosaurus) entity).getSchoolId() == schoolId) {
                school.add(entity);
            }
        }

        EntityKronosaurus kronosaurus0 = (EntityKronosaurus) school.get(0);
        EntityKronosaurus kronosaurus1 = (EntityKronosaurus) school.get(2);
        EntityKronosaurus kronosaurus2 = (EntityKronosaurus) school.get(3);
        Vector3d kronosaurus0Pos = new Vector3d(kronosaurus0.getX(),kronosaurus0.getY(),kronosaurus0.getZ());
        Vector3d kronosaurus1Pos = new Vector3d(kronosaurus1.getX(),kronosaurus1.getY(),kronosaurus1.getZ());
        Vector3d kronosaurus2Pos = new Vector3d(kronosaurus2.getX(),kronosaurus2.getY(),kronosaurus2.getZ());
        Vector3d averagePos = new Vector3d((kronosaurus0Pos.x+kronosaurus1Pos.x+kronosaurus2Pos.x)/3,(kronosaurus0Pos.y+kronosaurus1Pos.y+kronosaurus2Pos.y)/3,(kronosaurus0Pos.z+kronosaurus1Pos.z+kronosaurus2Pos.z)/3);

        Vector3d thisPosition = new Vector3d(this.getX(),this.getY(),this.getZ());
        double distanceAveragePosition = averagePos.subtract(thisPosition).length();
        Vector3d targetAveragePosition = averagePos.subtract(thisPosition).normalize();

        while (distanceAveragePosition>5) {
            this.setDeltaMovement(this.getDeltaMovement().add(targetAveragePosition.x,targetAveragePosition.y,targetAveragePosition.z));
            thisPosition = new Vector3d(this.getX(),this.getY(),this.getZ());
            distanceAveragePosition = averagePos.subtract(thisPosition).length();
        }
    }
*/
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
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 40, true, false, entity -> (entity instanceof EntityArchelon)));

    }


    public void setAirSupplyLocal(int airSupply) {
        this.entityData.set(AIR_SUPPLY,airSupply);
    }

    public int getAirSupplyLocal() {
        return this.entityData.get(AIR_SUPPLY);
    }

    public void setSchooling(boolean schoolStatus) {
        this.entityData.set(SCHOOL,schoolStatus);
    }

    public boolean getSchooling() {
        return this.entityData.get(SCHOOL);
    }

    public void setSchoolId(int id) {
        this.entityData.set(SCHOOL_ID,id);
    }

    public int getSchoolId() {
        return this.entityData.get(SCHOOL_ID);
    }
}