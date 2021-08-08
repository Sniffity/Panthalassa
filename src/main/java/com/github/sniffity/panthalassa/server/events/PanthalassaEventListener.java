package com.github.sniffity.panthalassa.server.events;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.vehicle.PanthalassaVehicle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Panthalassa.MODID)

public class PanthalassaEventListener {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event){
        PlayerEntity player = event.getPlayer();
        if(player != null){
            Entity vehicle = player.getRidingEntity();
            if(vehicle instanceof PanthalassaVehicle){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingHurtEvent event){
        if (event.getEntityLiving() instanceof PlayerEntity) {
           PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if(player != null) {
            Entity vehicle = player.getRidingEntity();
            if (vehicle instanceof PanthalassaVehicle) {
                event.setCanceled(true);
                vehicle.attackEntityFrom(event.getSource(),event.getAmount());
            }
        }
        }
    }
}