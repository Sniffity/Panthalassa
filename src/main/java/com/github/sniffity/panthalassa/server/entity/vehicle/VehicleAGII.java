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
import net.minecraft.util.math.vector.Vector3d;
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

public class VehicleAGII extends PanthalassaVehicle  implements IAnimatable {

    public float deltaRotation;

    protected static final DataParameter<Boolean> NET_ACTIVATED = EntityDataManager.defineId(VehicleAGII.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> NET_CATCH = EntityDataManager.defineId(VehicleAGII.class, DataSerializers.BOOLEAN);

    public VehicleAGII(EntityType<? extends PanthalassaVehicle> type, World world) {
        super(type, world);
        this.waterSpeed = 0.04F;
        this.landSpeed = 0.004F;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(MAX_HEALTH, 200F);
        this.entityData.define(HEALTH, 200F);
        this.entityData.define(ARMOR, 40F);
        this.entityData.define(NET_ACTIVATED, Boolean.FALSE);
        this.entityData.define(NET_CATCH, Boolean.FALSE);

        super.defineSynchedData();
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        if (compound.contains("netActivated", Constants.NBT.TAG_BYTE)) {
            this.setNetActivated(compound.getBoolean("netActivated"));
        }
        if (compound.contains("netCatch", Constants.NBT.TAG_BYTE)) {
            this.setNetCatch(compound.getBoolean("netCatch"));
        }

        super.readAdditionalSaveData(compound);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        {
            compound.putBoolean("netActivated", this.getNetActivated());

            compound.putBoolean("netCatch", this.getNetCatch());
            super.addAdditionalSaveData(compound);
        }

    }

    @Override
    protected boolean canAddPassenger(Entity vehicle) {
        return this.getPassengers().size() < 2;
    }


    @Override
    public double getPassengersRidingOffset() {
        return 0.6D;
    }


    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (getNetActivated()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ag2.hook_deploy_hold", true));
            return PlayState.CONTINUE;
        } else{
            return PlayState.STOP;
        }
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

        if (getNetActivated() && this.isInWater() && this.isAlive() && !getNetCatch()) {
            if (attemptNet(this))
            {
                setNetCatch(true);
            }
        }

        if (!getNetActivated() && getNetCatch()) {
            if (releaseNet(this)) ;
            {
                setNetCatch(false);
            }
        }

        List<Entity> passengers = this.getPassengers();

        if (getNetCatch()) {
            if (passengers.size() > 1) {
                LivingEntity netTarget = (LivingEntity) passengers.get(1);
                netTarget.addEffect(new EffectInstance(Effects.WEAKNESS, 20, 20));
            }
        }
        if (!this.getPassengers().isEmpty()) {
            if (!(this.getPassengers().get(0) instanceof PlayerEntity)){
                this.ejectPassengers();
                setNetCatch(false);
            }
        }
        super.tick();
    }

    @Override
    public void respondKeybindSpecial() {
        if (!this.level.isClientSide)
        {
            if (!this.getNetActivated() && this.isInWater()){
                setNetActivated(true);
            }
            else if (this.getNetActivated()) {
                setNetActivated(false);
            }
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
        if (!getNetCatch() && getNetActivated()) {
            List<Entity> entities = level.getEntities(vehicle, new AxisAlignedBB(vehicle.getX() - 5, vehicle.getY() - 5, vehicle.getZ() - 5, vehicle.getX() + 5, vehicle.getY() + 5, vehicle.getZ() + 5));
            float closestDistance = 100F;
            if (entities.size() != 0) {
                for (Entity testEntity : entities) {
                    if (testEntity instanceof LivingEntity && !(testEntity instanceof PlayerEntity) && testEntity.getVehicle() == null) {
                        float distance = distanceTo(testEntity);
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            if (closestDistance < 5) {
                                testEntity.startRiding(this);
                                if (!this.getPassengers().isEmpty()){
                                    return true;
                                }
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

    public boolean releaseNet(PanthalassaVehicle vehicle) {
        List<Entity> passengers = this.getPassengers();
        if (getNetCatch() && !getNetActivated() && passengers.size() > 1) {
            passengers.get(1).stopRiding();
            return true;
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

    public void setNetCatch(boolean target)
    {
        this.entityData.set(NET_CATCH, target);
    }

    public boolean getNetCatch()
    {
        return this.entityData.get(NET_CATCH);
    }

    @Override
    public void positionRider(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            float f = 0.0F;
            float f1 = (float)((!this.isAlive() ? (double)0.01F : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset());
            float f2 = 0.06F;

            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(passenger);
                if (i == 0) {
                    f = 0.0F;
                    f2 = 0.0F;
                } else {
                    f = -1.0F;
                    f2= -2.0F;
                }
            }

            Vector3d vector3d = (new Vector3d((double)f, 0, 0.0D)).yRot(-this.yRot * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
            passenger.setPos(this.getX() + vector3d.x, this.getY() + (double)f1+f2, this.getZ() + vector3d.z);
            passenger.yRot += this.deltaRotation;
            passenger.setYHeadRot((passenger.getYHeadRot() + this.deltaRotation));
        }
    }

}