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

public class VehicleMSRV extends PanthalassaVehicle  implements IAnimatable {

    private final AnimationFactory factory = new AnimationFactory(this);

    public VehicleMSRV(EntityType<? extends PanthalassaVehicle> type, World world) {
        super(type, world);
        this.waterSpeed= 1.0F;
        this.landSpeed = 0.4F;
//        this.aiMoveSpeed = 1.0F;

    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if ((this.isSwimming()) || (event.isMoving() && this.isInWater())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.msrv.roll", true));
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

}

