package com.github.sniffity.panthalassa.server.registry;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class PanthalassaItemGroup {

    public static final CreativeModeTab GROUP = new CreativeModeTab("group_panthalassa") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.CYAN_GLAZED_TERRACOTTA);
        }
    };
}
