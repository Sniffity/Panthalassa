package com.github.sniffity.panthalassa.server.entity.vehicle;

import com.github.sniffity.panthalassa.server.registry.PanthalassaEntityTypes;
import com.github.sniffity.panthalassa.server.registry.PanthalassaItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
    protected static final EntityDataAccessor<Integer> TEXTURE_VARIANT = SynchedEntityData.defineId(VehicleMRSV.class, EntityDataSerializers.INT);


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
        this.entityData.define(TEXTURE_VARIANT, 11);
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

        if (compound.contains("textureVariant")) {
            this.setTextureVariant(compound.getInt("textureVariant"));
        }
        super.readAdditionalSaveData(compound);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        {
            compound.putBoolean("isBoosting", this.getIsBoosting());
            compound.putFloat("boostCooldown", this.getBoostCooldown());
            compound.putFloat("boostingTimer", this.getBoostingTimer());
            compound.putInt("textureVariant", this.getTextureVariant());

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

    public void setTextureVariant(int variant) {
        this.entityData.set(TEXTURE_VARIANT, variant);
    }

    public int getTextureVariant()
    {
        return this.entityData.get(TEXTURE_VARIANT);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();
        int texture = 0;
        boolean flag = false;
        if (item == Items.BLACK_DYE) {
            texture = 0;
            flag = true;
        } else if (item == Items.RED_DYE) {
            texture = 1;
            flag = true;
        } else if (item == Items.GREEN_DYE) {
            texture = 2;
            flag = true;
        } else if (item == Items.BROWN_DYE) {
            texture = 3;
            flag = true;
        } else if (item == Items.BLUE_DYE) {
            texture = 4;
            flag = true;
        }  else if (item == Items.PURPLE_DYE) {
            texture = 5;
            flag = true;
        }  else if (item == Items.CYAN_DYE) {
            texture = 6;
            flag = true;
        } else if (item == Items.LIGHT_GRAY_DYE) {
            texture = 7;
            flag = true;
        } else if (item == Items.GRAY_DYE) {
            texture = 8;
            flag = true;
        }  else if (item == Items.PINK_DYE) {
            texture = 9;
            flag = true;
        } else if (item == Items.LIME_DYE) {
            texture = 10;
            flag = true;
        } else if (item == Items.YELLOW_DYE) {
            texture = 11;
            flag = true;
        } else if (item == Items.LIGHT_BLUE_DYE) {
            texture = 12;
            flag = true;
        } else if (item == Items.MAGENTA_DYE) {
            texture = 13;
            flag = true;
        } else if (item == Items.ORANGE_DYE) {
            texture = 14;
            flag = true;
        } else if (item == Items.WHITE_DYE) {
            texture = 15;
            flag = true;
        }
        if (flag) {
            this.setTextureVariant(texture);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.interact(player,hand);
    }
}