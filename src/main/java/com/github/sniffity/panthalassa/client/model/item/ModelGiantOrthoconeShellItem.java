package com.github.sniffity.panthalassa.client.model.item;


import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.item.display.ItemGiantOrthoconeShell;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelGiantOrthoconeShellItem extends AnimatedGeoModel<ItemGiantOrthoconeShell> {
    @Override
    public ResourceLocation getModelLocation(ItemGiantOrthoconeShell object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/display/giant_orthocone_shell/giant_orthocone_shell.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemGiantOrthoconeShell object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/display/giant_orthocone_shell/giant_orthocone_shell.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemGiantOrthoconeShell animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/display/giant_orthocone_shell/giant_orthocone_shell.json");
    }
}