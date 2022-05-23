package com.github.sniffity.panthalassa.server.item.display;

import com.github.sniffity.panthalassa.server.entity.display.DisplayGiantOrthoconeShell;
import com.github.sniffity.panthalassa.server.registry.PanthalassaEntityTypes;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import java.util.List;
import java.util.function.Predicate;

public class ItemGiantOrthoconeShellDisplay extends Item  {
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);


    public ItemGiantOrthoconeShellDisplay(Item.Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        HitResult raytraceresult = getPlayerPOVHitResult(worldIn, player, ClipContext.Fluid.ANY);
        if (raytraceresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            Vec3 vector3d = player.getViewVector(1.0F);
            double d0 = 5.0D;
            List<Entity> list = worldIn.getEntities(player, player.getBoundingBox().expandTowards(vector3d.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
            if (!list.isEmpty()) {
                Vec3 vector3d1 = player.getEyePosition(1.0F);

                for(Entity entity : list) {
                    AABB axisalignedbb = entity.getBoundingBox().inflate((double)entity.getPickRadius());
                    if (axisalignedbb.contains(vector3d1)) {
                        return InteractionResultHolder.pass(itemstack);
                    }
                }
            }

            if (raytraceresult.getType() == HitResult.Type.BLOCK) {
                DisplayGiantOrthoconeShell displayEntity = new DisplayGiantOrthoconeShell(PanthalassaEntityTypes.GIANT_ORTHOCONE_SHELL.get(), worldIn);
                if (!worldIn.isClientSide) {
                    displayEntity.yRot = player.yRot;
                    displayEntity.setPos(raytraceresult.getLocation().x, raytraceresult.getLocation().y, raytraceresult.getLocation().z);
                    worldIn.addFreshEntity(displayEntity);
                        if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                }
                player.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
            } else {
                return InteractionResultHolder.pass(itemstack);
            }
        }
    }
}