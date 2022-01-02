package com.github.sniffity.panthalassa.server.events;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.block.BlockPortalBlockEntity;
import com.github.sniffity.panthalassa.server.entity.creature.*;
import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import com.github.sniffity.panthalassa.server.network.PanthalassaPacketHandler;
import com.github.sniffity.panthalassa.server.network.packets.PacketCameraSwitch;
import com.github.sniffity.panthalassa.server.network.packets.PacketVehicleSpecial;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Panthalassa.MODID)

public class PanthalassaEventListener {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            Entity vehicle = player.getVehicle();
            if (vehicle instanceof PanthalassaVehicle) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof Player) {
            Player player = (Player) event.getEntityLiving();
            if (player != null) {
                Entity vehicle = player.getVehicle();
                if (vehicle instanceof PanthalassaVehicle) {
                    event.setCanceled(true);
                    vehicle.hurt(event.getSource(), event.getAmount());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)) {
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
        if (entity instanceof Player) {
            Player player = (Player) event.getEntity();
            BlockState blockstate = event.getPlacedBlock();
            if (blockstate.getBlock() == PanthalassaBlocks.PORTAL.get()) {
                event.setCanceled(true);
                player.sendMessage(new TranslatableComponent("block.panthalassa.panthalassa_portal.message"), Util.NIL_UUID);

            }
        }
     }

    @SubscribeEvent
    public static void onEntityMountEvent (EntityMountEvent event) {
        if (event.getEntityMounting() instanceof ServerPlayer serverPlayer) {
            if (event.isMounting() && event.getEntityBeingMounted() instanceof PanthalassaVehicle) {
                PanthalassaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PacketCameraSwitch());
            }
        }
    }
}