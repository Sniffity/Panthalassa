package com.github.sniffity.panthalassa.vehicle;

import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class PanthalassaVehicle extends Entity {

    public float waterSpeed;
    public float landSpeed;
    public float aiMoveSpeed;
    public Vector3d travelVec = new Vector3d(0,0,0);

    public PanthalassaVehicle(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected void registerData() {
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
    }

    public boolean canCollide(Entity entity) {
        return func_242378_a(this, entity);
    }

    public static boolean func_242378_a(Entity p_242378_0_, Entity entity) {
        return (entity.func_241845_aY() || entity.canBePushed()) && !p_242378_0_.isRidingSameEntity(entity);
    }

    public boolean func_241845_aY() {
        return true;
    }

    public boolean canBeCollidedWith() {
        return this.isAlive();
    }


    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        if (!this.world.isRemote) {
            return player.startRiding(this) ? ActionResultType.CONSUME : ActionResultType.PASS;
        } else {
            return ActionResultType.SUCCESS;
        }
    }

    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < 1;
    }


    public double getMountedYOffset() {
        return 0.0D;
    }


    @Nullable
    @Override
    public Entity getControllingPassenger() {
        List<Entity> passengers = getPassengers();
        return passengers.isEmpty() ? null : passengers.get(0);
    }

    public float getTravelSpeed() {
        return isInWater()? this.waterSpeed : this.landSpeed;
    }

    public float getAIMoveSpeed() {
        return this.aiMoveSpeed;
    }

    public void setAIMoveSpeed(float speedIn) {
        this.aiMoveSpeed = speedIn;
    }

    @Override
    public void tick() {
        vehicleTravel(travelVec);
        super.tick();
    }

    public void vehicleTravel(Vector3d vec3d) {
        if (isInWater())
        { if (getControllingPassenger() instanceof LivingEntity)
            {
                float speed = getTravelSpeed() * 0.225f;
                LivingEntity entity = (LivingEntity) getControllingPassenger();
                double moveY = vec3d.y;
                double moveX = vec3d.x;
                double moveZ = entity.moveForward;

                rotationYaw = -entity.rotationYaw;

                //if (!isJumpingOutOfWater())
                rotationPitch = entity.rotationPitch * 0.5f;
                double lookY = entity.getLookVec().y;
                if (entity.moveForward != 0 && (canSwim() || lookY < 0)) moveY = lookY;

                setAIMoveSpeed(speed);
                vec3d = new Vector3d(moveX, moveY, moveZ);
            }
/*
            // add motion if were coming out of water fast; jump out of water like a dolphin
            if (getDeltaMovement().y > 0.25 && level.getBlockState(new BlockPos(getEyePosition(1)).above()).getFluidState().isEmpty())
                setDeltaMovement(getDeltaMovement().multiply(1.2, 1.5f, 1.2d));
*/
            moveRelative(getAIMoveSpeed(), vec3d);
            move(MoverType.SELF, getMotion());
            setMotion(getMotion().scale(0.9d));

//            animationSpeedOld = animationSpeed;
            double xDiff = getPosX() - prevPosX;
            double yDiff = getPosY() - prevPosY;
            double zDiff = getPosZ() - prevPosZ;
            if (yDiff < 0.2) yDiff = 0;
            float amount = MathHelper.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff) * 4f;
            if (amount > 1f) amount = 1f;

//            animationSpeed += (amount - animationSpeed) * 0.4f;
//            animationPosition += animationSpeed;

//            if (vec3d.z == 0 && getTarget() == null && !isInSittingPose())
                setMotion(getMotion().add(0, -0.003d, 0));
        }
    }

}
