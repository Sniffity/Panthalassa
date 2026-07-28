package com.github.sniffity.panthalassa.entity.creature;

import com.github.sniffity.panthalassa.entity.creature.behaviour.goals.PanthalassaRandomSwimmingGoal;
import com.github.sniffity.panthalassa.entity.creature.behaviour.movement.PanthalassaMoveControl;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class PanthalassaCreature extends PathfinderMob implements GeoEntity {


    public boolean isLandNavigator;
    public float rotationPitch;
    public float prevRotationPitch;
    public float prevYRot;
    public float deltaYRot;
    public float adjustYaw;
    public boolean canBreatheOutsideWater;
    public float prevSetYaw;
    public float setYaw;
    private float prevYawRot;
    private float deltaYawRot;

    private BlockPos swimTarget;

    private int yawTickCounter;


    private final Vec3 center = new Vec3(0, 5, 0);
    private double angle = 0; // in radians
    private float radius;
    private float figure;
    private final double speed = 0.03; // radians per tick

    private double prevAngle = 0.0;
    public double angularSpeedEstimate = 0.0;


    protected PanthalassaCreature(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
        this.moveControl = new PanthalassaMoveControl(this, 85, 1F, 1F,true);
        this.navigation = new WaterBoundPathNavigation(this,this.level());
        this.lookControl = new SmoothSwimmingLookControl(this,10);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    private static final EntityDataAccessor<Optional<BlockPos>> SWIM_TARGET =
            SynchedEntityData.defineId(
                    PanthalassaCreature.class,
                    EntityDataSerializers.OPTIONAL_BLOCK_POS
            );
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SWIM_TARGET, Optional.empty());
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }


    // =========================================
    // ANIMATION METHODS
    // =========================================
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controllerLocomotion", 5, this::locomotionAnimController));
    }
    @Override

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("Radius")) {
            this.radius = tag.getFloat("Radius");
        }

        if (tag.contains("Figure")) {
            this.figure = tag.getFloat("Figure");
        }
    }


    protected <E extends PanthalassaCreature> PlayState locomotionAnimController(final AnimationState<E> event) {
        //locomotion Methods
        return PlayState.STOP;
    }


    // =========================================
    // SPECIES METHODS
    // =========================================
    protected abstract boolean speciesUnderwaterBreathing();

    protected abstract boolean speciesDynamicYaw();

    protected abstract boolean speciesDynamicPitch();

    protected abstract boolean speciesAmphibious();

    protected abstract boolean speciesReturnsToWater();


    @Override
    public void tick() {
        super.tick();
        //tickMotion();

        /*
        handleDynamicPitchOperations();
        if (speciesUnderwaterBreathing()) {
        }
        if (speciesDynamicYaw()) {
        }
        if (speciesDynamicPitch()) {
        }

        if (speciesAmphibious()) {
        }
        if (speciesReturnsToWater()) {
        }
         */


    }

    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.01F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(travelVector);
        }
    }

    public void tickMotion() {
        handleDynamicPitchOperations();

        if (speciesUnderwaterBreathing()) {

        }

        if (speciesDynamicYaw()) {
        }

        if (speciesDynamicPitch()) {
        }

        if (speciesAmphibious()) {

        }

        if (speciesReturnsToWater()) {

        }

        if (level().isClientSide){
            handleDynamicYawOperations();
        }


    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return type != NeoForgeMod.WATER_TYPE.value();
    }

    private void handleDynamicYawOperations() {
        float adjustment = 0.10F;

        float rawDelta = Mth.wrapDegrees(this.yBodyRot - prevYawRot);
        prevYawRot = this.yBodyRot;

        if (adjustYaw > rawDelta) {
            adjustYaw = Math.max(adjustYaw - adjustment, rawDelta);
        } else if (adjustYaw < rawDelta) {
            adjustYaw = Math.min(adjustYaw + adjustment, rawDelta);
        }

        prevSetYaw = setYaw;

        setYaw = (adjustYaw) * ((float)Math.PI/180F);

    }

    private void figureMotion(){
        angle += speed;
        /*
        if (angle > 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }

         */

        double targetX;
        double targetZ;

        if (figure == 1) {
            targetX = center.x + radius * Math.sin(angle);
            targetZ = center.z + radius * Math.sin(angle) * Math.cos(angle);
        } else {
            targetX = center.x + radius * Math.cos(angle);
            targetZ = center.z + radius * Math.sin(angle);
        }

        double motionX = targetX - this.getX();
        double motionZ = targetZ - this.getZ();

        this.setDeltaMovement(motionX * 0.1, 0, motionZ * 0.1);

        Vec3 motion = this.getDeltaMovement();
        double dx = motion.x;
        double dz = motion.z;
        float yaw = (float) Math.toDegrees(-Math.atan2(-dx, dz));


            /*
            if (motion.lengthSqr() > 1.0E-6) {
                double currAngle = Math.atan2(motion.z, motion.x); // yaw angle in radians
                double deltaAngle = currAngle - prevAngle;

                // Normalise to [-π, π]
                deltaAngle = Mth.wrapDegrees(Math.toDegrees(deltaAngle))  * (Math.PI / 180);;

                angularSpeedEstimate = deltaAngle; // radians per tick
                prevAngle = currAngle;
            }

             */

        //this.setYRot(yaw);
        this.setYBodyRot(-yaw-90F);
        //this.setYHeadRot(-yaw);   // Optional: keeps head aligned

    }


    private void handleDynamicPitchOperations() {
        prevRotationPitch = rotationPitch;
        rotationPitch = (float) ((Math.atan2((this.getDeltaMovement().y), Math.sqrt((float) ((this.getDeltaMovement().x) * (this.getDeltaMovement().x) + (this.getDeltaMovement().z) * (this.getDeltaMovement().z))))));
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(4, new PanthalassaRandomSwimmingGoal(this, 0.7, 50, 5,5,20));
    }


    public void setSwimTarget(@Nullable BlockPos pos) {
        entityData.set(SWIM_TARGET, Optional.ofNullable(pos));
    }

    @Nullable
    public BlockPos getSwimTarget() {
        return entityData.get(SWIM_TARGET).orElse(null);
    }
}