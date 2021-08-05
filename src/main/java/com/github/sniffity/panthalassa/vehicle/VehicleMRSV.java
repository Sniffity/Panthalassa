package com.github.sniffity.panthalassa.vehicle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class VehicleMRSV extends PanthalassaVehicle  implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public VehicleMRSV(EntityType<? extends PanthalassaVehicle> type, World world) {
        super(type, world);
        this.waterSpeed= 0.05F;
        this.landSpeed = 0.02F;
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if ((this.isSwimming()) || (event.isMoving() && this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.mrsv.roll", true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public double getMountedYOffset() {
        return 0.0D;
    }

}

