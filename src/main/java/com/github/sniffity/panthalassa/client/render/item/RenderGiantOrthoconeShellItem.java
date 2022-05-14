package com.github.sniffity.panthalassa.client.render.item;


import com.github.sniffity.panthalassa.client.model.display.ModelGiantOrthoconeShell;
import com.github.sniffity.panthalassa.client.model.item.ModelGiantOrthoconeShellItem;
import com.github.sniffity.panthalassa.server.item.display.ItemGiantOrthoconeShell;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderGiantOrthoconeShellItem extends GeoItemRenderer<ItemGiantOrthoconeShell> {
    public RenderGiantOrthoconeShellItem() {
        super(new ModelGiantOrthoconeShellItem());
    }
}