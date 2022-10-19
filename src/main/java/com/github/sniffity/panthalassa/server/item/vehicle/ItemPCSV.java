package com.github.sniffity.panthalassa.server.item.vehicle;

import com.github.sniffity.panthalassa.server.entity.vehicle.VehiclePCSV;
import net.minecraft.ChatFormatting;
    import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

import static com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle.*;

public class ItemPCSV extends Item {
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

    public ItemPCSV(Item.Properties p_i48526_2_) {
        super(p_i48526_2_);
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
                VehiclePCSV vehiclePCSV = new VehiclePCSV(worldIn, raytraceresult.getLocation().x, raytraceresult.getLocation().y, raytraceresult.getLocation().z);
                vehiclePCSV.yRot = player.yRot;
                if (!worldIn.noCollision(vehiclePCSV, vehiclePCSV.getBoundingBox().inflate(-0.1D))) {
                    return InteractionResultHolder.fail(itemstack);
                } else {
                    if (!worldIn.isClientSide) {
                        if (itemstack.getTag() != null) {
                            vehiclePCSV.setHealth(itemstack.getTag().getFloat(VEHICLE_HEALTH));
                            vehiclePCSV.setTorpedoCount(itemstack.getTag().getInt(TORPEDO_COUNT));
                        }
                        worldIn.addFreshEntity(vehiclePCSV);
                        if (!player.getAbilities().instabuild) {
                            itemstack.shrink(1);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide());
                }
            } else {
                return InteractionResultHolder.pass(itemstack);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        if  (stack.getTag() != null) {
            Float health = stack.getTag().getFloat(VEHICLE_HEALTH);
            tooltip.add(new TextComponent("Vehicle Health: ").append(new TextComponent(health.toString())).withStyle(ChatFormatting.YELLOW));
            int torpedoCount = stack.getTag().getInt(TORPEDO_COUNT);
            tooltip.add(new TextComponent("Torpedo Count: ").append(new TextComponent(String.valueOf(torpedoCount))).withStyle(ChatFormatting.YELLOW));
        }
    }
}