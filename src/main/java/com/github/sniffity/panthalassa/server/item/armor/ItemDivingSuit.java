package com.github.sniffity.panthalassa.server.item.armor;

import com.github.sniffity.panthalassa.server.registry.PanthalassaItemGroup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;

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
}