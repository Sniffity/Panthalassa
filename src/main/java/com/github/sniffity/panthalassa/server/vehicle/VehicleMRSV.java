package com.github.sniffity.panthalassa.server.vehicle;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class VehicleMRSV extends PanthalassaVehicle  implements IAnimatable {

    protected static final DataParameter<Boolean> IS_BOOSTING = EntityDataManager.createKey(VehicleMRSV.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> LIGHTS_ON = EntityDataManager.createKey(VehicleMRSV.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> BOOST_COOLDOWN = EntityDataManager.createKey(VehicleMRSV.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> BOOSTING_TIMER = EntityDataManager.createKey(VehicleMRSV.class, DataSerializers.FLOAT);


    public VehicleMRSV(EntityType<? extends PanthalassaVehicle> type, World world) {
        super(type, world);
        this.waterSpeed = 0.05F;
        this.landSpeed = 0.004F;
    }

    @Override
    protected void registerData() {
        this.dataManager.register(MAX_HEALTH, 100F);
        this.dataManager.register(HEALTH, 100F);
        this.dataManager.register(ARMOR, 20F);
        this.dataManager.register(IS_BOOSTING, Boolean.FALSE);
        this.dataManager.register(LIGHTS_ON, Boolean.FALSE);
        this.dataManager.register(BOOST_COOLDOWN, 20F);
        this.dataManager.register(BOOSTING_TIMER, 0F);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        if (compound.contains("isBoosting", Constants.NBT.TAG_BYTE)) {
            this.setIsBoosting(compound.getBoolean("isBoosting"));
        }

        if (compound.contains("boostCooldown", Constants.NBT.TAG_FLOAT)) {
            this.setBoostCooldown(compound.getFloat("boostCooldown"));
        }

        if (compound.contains("boostingTimer", Constants.NBT.TAG_FLOAT)) {
            this.setBoostingTimer(compound.getFloat("boostingTimer"));
        }
        super.readAdditional(compound);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        {
            compound.putBoolean("isBoosting", this.getIsBoosting());
            compound.putFloat("boostCooldown", this.getBoostCooldown());
            compound.putFloat("boostingTimer", this.getBoostingTimer());

            super.writeAdditional(compound);
        }
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
        if (!this.world.isRemote && this.getBoostCooldown() < 0) {
            setIsBoosting(true);
            setBoostCooldown(500);
        }
    }

    @Override
    public float getTravelSpeed() {
        if (this.getIsBoosting()) {
            return this.waterSpeed*4;
        } else if (this.isInWater()) {
            return this.waterSpeed;
        } else {
            return this.landSpeed;
        }
    }
/*
    public void addLight(){
        BlockPos vehiclePos = new BlockPos(this.getPosX(),this.getPosY(),this.getPosZ());
        BlockState vehiclePosBlockState = new BlockState
        //SetBlockState(pos) light level 15?
    }
*/

    public void setBoostingTimer(float cooldown)
    {
        this.dataManager.set(BOOSTING_TIMER, cooldown);
    }

    public float getBoostingTimer()
    {
        return this.dataManager.get(BOOSTING_TIMER);
    }

    public void setIsBoosting(boolean isBoosting)
    {
        this.dataManager.set(IS_BOOSTING, isBoosting);
    }

    public boolean getIsBoosting()
    {
        return this.dataManager.get(IS_BOOSTING);
    }

    public void setBoostCooldown(float cooldown)
    {
        this.dataManager.set(BOOST_COOLDOWN, cooldown);
    }

    public float getBoostCooldown()
    {
        return this.dataManager.get(BOOST_COOLDOWN);
    }


}