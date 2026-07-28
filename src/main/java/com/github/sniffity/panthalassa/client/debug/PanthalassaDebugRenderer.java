package com.github.sniffity.panthalassa.client.debug;

import com.github.sniffity.panthalassa.entity.creature.CreatureKronosaurus;
import com.github.sniffity.panthalassa.entity.creature.PanthalassaCreature;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;

public class PanthalassaDebugRenderer {

    public static void render(
            CreatureKronosaurus creature,
            PoseStack poseStack,
            MultiBufferSource buffer
    ) {
        BlockPos target = creature.getSwimTarget();

        if (target == null) {
            return;
        }


        double x = target.getX() - creature.getX();
        double y = target.getY() - creature.getY();
        double z = target.getZ() - creature.getZ();

        AABB box = new AABB(x, y, z, x + 1, y + 1, z + 1);

        LevelRenderer.renderLineBox(
                poseStack,
                buffer.getBuffer(RenderType.lines()),
                box,
                0.0F, // red
                1.0F, // green
                0.0F, // blue
                1.0F  // alpha
        );


    }
}