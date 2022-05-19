package com.github.sniffity.panthalassa.client.render.armor;


import com.github.sniffity.panthalassa.client.model.armor.ModelDivingSuit;
import com.github.sniffity.panthalassa.server.item.armor.ItemDivingSuit;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class RenderDivingSuit extends GeoArmorRenderer<ItemDivingSuit> {
    public RenderDivingSuit() {
        super(new ModelDivingSuit());

        this.headBone = "helmet";
        this.bodyBone = "chestplate";
        this.rightArmBone = "rightArm";
        this.leftArmBone = "leftArm";
        this.rightLegBone = "rightLeg";
        this.leftLegBone = "leftLeg";
        this.rightBootBone = "rightBoot";
        this.leftBootBone = "leftBoot";
    }
}