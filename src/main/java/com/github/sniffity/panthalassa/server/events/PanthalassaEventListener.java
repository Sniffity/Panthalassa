package com.github.sniffity.panthalassa.server.events;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.config.PanthalassaCommonConfig;
import com.github.sniffity.panthalassa.server.entity.creature.*;
import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import com.github.sniffity.panthalassa.server.network.PanthalassaPacketHandler;
import com.github.sniffity.panthalassa.server.network.packets.PacketCameraSwitch;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.server.registry.PanthalassaEffects;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.PacketDistributor;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Collection;

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
        if (event.getEntityLiving() instanceof Player player) {
            Entity vehicle = player.getVehicle();
            if (vehicle instanceof PanthalassaVehicle) {
                event.setCanceled(true);
                vehicle.hurt(event.getSource(), event.getAmount());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (PanthalassaCommonConfig.COMMON.GENERAL.crushDepth.get()) {
            Entity entity = event.getEntity();
            if (entity instanceof LivingEntity && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)) {
                if (entity.level.dimension() == PanthalassaDimension.PANTHALASSA) {
                    if (!(entity instanceof PanthalassaEntity)) {
                        if (entity.getVehicle() == null || !(entity.getVehicle() instanceof PanthalassaVehicle)) {
                            if (!((LivingEntity) entity).hasEffect(PanthalassaEffects.CRUSH_RESIST.get())) {
                                entity.hurt(DamageSource.CRAMMING, 100.0F);
                            }
                        }
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

    @SubscribeEvent
    public static void onPLayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (PanthalassaCommonConfig.COMMON.GENERAL.giveJournal.get()) {
            CompoundTag playerData = event.getPlayer().getPersistentData();
            CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
            if (data != null && !data.getBoolean("panthalassa_received_journal")) {
                ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), (PatchouliAPI.get().getBookStack(new ResourceLocation(Panthalassa.MODID,"journal"))));
                data.putBoolean("panthalassa_received_journal", true);
                playerData.put(Player.PERSISTED_NBT_TAG, data);
            }
        }
    }
}