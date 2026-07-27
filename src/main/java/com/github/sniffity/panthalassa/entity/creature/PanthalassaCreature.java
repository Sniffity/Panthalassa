package com.github.sniffity.panthalassa.entity.creature;

import com.github.sniffity.panthalassa.entity.creature.behaviour.movement.PanthalassaMoveControl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

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
        this.navigation = new GroundPathNavigation(this,this.level());
        this.moveControl = new PanthalassaMoveControl(this, 85, 1F, 1F, true);
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
        controllers.add(new AnimationController<>(this, "controllerAbility", 5, this::abilityAnimController));
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



    protected static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("move.fly");

    protected <E extends PanthalassaCreature> PlayState abilityAnimController(final AnimationState<E> event) {

        /*
        //BoneType: INVISIBLE Bones

        //All Ability Animations to be played are stored as DataParameters Strings
        //We begin by getting the animation that should be played.
        //This string may have been set as part of an AnimatedGoal that requires a one-shot ability animation to play..
        String animation = this.getAnimation();
        //If we do have a one-shot ability animation, we will play that.
        //"base" is the null value for getAnimation
        if (!animation.equals("base")) {
            //If we do have an ability animation, we get the type (Loop (1), Play once (2), Hold on last frame (3))
            int animationType = this.getAnimationType();
            RawAnimation abilityAnimation;

            switch (animationType) {
                case 1 -> abilityAnimation = RawAnimation.begin().then(animation, Animation.LoopType.LOOP);
                case 2 -> abilityAnimation = RawAnimation.begin().then(animation,Animation.LoopType.PLAY_ONCE);
                case 3 -> abilityAnimation = RawAnimation.begin().then(animation,Animation.LoopType.HOLD_ON_LAST_FRAME);
                default -> {return PlayState.STOP;}
            }

            //We proceed to play the corresponding animation...
            return event.setAndContinue(abilityAnimation);
        }
        //Else, just return base:
        //This will not cause a transition to a stiff pose, as walking animations will be running concurrently...
        //We are only resetting the position of the iBones/Bones here
        return event.setAndContinue(RawAnimation.begin().then("base",Animation.LoopType.LOOP));

         */
        return PlayState.STOP;
    }

    protected <E extends PanthalassaCreature> PlayState locomotionAnimController(final AnimationState<E> event) {
        //locomotion Methods
        return PlayState.STOP;
    }

    /*

    public void setAnimation(String animation) {
        entityData.set(ANIMATION,animation);
    }

    public String getAnimation() {
        return entityData.get(ANIMATION);
    }

    public void setAnimationType(int animationType) {
        entityData.set(ANIMATION_TYPE,animationType);
    }

    public int getAnimationType(){
        return entityData.get(ANIMATION_TYPE);
    }

     */



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
        tickMotion();

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
        System.out.println("tick pos=(" + this.getX() + "," + this.getZ() + ") motion=(" + dx + "," + dz + ") yaw=" + yaw);


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


        if (level().isClientSide){
            handleDynamicYawOperations();
        }

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

    private void handleDynamicPitchOperations() {
        prevRotationPitch = rotationPitch;
        rotationPitch = (float) ((Math.atan2((this.getDeltaMovement().y), Math.sqrt((float) ((this.getDeltaMovement().x) * (this.getDeltaMovement().x) + (this.getDeltaMovement().z) * (this.getDeltaMovement().z))))));
    }

    @Override
    public void registerGoals() {
        //this.goalSelector.addGoal(1, new RandomStrollGoal(this, 2.0F,1,false));

    }

    @Override
    public boolean isNoGravity() {
        return false;
    }
}