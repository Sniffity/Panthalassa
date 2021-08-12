package com.github.sniffity.panthalassa.server.entity.vehicle;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class VehicleAG extends PanthalassaVehicle  implements IAnimatable {

    protected static final DataParameter<Boolean> NET_ACTIVATED = EntityDataManager.defineId(VehicleAG.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> NET_TARGET = EntityDataManager.defineId(VehicleAG.class, DataSerializers.BOOLEAN);

    public VehicleAG(EntityType<? extends PanthalassaVehicle> type, World world) {
        super(type, world);
        this.waterSpeed = 0.02F;
        this.landSpeed = 0.002F;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(MAX_HEALTH, 200F);
        this.entityData.define(HEALTH, 200F);
        this.entityData.define(ARMOR, 40F);
        this.entityData.define(NET_ACTIVATED, Boolean.FALSE);
        this.entityData.define(NET_TARGET, Boolean.FALSE);

        super.defineSynchedData();
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        if (compound.contains("netActivated", Constants.NBT.TAG_BYTE)) {
            this.setNetActivated(compound.getBoolean("netActivated"));
        }
        if (compound.contains("netTarget", Constants.NBT.TAG_BYTE)) {
            this.setNetTarget(compound.getBoolean("netTarget"));
        }

        super.readAdditionalSaveData(compound);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        {
            compound.putBoolean("netActivated", this.getNetActivated());

            compound.putBoolean("netTarget", this.getNetTarget());
            super.addAdditionalSaveData(compound);
        }

    }

    @Override
    protected boolean canAddPassenger(Entity vehicle) {
        return this.getPassengers().size() < 2;
    }


    @Override
    public double getPassengersRidingOffset() {
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

    private final AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void tick() {
        if (getNetActivated() && this.isInWater() && !getNetTarget()) {
            if (attemptNet(this)) ;
            {
                setNetTarget(true);
            }
        }

        List<Entity> passengers = this.getPassengers();

        if (getNetTarget()) {
            if (passengers != null && passengers.size() > 0) {
                LivingEntity netTarget = (LivingEntity) passengers.get(1);
                netTarget.addEffect(new EffectInstance(Effects.WEAKNESS, 20, 5));
            }
        }

        super.tick();

    }

    @Override
    public void respondKeybindSpecial() {
        if (!this.level.isClientSide && !this.getNetActivated() && this.isInWater()) {
            setNetActivated(true);
        }
    }



    @Override
    public float getTravelSpeed() {
        if (this.getNetActivated() && this.isInWater()) {
            return this.waterSpeed*0.8F;
        } else if (this.isInWater()) {
            return this.waterSpeed;
        } else
            return this.landSpeed;
    }

    public boolean attemptNet(PanthalassaVehicle vehicle) {
        if (!getNetTarget() && getNetActivated()) {
            List<Entity> entities = level.getEntities(vehicle, new AxisAlignedBB(vehicle.getX() - 5, vehicle.getY() - 5, vehicle.getZ() - 5, vehicle.getX() + 5, vehicle.getY() + 5, vehicle.getZ() + 5));
            float closestDistance = 100F;
            if (entities.size() != 0) {
                for (Entity testEntity : entities) {
                    if (testEntity instanceof LivingEntity && !(testEntity instanceof PlayerEntity)) {
                        float distance = distanceTo(testEntity);
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            if (closestDistance < 5) {
                                testEntity.startRiding(this);
                                return true;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void setNetActivated(boolean active)
    {
        this.entityData.set(NET_ACTIVATED, active);
    }

    public boolean getNetActivated()
    {
        return this.entityData.get(NET_ACTIVATED);
    }

    public void setNetTarget(boolean target)
    {
        this.entityData.set(NET_TARGET, target);
    }

    public boolean getNetTarget()
    {
        return this.entityData.get(NET_TARGET);
    }


}