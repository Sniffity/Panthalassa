package com.github.sniffity.panthalassa.server.item;

import com.github.sniffity.panthalassa.server.registry.PanthalassaItemGroup;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemDivingSuit extends ArmorItem {

    private String texture;
    public ItemDivingSuit(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Item.Properties().tab(PanthalassaItemGroup.GROUP));
    }

    public Item setArmorTexture(String string) {
        this.texture = string;
        return this;
    }

    @Override
    public String getArmorTexture(@Nonnull ItemStack stack, Entity entity, EquipmentSlot slot, String layer) {
        return "panthalassa:textures/armor/" + this.texture + ".png";
    }
}
