package com.github.sniffity.panthalassa.vehicle;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class VehicleMSRV extends PanthalassaVehicle2  implements IAnimatable {

    private AnimationFactory factory = new AnimationFactory(this);

    public VehicleMSRV(EntityType<?> type, World world) {
        super((EntityType<? extends PanthalassaVehicle2>) type, world);
    }
    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        //If it's moving in the water, swimming, play swim.
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

