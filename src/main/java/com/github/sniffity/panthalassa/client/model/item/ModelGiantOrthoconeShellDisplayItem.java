package com.github.sniffity.panthalassa.client.model.item;


import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.item.display.ItemGiantOrthoconeShellDisplay;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelGiantOrthoconeShellDisplayItem extends AnimatedGeoModel<ItemGiantOrthoconeShellDisplay> {
    @Override
    public ResourceLocation getModelLocation(ItemGiantOrthoconeShellDisplay object) {
        return new ResourceLocation(Panthalassa.MODID,"geo/display/giant_orthocone_shell/giant_orthocone_shell.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemGiantOrthoconeShellDisplay object) {
        return new ResourceLocation(Panthalassa.MODID,"textures/display/giant_orthocone_shell/giant_orthocone_shell.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemGiantOrthoconeShellDisplay animatable) {
        return new ResourceLocation(Panthalassa.MODID,"animations/display/giant_orthocone_shell/giant_orthocone_shell.json");
    }
}