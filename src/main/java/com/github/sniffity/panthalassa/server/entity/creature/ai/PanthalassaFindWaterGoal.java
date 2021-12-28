package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.fluid.Fluid;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Random;

/**
 * Panthalassa Mod - Class: PanthalassaFindWaterGoal <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed imitating the methods used in Ice and FIre SeaSerpentAIGetInWaterGoal,
 * with permission from the mod's author Alexthe666. All due credit goes to him. Slight tweaks were made to the source code.
 */

public class PanthalassaFindWaterGoal extends Goal {
    private final PanthalassaEntity mob;
    private final float speed;
    private BlockPos targetPos;
    private int tickCounter;


    public PanthalassaFindWaterGoal(PanthalassaEntity panthalassaEntity, float speedIn) {
        this.mob = panthalassaEntity;
        this.speed = speedIn;
    }

    @Override
    public boolean canUse() {
        if (this.mob.isOnGround() && !this.mob.isInWater()){
            targetPos = generateTarget();
            return targetPos != null;
        }
        if (this.mob.isInWater() && this.mob.level.getBlockState(this.mob.blockPosition().below()).canOcclude() && this.mob.level.getBlockState(this.mob.blockPosition().above()).is(Blocks.AIR)) {
            targetPos = generateTarget();
            return targetPos != null;
        }
        return false;
    }

    @Override
    public void start() {
        if (targetPos != null) {
            this.mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), speed);

        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.getNavigation().isDone()){
            return false;
        }
        if (this.mob.isInWater() && this.mob.level.getFluidState(this.mob.blockPosition().below()).is(FluidTags.WATER)) {
            return false;
        }

        if (tickCounter > 200) {
            return false;
        }
        return true;
    }

    @Override
    public void tick(){
        tickCounter = tickCounter++;

    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        super.stop();
    }


    public BlockPos generateTarget() {
        BlockPos blockpos = null;
        Random random = new Random();
        int range = 16;
        for(int i = 0; i < 15; i++){
            BlockPos blockpos1 = new BlockPos(this.mob.position().add(random.nextInt(range) - range/2, 3, random.nextInt(range) - range/2));
            while(this.mob.level.getBlockState(blockpos1).is(Blocks.AIR) && blockpos1.getY() > 1){
                blockpos1 = blockpos1.below();
            }
            if(this.mob.level.getFluidState(blockpos1).is(FluidTags.WATER) && this.mob.level.getFluidState(blockpos1.below()).is(FluidTags.WATER)){
                blockpos = blockpos1;
            }
        }
        return blockpos;
    }
}