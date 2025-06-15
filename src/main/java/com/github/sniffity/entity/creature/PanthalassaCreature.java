package com.github.sniffity.entity.creature;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import static java.lang.Math.PI;

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


    private final Vec3 center = new Vec3(0, 5, 0);
    private double angle = 0; // in radians
    private final double radius = 15;
    private final double speed = 0.01; // radians per tick

    protected PanthalassaCreature(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.lookControl = new LookControl(this) {
            @Override
            public void tick() {
                // Do nothing — disables automatic look updates
            }

            @Override
            public void setLookAt(double x, double y, double z) {
                // Do nothing — disables manual look-at
            }

            @Override
            public void setLookAt(double x, double y, double z, float deltaYaw, float deltaPitch) {
                // Do nothing
            }

            @Override
            public void setLookAt(Entity entity, float deltaYaw, float deltaPitch) {
                // Do nothing
            }
        };

    }

/*
    public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(PanthalassaCreature.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> ANIMATION_TYPE = SynchedEntityData.defineId(PanthalassaCreature.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
        entityData.define(ANIMATION,"base");
        entityData.define(ANIMATION_TYPE,0);

    }

 */

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
        handleDynamicPitchOperations();

        super.tick();
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
        if (angle > 2 * Math.PI) angle -= 2 * Math.PI;

        double targetX = center.x + radius * Math.cos(angle);
        double targetZ = center.z + radius * Math.sin(angle);

        double motionX = targetX - this.getX();
        double motionZ = targetZ - this.getZ();

        this.setDeltaMovement(motionX * 0.1, 0, motionZ * 0.1);


        Vec3 motion = this.getDeltaMovement();
        double dx = motion.x;
        double dz = motion.z;
        float yaw = (float)(Math.atan2(dz, dx) * (180F / Math.PI))+90;

        //this.setYRot(yaw);

        //this.setYRot(yaw);
        this.setYBodyRot(yaw);
        //this.setYHeadRot(-yaw);   // Optional: keeps head aligned
        if (level().isClientSide){
            handleDynamicYawOperations();
        }
    }


    private void handleDynamicYawOperations() {
        float adjustment = 0.05F;
        //YAW OPERATIONS:
        //The following lines of code handle the dynamic yaw animations for entities...

        //Grab the change in the entity's Yaw, deltaYRot...
        //deltaYaw will tell us in which direction the entity is rotating...
        deltaYawRot = this.yBodyRot - prevYawRot;
        //System.out.print("DeltaYawRot: "+deltaYawRot);
        //Store the previous yaw value, so we can use it next tick to calculate deltaYaw...
        prevYawRot = this.yBodyRot;
        //System.out.print("prevYawRot: "+prevYawRot);


        //adjustYaw is a local variable that changes to try and match the change in Yaw....
        //This is what we will set each bone's yaw to...
        //So, adjustYaw starts at 0.
        // If it's rotating in the negative direction (deltaYRot negative), adjustYaw will start decreasing to catch up...
        // Likewise, if it's rotating in the positive direction (deltaYRot positive) adjustYaw will start increasing to catch up...

        //The increase or decrease always depends on the adjustment variable. This determines how "fast" adjustYaw will catch up.
        //The max and min functions ensure that adjustYaw doesn't overshoot deltaYRot...
        //Thus, adjustment will determine --how fast-- the pieces of the entity's model change their rotation.
        //The multiplying factor in the corresponding entity's model will determine --how far-- they rotate.

        if (adjustYaw > deltaYawRot) {
            adjustYaw = adjustYaw - adjustment;

            adjustYaw = Math.max(adjustYaw, deltaYawRot);
        } else if (adjustYaw < deltaYawRot) {
            adjustYaw = adjustYaw + adjustment;
            adjustYaw = Math.min(adjustYaw, deltaYawRot);

        }


        //We store the prevAdjustYaw value and use this and the current adjustYaw value for partial tick methods.
        prevSetYaw = setYaw;
        //Finally, the yaw value is converted to radians, for use in the model class
        setYaw = (float) (adjustYaw * (PI / 180.0F));

        //Troubleshooting:
        // If the rotation "lags behind" (does not change directions fast enough) increase adjustment.
        // If the rotation looks choppy (adjusts too fast), decrease adjustment
        // If the entity seems to "dislocate", reduce the multipliers for bone rotation in the Model class.
        // Reducing rotation multiplier in model class can also reduce choppiness, at the cost of how wide the bone rotation is.
    }

    private void handleDynamicPitchOperations() {
        prevRotationPitch = rotationPitch;
        rotationPitch = (float) ((Math.atan2((this.getDeltaMovement().y), Math.sqrt((float) ((this.getDeltaMovement().x) * (this.getDeltaMovement().x) + (this.getDeltaMovement().z) * (this.getDeltaMovement().z))))));
    }

    @Override
    public void registerGoals() {

    }
}