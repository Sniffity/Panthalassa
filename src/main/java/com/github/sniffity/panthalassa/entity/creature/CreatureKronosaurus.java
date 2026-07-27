package com.github.sniffity.panthalassa.entity.creature;

import com.github.sniffity.panthalassa.entity.creature.behaviour.goals.PanthalassaRandomSwimmingGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class CreatureKronosaurus extends PanthalassaCreature{
    public CreatureKronosaurus(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected boolean speciesUnderwaterBreathing() {
        return false;
    }

    @Override
    protected boolean speciesDynamicYaw() {
        return true;
    }

    @Override
    protected boolean speciesDynamicPitch() {
        return true;
    }

    @Override
    protected boolean speciesAmphibious() {
        return false;
    }

    @Override
    protected boolean speciesReturnsToWater() {
        return false;
    }

    public void registerGoals() {
        this.goalSelector.addGoal(4, new PanthalassaRandomSwimmingGoal(this, 0.7, 50, 20,5,20));
    }

}