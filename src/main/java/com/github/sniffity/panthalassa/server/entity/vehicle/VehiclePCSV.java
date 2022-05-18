package com.github.sniffity.panthalassa.server.entity.vehicle;

import com.github.sniffity.panthalassa.server.entity.projectile.ProjectileTorpedo;
import com.github.sniffity.panthalassa.server.registry.PanthalassaEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;


public class VehiclePCSV extends PanthalassaVehicle implements IAnimatable {

    protected static final EntityDataAccessor<Float> TORPEDO_COOLDOWN = SynchedEntityData.defineId(VehiclePCSV.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Integer> TORPEDO_COUNT = SynchedEntityData.defineId(VehiclePCSV.class, EntityDataSerializers.INT);


    public VehiclePCSV(EntityType<? extends PanthalassaVehicle> type, Level world) {
        super(type, world);
        this.waterSpeed = 0.060F;
        this.landSpeed = 0.006F;
    }

    public VehiclePCSV(Level p_i1705_1_, double x, double y, double z) {
        this(PanthalassaEntityTypes.PCSV.get(), p_i1705_1_);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(MAX_HEALTH, 300F);
        this.entityData.define(HEALTH, 300F);
        this.entityData.define(ARMOR, 50F);
        this.entityData.define(TORPEDO_COOLDOWN, 100F);
        this.entityData.define(TORPEDO_COUNT, 10);
        super.defineSynchedData();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("torpedoCount")) {
            this.setTorpedoCount(compound.getInt("torpedoCount"));
        }

        if (compound.contains("torpedoCooldown")) {
            this.setTorpedoCooldown(compound.getFloat("torpedoCooldown"));
        }

        super.readAdditionalSaveData(compound);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("torpedoCount", this.getTorpedoCount());
        compound.putFloat("torpedoCooldown", this.getTorpedoCooldown());

        super.addAdditionalSaveData(compound);
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
        setTorpedoCooldown(getTorpedoCount() - 1);
        super.tick();
    }

    @Override
    public void respondKeybindSpecial() {
        if (!this.level.isClientSide && this.getTorpedoCooldown() < 0 && this.getTorpedoCount() > 0) {
            this.level.addFreshEntity(new ProjectileTorpedo(PanthalassaEntityTypes.TORPEDO.get(), this, this.getEyePosition().subtract(0,2,0), Vec3.directionFromRotation(this.xRot, this.yRot)));
            this.setTorpedoCooldown(100);
            this.setTorpedoCount(this.getTorpedoCount()-1);
        }
    }




    //TODO: Perhaps if on TORPEDO_COOLDOWN, apply a different texture, which does not have the torpedoes textured.
    //TODO: If torpedo count = 0, same
    public void setTorpedoCount(int count)
    {
        this.entityData.set(TORPEDO_COUNT, count);
    }

    public int getTorpedoCount()
    {
        return this.entityData.get(TORPEDO_COUNT);
    }

    public void setTorpedoCooldown(float cooldown)
    {
        this.entityData.set(TORPEDO_COOLDOWN, cooldown);
    }

    public float getTorpedoCooldown()
    {
        return this.entityData.get(TORPEDO_COOLDOWN);
    }
}