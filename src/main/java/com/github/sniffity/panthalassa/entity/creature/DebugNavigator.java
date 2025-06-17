package com.github.sniffity.panthalassa.entity.creature;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class DebugNavigator extends GroundPathNavigation {
    public DebugNavigator(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), (double)this.getSurfaceY(), this.mob.getZ());
    }

    @Override
    public Path createPath(BlockPos pos, int accuracy) {
        BlockPos forcedPos = new BlockPos(pos.getX(), 15, pos.getZ());
        return super.createPath(ImmutableSet.of(forcedPos), 8, false, accuracy);
    }

    public int getSurfaceY() {
        return 15;
    }

}
