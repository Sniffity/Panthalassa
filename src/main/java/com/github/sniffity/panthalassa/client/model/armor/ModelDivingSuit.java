package com.github.sniffity.panthalassa.client.model.armor;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.item.armor.ItemDivingSuit;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDivingSuit extends AnimatedGeoModel<ItemDivingSuit> {
    @Override
    public ResourceLocation getModelLocation(ItemDivingSuit object) {
        return new ResourceLocation(Panthalassa.MODID, "geo/armor/diving_suit/diving_suit.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemDivingSuit object) {
        return new ResourceLocation(Panthalassa.MODID, "textures/armor/diving_suit/diving_suit.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemDivingSuit animatable) {
        return new ResourceLocation(Panthalassa.MODID, "animations/armor/diving_suit/diving_suit.json");
    }
}