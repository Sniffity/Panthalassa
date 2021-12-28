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


    public PanthalassaFindWaterGoal(PanthalassaEntity panthalassaEntity, float speedIn) {
        this.mob = panthalassaEntity;
        this.speed = speedIn;
    }

    public boolean canUse() {


        if (this.mob.isOnGround() && !this.mob.level.getFluidState(this.mob.getEntity().blockPosition()).is(FluidTags.WATER)){
            targetPos = generateTarget();
            return targetPos != null;

        }
        return false;

    }

    public void start() {

        if (targetPos != null) {
            this.mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), speed);
        }
    }

    @Override
    public void tick(){
        if (!this.mob.isInWater()) {
            this.mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), speed);
        } else {
            this.mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), speed/2);
        }

    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && targetPos != null && !this.mob.level.getFluidState(new BlockPos(this.mob.position())).is(FluidTags.WATER);

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
