package com.github.sniffity.panthalassa.entity.creature;

import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class RandomGoal extends RandomStrollGoal {
    public RandomGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier,10,false);
    }

    public void start() {

        this.mob.getNavigation().moveTo(this.wantedX, 15, this.wantedZ, this.speedModifier);
        for (Player player : mob.level().players()) {
            player.sendSystemMessage(Component.literal("New Navigation - X: "+(wantedX)+" Y: 15 Z: "+(wantedZ)));

        }
    }

    @Nullable
    @Override
    public Vec3 getPosition() {
        RandomSource random = mob.getRandom();
        double x = -40 + (80 * random.nextDouble()); // Range: -20 to 20
        double y = 15;                               // Fixed Y
        double z = -40 + (80 * random.nextDouble()); // Range: -20 to 20
        return new Vec3(x, y, z);
    }


}
