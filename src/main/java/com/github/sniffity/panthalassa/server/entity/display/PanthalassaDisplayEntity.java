package com.github.sniffity.panthalassa.server.entity.display;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;


public class PanthalassaDisplayEntity extends Mob implements IAnimatable {

    protected static final EntityDataAccessor<Float> DIRECTION = SynchedEntityData.defineId(PanthalassaDisplayEntity.class, EntityDataSerializers.FLOAT);

    public PanthalassaDisplayEntity(EntityType type, Level level) {
        super(type, level);
        this.noCulling = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DIRECTION, 0F);
        super.defineSynchedData();
    }

    public static AttributeSupplier.Builder displayAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.MOVEMENT_SPEED, (double) 0);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isNoAi() {
        return true;
    }

    public void tick() {
        this.yBodyRotO = 0;
        this.yHeadRotO = 0;
        this.yBodyRot = 0;
        this.yHeadRot = 0;
    }

    public float getYaw() {
        return this.entityData.get(DIRECTION);
    }

    public void setYaw(float yaw) {
        this.entityData.set(DIRECTION,yaw);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            this.setYaw(player.yRot);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setYaw(compound.getFloat("Yaw"));
        super.readAdditionalSaveData(compound);

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("Yaw", this.getYaw());
        super.addAdditionalSaveData(compound);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }
}