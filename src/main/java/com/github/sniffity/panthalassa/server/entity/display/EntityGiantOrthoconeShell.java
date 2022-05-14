package com.github.sniffity.panthalassa.server.entity.display;

import com.github.sniffity.panthalassa.server.registry.PanthalassaItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntityGiantOrthoconeShell extends PanthalassaDisplayEntity{
    public EntityGiantOrthoconeShell(EntityType type, Level level) {
        super(type, level);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.turnIntoItem();
        return super.hurt(source, amount);
    }

    public void turnIntoItem() {
        if (isRemoved())
            return;
        this.discard();
        ItemStack stack = new ItemStack(PanthalassaItems.GIANT_ORTHOCONE_SHELL.get(), 1);
        if (!this.level.isClientSide)
            this.spawnAtLocation(stack, 0.0F);
    }
}


