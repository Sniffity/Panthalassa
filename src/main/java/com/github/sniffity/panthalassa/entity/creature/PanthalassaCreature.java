package com.github.sniffity.panthalassa.entity.creature;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

import static java.lang.Math.PI;

public abstract class PanthalassaCreature extends PathfinderMob {
    protected PanthalassaCreature(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private boolean isLandNavigator;
    private float rotationPitch;
    private float prevRotationPitch;
    private float prevYawRot;
    private float deltaYawRot;
    public float adjustYaw;
    private float adjustment;
    private boolean canBreatheOutsideWater;
    private float prevSetYaw;
    private float setYaw;

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
    }



    // =========================================
    // Species Methods
    // =========================================
    protected abstract boolean speciesUnderwaterBreathing();

    protected abstract boolean speciesDynamicYaw();

    protected abstract boolean speciesDynamicPitch();

    protected abstract boolean speciesAmphibious();

    protected abstract boolean speciesReturnsToWater();

    @Override
    public void tick(){
        super.tick();
        if (speciesUnderwaterBreathing()) {

        }

        if (speciesDynamicYaw()) {
            handleDynamicYawOperations();
        }

        if (speciesDynamicPitch()) {
            handleDynamicPitchOperations();
        }

        if (speciesAmphibious()) {

        }

        if (speciesReturnsToWater()) {

        }
    }

    private void handleDynamicYawOperations(){
        //YAW OPERATIONS:
        //The following lines of code handle the dynamic yaw animations for entities...
        //Grab the change in the entity's Yaw, deltaYRot...
        //deltaYaw will tell us in which direction the entity is rotating...
        deltaYawRot = this.yRot - prevYawRot;
        //Store the previous yaw value, so we can use it next tick to calculate deltaYaw...
        prevYawRot = this.yRot;
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
        setYaw = (float) (adjustYaw*(PI/180.0F));
        //Troubleshooting:
        // If the rotation "lags behind" (does not change directions fast enough) increase adjustment.
        // If the rotation looks choppy (adjusts too fast), decrease adjustment
        // If the entity seems to "dislocate", reduce the multipliers for bone rotation in the Model class.
        // Reducing rotation multiplier in model class can also reduce choppiness, at the cost of how wide the bone rotation is.
    }

    private void handleDynamicPitchOperations() {
        prevRotationPitch = rotationPitch;
        rotationPitch = (float)((Math.atan2((this.getDeltaMovement().y),Math.sqrt((float) ((this.getDeltaMovement().x)*(this.getDeltaMovement().x)+(this.getDeltaMovement().z)*(this.getDeltaMovement().z))))));
    }

}
