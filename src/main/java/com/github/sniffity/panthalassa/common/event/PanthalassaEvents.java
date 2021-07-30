package com.github.sniffity.panthalassa.common.event;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.common.block.PanthalassaPortalBlock;
import com.github.sniffity.panthalassa.common.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.common.registry.PanthalassaItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Panthalassa.MODID)

public class PanthalassaEvents {

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        World world = player.world;
        if (!world.isRemote && event.phase == TickEvent.Phase.END && player.ticksExisted % 20 == 0) {
            if (world.getDimensionKey() == World.OVERWORLD) {
                for (ItemEntity entityItem : world.getEntitiesWithinAABB(ItemEntity.class, player.getBoundingBox().grow(10.0F, 1.0F, 10.0F))) {
                    BlockState state = world.getBlockState(entityItem.getPosition());
                    if (entityItem.getItem().getItem() == PanthalassaItems.PORTAL_KEY.get()
                            && (state.getBlock() == Blocks.WATER
                            || state == PanthalassaBlocks.PORTAL.get().getDefaultState())) {
                        if (((PanthalassaPortalBlock) PanthalassaBlocks.PORTAL.get()).trySpawnPortal(world, entityItem.getPosition())) {
                            entityItem.remove();
                            return;
                        }
                    }
                }
            }
        }
    }
}
