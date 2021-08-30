package com.github.sniffity.panthalassa.server.entity.vehicle;

import com.github.sniffity.panthalassa.server.registry.PanthalassaEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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

/**
 * Panthalassa Mod - Class: PanthalassaVehicle <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Wyrmroost
 * and Mr. Crayfish's Vehicle Mod handle their own respective vehicles. Additionally,
 * Vanilla Minecraft's BoatEntity class was also studied.
 */


public class VehicleMRSV extends PanthalassaVehicle  implements IAnimatable {

    protected static final DataParameter<Boolean> IS_BOOSTING = EntityDataManager.defineId(VehicleMRSV.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> BOOST_COOLDOWN = EntityDataManager.defineId(VehicleMRSV.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> BOOSTING_TIMER = EntityDataManager.defineId(VehicleMRSV.class, DataSerializers.FLOAT);


    public VehicleMRSV(EntityType<? extends PanthalassaVehicle> type, World world) {
        super(type, world);
        this.waterSpeed = 0.06F;
        this.landSpeed = 0.004F;
    }

    public VehicleMRSV(World p_i1705_1_, double x, double y, double z) {
        this(PanthalassaEntityTypes.MRSV.get(), p_i1705_1_);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vector3d.ZERO);
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
    protected void readAdditionalSaveData(CompoundNBT compound) {
        if (compound.contains("isBoosting", Constants.NBT.TAG_BYTE)) {
            this.setIsBoosting(compound.getBoolean("isBoosting"));
        }

        if (compound.contains("boostCooldown", Constants.NBT.TAG_FLOAT)) {
            this.setBoostCooldown(compound.getFloat("boostCooldown"));
        }

        if (compound.contains("boostingTimer", Constants.NBT.TAG_FLOAT)) {
            this.setBoostingTimer(compound.getFloat("boostingTimer"));
        }
        super.readAdditionalSaveData(compound);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
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
            setBoostCooldown(500);
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