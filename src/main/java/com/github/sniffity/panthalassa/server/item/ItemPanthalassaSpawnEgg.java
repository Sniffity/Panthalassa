package com.github.sniffity.panthalassa.server.item;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.BlockSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.core.Direction;
import net.minecraftforge.common.ForgeSpawnEggItem;
import java.util.function.Supplier;


public class ItemPanthalassaSpawnEgg extends ForgeSpawnEggItem {
    public ItemPanthalassaSpawnEgg(Supplier<? extends EntityType<? extends Mob>> typeIn, int primaryColorIn, int secondaryColorIn, Properties builder) {
        super(typeIn, primaryColorIn, secondaryColorIn, builder);


        DispenserBlock.registerBehavior(
                this,
                new DefaultDispenseItemBehavior() {
                    public ItemStack execute(BlockSource source, ItemStack stack) {
                        Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                        EntityType<?> entitytype = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
                        entitytype.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                        stack.shrink(1);
                        return stack;
                    }
                });
    }
}