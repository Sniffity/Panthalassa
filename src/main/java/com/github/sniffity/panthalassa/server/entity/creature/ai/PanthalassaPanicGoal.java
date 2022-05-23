package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.EntityArchelon;
import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;


public class PanthalassaPanicGoal extends Goal {
    protected final PanthalassaEntity mob;
    protected final double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected boolean isRunning;

    public PanthalassaPanicGoal(PanthalassaEntity p_25691_, double p_25692_) {
        this.mob = p_25691_;
        this.speedModifier = p_25692_;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (this.mob.getLastHurtByMob() == null && !this.mob.isOnFire()) {
            return false;
        }
        if (!(this.mob instanceof EntityArchelon) && this.mob.isLandNavigator) {
            return false;
        }
        else {
            return this.findRandomPosition();
        }
    }

    protected boolean findRandomPosition() {
        Vec3 vec3 = DefaultRandomPos.getPos(this.mob, 5, 4);
        if (vec3 == null) {
            return false;
        } else {
            this.posX = vec3.x;
            this.posY = vec3.y;
            this.posZ = vec3.z;
            return true;
        }
    }

    public void start() {
        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;
    }

    public boolean canContinueToUse() {
        if (!(this.mob instanceof EntityArchelon) && this.mob.isLandNavigator) {
            return false;
        }
        return !this.mob.getNavigation().isDone();
    }
}