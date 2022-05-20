package com.github.sniffity.panthalassa.server.entity.vehicle;

import com.github.sniffity.panthalassa.server.registry.PanthalassaEntityTypes;
import com.github.sniffity.panthalassa.server.registry.PanthalassaItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class VehicleMRSV extends PanthalassaVehicle  implements IAnimatable {

    protected static final EntityDataAccessor<Boolean> IS_BOOSTING = SynchedEntityData.defineId(VehicleMRSV.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> BOOST_COOLDOWN = SynchedEntityData.defineId(VehicleMRSV.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> BOOSTING_TIMER = SynchedEntityData.defineId(VehicleMRSV.class, EntityDataSerializers.FLOAT);


    public VehicleMRSV(EntityType<? extends PanthalassaVehicle> type, Level world) {
        super(type, world);
        this.waterSpeed = 0.042F;
        this.landSpeed = 0.004F;
        this.itemStack = new ItemStack(PanthalassaItems.MRSV_VEHICLE.get(),1);
    }

    public VehicleMRSV(Level p_i1705_1_, double x, double y, double z) {
        this(PanthalassaEntityTypes.MRSV.get(), p_i1705_1_);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(MAX_HEALTH, 100F);
        this.entityData.define(HEALTH, 100F);
        this.entityData.define(ARMOR, 20F);
        this.entityData.define(IS_BOOSTING, Boolean.FALSE);
        this.entityData.define(BOOST_COOLDOWN, 20F);
        this.entityData.define(BOOSTING_TIMER, 0F);
        super.defineSynchedData();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("isBoosting")) {
            this.setIsBoosting(compound.getBoolean("isBoosting"));
        }

        if (compound.contains("boostCooldown")) {
            this.setBoostCooldown(compound.getFloat("boostCooldown"));
        }

        if (compound.contains("boostingTimer")) {
            this.setBoostingTimer(compound.getFloat("boostingTimer"));
        }
        super.readAdditionalSaveData(compound);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        {
            compound.putBoolean("isBoosting", this.getIsBoosting());
            compound.putFloat("boostCooldown", this.getBoostCooldown());
            compound.putFloat("boostingTimer", this.getBoostingTimer());

            super.addAdditionalSaveData(compound);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.15D;
    }


    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
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
        setBoostCooldown(getBoostCooldown() - 1);
        if (getIsBoosting()){
            setBoostingTimer(getBoostingTimer()+1);
            if (getBoostingTimer()>50) {
                setIsBoosting(false);
                setBoostingTimer(0);
            }
        }
        super.tick();
    }

    @Override
    public void respondKeybindSpecial() {
        if (!this.level.isClientSide && this.getBoostCooldown() < 0 && this.isInWater()) {
            setIsBoosting(true);
            setBoostCooldown(400);
        }
    }

    @Override
    public float getTravelSpeed() {
        if (this.getIsBoosting() && this.isInWater()) {
            return this.waterSpeed*4;
        } else if (this.isInWater()) {
            return this.waterSpeed;
        } else
        return this.landSpeed;
    }

    public void setBoostingTimer(float cooldown)
    {
        this.entityData.set(BOOSTING_TIMER, cooldown);
    }

    public float getBoostingTimer()
    {
        return this.entityData.get(BOOSTING_TIMER);
    }

    public void setIsBoosting(boolean isBoosting)
    {
        this.entityData.set(IS_BOOSTING, isBoosting);
    }

    public boolean getIsBoosting()
    {
        return this.entityData.get(IS_BOOSTING);
    }

    public void setBoostCooldown(float cooldown)
    {
        this.entityData.set(BOOST_COOLDOWN, cooldown);
    }

    public float getBoostCooldown()
    {
        return this.entityData.get(BOOST_COOLDOWN);
    }


}