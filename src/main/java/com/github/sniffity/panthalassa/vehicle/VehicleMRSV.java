package com.github.sniffity.panthalassa.vehicle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class VehicleMRSV extends PanthalassaVehicle  implements IAnimatable {

    protected static final DataParameter<Boolean> BOOST_ON = EntityDataManager.createKey(PanthalassaVehicle.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> LIGHTS_ON = EntityDataManager.createKey(PanthalassaVehicle.class, DataSerializers.BOOLEAN);

    private final AnimationFactory factory = new AnimationFactory(this);

    public VehicleMRSV(EntityType<? extends PanthalassaVehicle> type, World world) {
        super(type, world);
        this.waterSpeed = 0.05F;
        this.landSpeed = 0.02F;
    }

    @Override
    protected void registerData() {
        this.dataManager.register(MAX_HEALTH, 100F);
        this.dataManager.register(HEALTH, 100F);
        this.dataManager.register(ARMOR, 20F);
        this.dataManager.register(BOOST_ON, Boolean.FALSE);
        this.dataManager.register(LIGHTS_ON, Boolean.FALSE);
    }


    @Override
    public double getMountedYOffset() {
        return 0.0D;
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
    protected void writeAdditional(CompoundNBT compound) {
        {
            //Write boost
            //Write lights
            super.writeAdditional(compound);
        }
    }
}
