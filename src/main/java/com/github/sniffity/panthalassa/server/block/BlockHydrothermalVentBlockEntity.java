package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class BlockHydrothermalVentBlockEntity extends BlockEntity {

    public BlockHydrothermalVentBlockEntity(BlockPos pos, BlockState state) {
        super(PanthalassaBlockEntities.HYDROTHERMAL_VENT_TE.get(), pos, state);
    }

    public static <B extends BlockEntity>void tick(Level level, BlockPos pos) {
        if (level.isClientSide()) {
            Random random = level.getRandom();
            level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                    true,
                    (double)pos.getX() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1),
                    (double)pos.getY() + random.nextDouble() + random.nextDouble(),
                    (double)pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1),
                    0.0D,
                    0.07D,
                    0.0D);
        }
    }
}
