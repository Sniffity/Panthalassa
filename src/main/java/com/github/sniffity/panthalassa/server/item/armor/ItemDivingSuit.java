package com.github.sniffity.panthalassa.server.item.armor;

import com.github.sniffity.panthalassa.server.registry.PanthalassaEffects;
import com.github.sniffity.panthalassa.server.registry.PanthalassaItemGroup;
import com.github.sniffity.panthalassa.server.registry.PanthalassaItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;

import javax.annotation.Nonnull;

public class ItemDivingSuit extends GeoArmorItem implements IAnimatable {
    private AnimationFactory factory = new AnimationFactory(this);

    public ItemDivingSuit(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Item.Properties().tab(PanthalassaItemGroup.GROUP));
    }

    @SuppressWarnings("unused")
    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        return PlayState.STOP;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 20, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onArmorTick(@Nonnull ItemStack stack, Level world, Player player) {
        if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == PanthalassaItems.DIVING_SUIT_HELMET.get()
                && player.getItemBySlot(EquipmentSlot.CHEST).getItem() == PanthalassaItems.DIVING_SUIT_CHEST.get()
                &&  player.getItemBySlot(EquipmentSlot.LEGS).getItem() == PanthalassaItems.DIVING_SUIT_LEGS.get()
                &&  player.getItemBySlot(EquipmentSlot.FEET).getItem() == PanthalassaItems.DIVING_SUIT_BOOTS.get()) {
            player.addEffect(new MobEffectInstance(PanthalassaEffects.CRUSH_RESIST.get(), 20, 0, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 20, 0, false, false, false));
        }
    }
}