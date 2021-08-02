package com.github.sniffity.panthalassa.common.item;

import com.github.sniffity.panthalassa.common.block.BlockPortal;
import com.github.sniffity.panthalassa.common.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.common.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.common.registry.PanthalassaItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPortalKey extends Item {

    public ItemPortalKey() {
        super(new Properties()
                .group(PanthalassaItemGroup.GROUP)
                .maxStackSize(1)
        );
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if(context.getPlayer() != null) {
            if(context.getPlayer().world.getDimensionKey() == PanthalassaDimension.PANTHALASSA ||context.getPlayer().world.getDimensionKey() == World.OVERWORLD) {
                for(Direction direction : Direction.Plane.HORIZONTAL) {
                    BlockPos clickPos = context.getPos().offset(direction);
                    if(((BlockPortal) PanthalassaBlocks.PORTAL.get()).trySpawnPortal(context.getWorld(), clickPos)) {
                        return ActionResultType.CONSUME;
                    }
                    else return ActionResultType.FAIL;
                }
            }
        }
        return ActionResultType.FAIL;
    }
}
