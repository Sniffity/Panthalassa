package com.github.sniffity.panthalassa.entity.creature;

import com.github.sniffity.panthalassa.entity.creature.behaviour.goals.PanthalassaRandomSwimmingGoal;
import com.github.sniffity.panthalassa.entity.creature.behaviour.movement.PanthalassaMoveControl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;

public class CreatureKronosaurus extends PanthalassaCreature{

    public float waterSpeed = 15F;
    public float waterDrag = 90F;

    public CreatureKronosaurus(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new PanthalassaMoveControl(this, 90, 2F, 2F,true);
        this.navigation = new WaterBoundPathNavigation(this,this.level());
        this.lookControl = new SmoothSwimmingLookControl(this,10);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
    }

    @Override
    protected boolean speciesUnderwaterBreathing() {
        return true;
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
        return true;
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(4, new PanthalassaRandomSwimmingGoal(this, 0.7, 50, 5,5,20));
    }

}