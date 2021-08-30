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

public class PanthalassaFindWaterGoal extends Goal {
    private final PanthalassaEntity mob;
    private final float speed;
    private BlockPos targetPos;


    public PanthalassaFindWaterGoal(PanthalassaEntity panthalassaEntity, float speedIn) {
        this.mob = panthalassaEntity;
        this.speed = speedIn;
    }

    public boolean canUse() {
        if (this.mob.isInWater() && this.mob.level.getBlockState(this.mob.blockPosition().below()).canOcclude() && this.mob.level.getBlockState(this.mob.blockPosition().above()).is(Blocks.AIR)) {
            targetPos = generateTarget();
            return targetPos != null;
        }

        if (this.mob.isOnGround() && (!this.mob.isInWater())) {
            targetPos = generateTarget();
            return targetPos != null;
        }
        return false;
    }

    public void start() {

        if (targetPos != null) {
            Vector3d targetVector = new Vector3d ((double)targetPos.getX(), (double)targetPos.getY(), (double)targetPos.getZ()).subtract(mob.position()).normalize().scale(0.05);
            this.mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), Math.max(speed,0.10D));
            this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(targetVector));
            System.out.println(this.mob.getDeltaMovement().length());

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
