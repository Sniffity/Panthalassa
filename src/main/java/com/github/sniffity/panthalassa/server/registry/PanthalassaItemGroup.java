package com.github.sniffity.panthalassa.server.registry;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class PanthalassaItemGroup {

    public static final CreativeModeTab GROUP = new CreativeModeTab("group_panthalassa") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(PanthalassaItems.PANTHALASSA_LOGO.get());
        }
    };
}
