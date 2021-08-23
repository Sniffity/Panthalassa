package com.github.sniffity.panthalassa.server.events;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import com.github.sniffity.panthalassa.server.entity.creature.spawner.ArchelonSpawner;
import com.github.sniffity.panthalassa.server.entity.creature.spawner.KronosaurusSpawner;
import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.system.CallbackI;

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
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        KronosaurusSpawner kronosaurusSpawner = new KronosaurusSpawner();
        ArchelonSpawner archelonSpawner = new ArchelonSpawner();

        if (event.world instanceof ServerWorld) {
            kronosaurusSpawner.tick((ServerWorld) event.world);
            archelonSpawner.tick((ServerWorld) event.world);
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
}
