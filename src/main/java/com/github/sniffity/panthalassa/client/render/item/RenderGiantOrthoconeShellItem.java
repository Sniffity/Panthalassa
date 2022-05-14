package com.github.sniffity.panthalassa.client.render.item;



import com.github.sniffity.panthalassa.client.model.item.ModelGiantOrthoconeShellDisplayItem;
import com.github.sniffity.panthalassa.server.item.display.ItemGiantOrthoconeShellDisplay;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class RenderGiantOrthoconeShellItem extends GeoItemRenderer<ItemGiantOrthoconeShellDisplay> {
    public RenderGiantOrthoconeShellItem() {
        super(new ModelGiantOrthoconeShellDisplayItem());
    }
}