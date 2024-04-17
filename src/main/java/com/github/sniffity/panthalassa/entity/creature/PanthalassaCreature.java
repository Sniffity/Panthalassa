package com.github.sniffity.panthalassa.entity.creature;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public abstract class PanthalassaCreature extends PathfinderMob {
    protected PanthalassaCreature(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData(){
        super.defineSynchedData();
    }



    // =========================================
    // Species Methods
    // =========================================
    protected abstract boolean speciesUnderwaterBreathing();

    protected abstract boolean speciesDynamicYaw();

    protected abstract boolean speciesAmphibious();

    @Override
    public void tick(){
        super.tick();
        if (speciesUnderwaterBreathing()) {

        }

        if (speciesDynamicYaw()) {

        }

        if (speciesAmphibious()) {

        }
    }

    private void yawOperations(){
        //YAW OPERATIONS:
        //The following lines of code handle the dynamic yaw animations for entities...

        //Grab the change in the entity's Yaw, deltaYRot...
        //deltaYaw will tell us in which direction the entity is rotating...
        //PENDING
        //Store the previous yaw value, so we can use itn ext tick to calculate deltaYaw...
        //PENDING

    }

}
