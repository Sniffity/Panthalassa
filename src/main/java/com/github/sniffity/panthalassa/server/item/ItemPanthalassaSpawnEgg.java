package com.github.sniffity.panthalassa.server.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Panthalassa Mod - Class: ItemPanthalassaSpawnEgg <br></br?>
 * <p>
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 * <p>
 * Acknowledgements: The following class was developed by replicating how Cadiboo registers SpawnEggs, in
 * their mod MoreOre-1.16.
 */

public class ItemPanthalassaSpawnEgg extends SpawnEggItem {
/**
 * Exists to work around a limitation with Spawn Eggs: Spawn Eggs require an
 * EntityType, but EntityTypes are created AFTER Items. Therefore it is
 * "impossible" for modded spawn eggs to exist. This class gets around it by
 * passing "null" to the SpawnEggItem constructor and doing the initialisation
 * after registry events have finished firing.
 * <p>
 *     */

    protected static final List<ItemPanthalassaSpawnEgg> UNADDED_EGGS = new ArrayList<>();
    private final Lazy<? extends EntityType<?>> entityTypeSupplier;

    public ItemPanthalassaSpawnEgg(final NonNullSupplier<? extends EntityType<?>> entityTypeSupplier, final int p_i48465_2_,
                           final int p_i48465_3_, final Properties p_i48465_4_) {
        super(null, p_i48465_2_, p_i48465_3_, p_i48465_4_);
        this.entityTypeSupplier = Lazy.of(entityTypeSupplier::get);
        UNADDED_EGGS.add(this);
    }

    public ItemPanthalassaSpawnEgg(final RegistryObject<? extends EntityType<?>> entityTypeSupplier, final int p_i48465_2_,
                           final int p_i48465_3_, final Properties p_i48465_4_) {
        super(null, p_i48465_2_, p_i48465_3_, p_i48465_4_);
        this.entityTypeSupplier = Lazy.of(entityTypeSupplier);
        UNADDED_EGGS.add(this);
    }


    public static void initUnaddedEggs() {
        final Map<EntityType<?>, SpawnEggItem> EGGS = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class,
                null, "field_195987_b");
        DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior() {
            public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
                Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
                EntityType<?> entitytype = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
                entitytype.spawn(source.getLevel(), stack, null, source.getPos().relative(direction),
                        SpawnReason.DISPENSER, direction != Direction.UP, false);
                stack.shrink(1);
                return stack;
            }
        };
        for (final SpawnEggItem egg : UNADDED_EGGS) {
            EGGS.put(egg.getType(null), egg);
            DispenserBlock.registerBehavior(egg, defaultDispenseItemBehavior);
            // ItemColors for each spawn egg don't need to be registered because this method
            // is called before ItemColors is created
        }
        UNADDED_EGGS.clear();
    }

    @Override
    public EntityType<?> getType(@Nullable final CompoundNBT p_208076_1_) {
        return entityTypeSupplier.get();
    }

}