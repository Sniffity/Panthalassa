package com.github.sniffity.panthalassa.entity.creature;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PanthalassaCreature extends PathfinderMob implements GeoEntity {

    public boolean isLandNavigator;
    public float rotationPitch;
    public float prevRotationPitch;
    public float prevYRot;
    public float deltaYRot;
    public float adjustYaw;
    public float prevSetYaw;
    public float setYaw;
    private float prevYawRot;
    private float deltaYawRot;
    private int yawTickCounter;
    private final Vec3 center = new Vec3(0, 5, 0);
    private final double speed = 0.03; // radians per tick
    private double prevAngle = 0.0;
    public double angularSpeedEstimate = 0.0;
    public float waterSpeed = 10F;
    public float waterDrag = 90F;

    protected PanthalassaCreature(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noCulling = true;
    }

    // =========================================
    // ENTITY DATA
    // =========================================

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SWIM_TARGET, Optional.empty());
        builder.define(PATH_NODES, new CompoundTag());
    }

    private static final EntityDataAccessor<CompoundTag> PATH_NODES = SynchedEntityData.defineId(PanthalassaCreature.class, EntityDataSerializers.COMPOUND_TAG);

    public void setPathNodes(@Nullable Path path) {
        CompoundTag tag = new CompoundTag();
        if (path != null) {
            long[] packed = new long[path.getNodeCount()];
            for (int i = 0; i < path.getNodeCount(); i++) {
                Node node = path.getNode(i);
                packed[i] = BlockPos.asLong(node.x, node.y, node.z);
            }
            tag.putLongArray("nodes", packed);
        }
        entityData.set(PATH_NODES, tag);
    }

    public List<BlockPos> getPathNodes() {
        long[] packed = entityData.get(PATH_NODES).getLongArray("nodes");
        List<BlockPos> result = new ArrayList<>(packed.length);
        for (long l : packed) {
            result.add(BlockPos.of(l));
        }
        return result;
    }

    private static final EntityDataAccessor<Optional<BlockPos>> SWIM_TARGET = SynchedEntityData.defineId(PanthalassaCreature.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    public void setSwimTarget(@Nullable BlockPos pos) {
        entityData.set(SWIM_TARGET, Optional.ofNullable(pos));
    }

    @Nullable
    public BlockPos getSwimTarget() {
        return entityData.get(SWIM_TARGET).orElse(null);
    }

    // =========================================
    // SPECIES METHODS
    // =========================================

    protected abstract boolean speciesUnderwaterBreathing();

    protected abstract boolean speciesDynamicYaw();

    protected abstract boolean speciesDynamicPitch();

    protected abstract boolean speciesAmphibious();

    protected abstract boolean speciesReturnsToWater();


    @Override
    public void tick() {
        super.tick();
        //tickMotion();

        /*
        handleDynamicPitchOperations();
        if (speciesUnderwaterBreathing()) {
        }
        if (speciesDynamicYaw()) {
        }
        if (speciesDynamicPitch()) {
        }

        if (speciesAmphibious()) {
        }
        if (speciesReturnsToWater()) {
        }
         */


    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {

        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(waterSpeed/1000, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(waterDrag/1000));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(travelVector);
        }


        /*
        if (speciesUnderwaterBreathing()) {

        }

        if (speciesDynamicYaw()) {
            handleDynamicYawOperations();

        }

        if (speciesDynamicPitch()) {
            handleDynamicPitchOperations();

        }

        if (speciesAmphibious()) {

        }

        if (speciesReturnsToWater()) {

        }
       */

    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return type != NeoForgeMod.WATER_TYPE.value();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    // =========================================
    // ANIMATION METHODS
    // =========================================
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controllerLocomotion", 5, this::locomotionAnimationController));
    }

    protected <E extends PanthalassaCreature> PlayState locomotionAnimationController(final AnimationState<E> event) {
        //locomotion Methods
        return PlayState.STOP;
    }

    private void handleDynamicYawOperations() {
        float adjustment = 0.10F;

        float rawDelta = Mth.wrapDegrees(this.yBodyRot - prevYawRot);
        prevYawRot = this.yBodyRot;

        if (adjustYaw > rawDelta) {
            adjustYaw = Math.max(adjustYaw - adjustment, rawDelta);
        } else if (adjustYaw < rawDelta) {
            adjustYaw = Math.min(adjustYaw + adjustment, rawDelta);
        }

        prevSetYaw = setYaw;

        setYaw = (adjustYaw) * ((float)Math.PI/180F);

    }

    private void handleDynamicPitchOperations() {
        prevRotationPitch = rotationPitch;
        rotationPitch = (float) ((Math.atan2((this.getDeltaMovement().y), Math.sqrt((float) ((this.getDeltaMovement().x) * (this.getDeltaMovement().x) + (this.getDeltaMovement().z) * (this.getDeltaMovement().z))))));
    }

}