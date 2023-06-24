package com.github.sniffity.panthalassa.server.events;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.block.BlockPortal;
import com.github.sniffity.panthalassa.server.block.BlockPortalTileEntity;
import com.github.sniffity.panthalassa.server.entity.creature.*;
import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.NoteBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Panthalassa.MODID)

public class PanthalassaEventListener {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player != null) {
            Entity vehicle = player.getVehicle();
            if (vehicle instanceof PanthalassaVehicle) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            Entity vehicle = player.getVehicle();
            if (vehicle instanceof PanthalassaVehicle) {
                event.setCanceled(true);
                vehicle.hurt(event.getSource(), event.getAmount());
            }
        }
    }


    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity && EntityPredicates.NO_CREATIVE_OR_SPECTATOR.test(entity)) {
            if (entity.level.dimension() == PanthalassaDimension.PANTHALASSA) {
                if (!(entity instanceof PanthalassaEntity)) {
                    if (entity.getVehicle() == null || !(entity.getVehicle() instanceof PanthalassaVehicle)) {
                        entity.hurt(DamageSource.CRAMMING, 100.0F);

                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof PlayerEntity) {
            BlockState blockstate = event.getPlacedBlock();
            if (blockstate.getBlock() == PanthalassaBlocks.PORTAL.get()) {
                BlockPos blockPos = event.getPos();
                World world = entity.level;
                float minPortalFrameRadius = 6.1f;
                float maxPortalFrameRadius = 7.5f;
                float minRadiusSq = minPortalFrameRadius * minPortalFrameRadius;
                BlockPos.Mutable mutable = new BlockPos.Mutable();
                for (int x = (int) -minPortalFrameRadius; x < minPortalFrameRadius; x++) {
                    for (int z = (int) -minPortalFrameRadius; z < minPortalFrameRadius; z++) {
                        int distSq = x * x + z * z;
                        if (distSq <= minRadiusSq) {
                            mutable.set(blockPos).move(x, 0, z);
                            world.setBlock(mutable, PanthalassaBlocks.PORTAL.get().defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);

                            TileEntity tileEntity = world.getBlockEntity(mutable);
                            if (tileEntity instanceof BlockPortalTileEntity) {
                                ((BlockPortalTileEntity) tileEntity).offsetFromCenter = new BlockPos(x, 0, z);
                            }

                            // Helps create some space for mobs to swim into portal
                            if (((ServerWorld) world).dimension() == PanthalassaDimension.PANTHALASSA) {
                                while (mutable.move(Direction.UP).getY() < world.getHeight() && !world.getBlockState(mutable).is(Blocks.BEDROCK) && mutable.getY() < blockPos.getY() + 7) {
                                    world.setBlock(mutable, Blocks.WATER.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                                }
                            }
                        }
                    }
                }
            }
        }
     }

}

