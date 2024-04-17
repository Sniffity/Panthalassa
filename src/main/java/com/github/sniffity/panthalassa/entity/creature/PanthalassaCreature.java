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

    @Override
    public void tick(){
        super.tick();
    }

    //SPECIES TYPE:
    protected abstract boolean speciesUnderwaterBreathing();

    
}
