package com.github.sniffity.panthalassa.vehicle;

import net.minecraft.entity.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;



public class PanthalassaVehicle extends Entity {

    protected static final DataParameter<Float> MAX_HEALTH = EntityDataManager.createKey(PanthalassaVehicle.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> HEALTH = EntityDataManager.createKey(PanthalassaVehicle.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> ARMOR = EntityDataManager.createKey(PanthalassaVehicle.class, DataSerializers.FLOAT);

    public float waterSpeed;
    public float landSpeed;
    public float aiMoveSpeed;
    protected int newPosRotationIncrements;
    protected double interpTargetX;
    protected double interpTargetY;
    protected double interpTargetZ;
    protected double interpTargetYaw;
    protected double interpTargetPitch;
    public float moveStrafing;
    public float moveVertical;
    public float moveForward;
    public float jumpMovementFactor = 0.02F;
    public double nlfLastCheck = 0;
    public double nlfDistance;
    public double floorLastCheck;
    public int floorDistance;

    public PanthalassaVehicle(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected void registerData() {
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        if(compound.contains("MaxHealth", Constants.NBT.TAG_FLOAT))
        {
            this.setMaxHealth(compound.getFloat("MaxHealth"));
        }
        if(compound.contains("Health", Constants.NBT.TAG_FLOAT))
        {
            this.setHealth(compound.getFloat("Health"));
        }
        if(compound.contains("Armor", Constants.NBT.TAG_FLOAT))
        {
            this.setArmor(compound.getFloat("Armor"));
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        {
            compound.putFloat("MaxHealth", this.getMaxHealth());
            compound.putFloat("Health", this.getHealth());
            compound.putFloat("Armor", this.getArmor());
        }
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

    public boolean canCollide(Entity entity) {
        return func_242378_a(this, entity);
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

    @Override
    public double getMountedYOffset() {
        return 0.0D;
    }

    public int testFloorDistance(PanthalassaVehicle vehicle, World world) {
        BlockPos pos = vehicle.getPosition();
        while (pos.getY() > 0) {
            if (!(world.getBlockState(pos).isSolid())) {
                pos = pos.down();
            } else {
                return (vehicle.getPosition().getY() - pos.getY());
            }
        }
        return 0;
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        List<Entity> passengers = getPassengers();
        return passengers.isEmpty() ? null : passengers.get(0);
    }

    public float getTravelSpeed() {
        return isInWater() ? this.waterSpeed : this.landSpeed;
    }

    public float getAIMoveSpeed() {
        return this.aiMoveSpeed;
    }

    public void setAIMoveSpeed(float speedIn) {
        this.aiMoveSpeed = speedIn;
    }


    @Override
    public void tick() {
        super.tick();
        this.vehicleTick();
    }

    public void vehicleTick() {
        if (this.canPassengerSteer()) {
            this.newPosRotationIncrements = 0;
            this.setPacketCoordinates(this.getPosX(), this.getPosY(), this.getPosZ());
        }

        if (!this.getPassengers().isEmpty()) {
            if (this.world.getGameTime() - nlfLastCheck > 10) {
                nlfLastCheck = this.world.getGameTime();
                Double nlfDistance = testNLFDistance(this);
                if (nlfDistance != null) {
                    setNLFDistance(nlfDistance);
                } else {
                    setNLFDistance(-1);
                }
            }
        }

        if (this.world.getGameTime() - floorLastCheck > 10) {
            floorLastCheck = this.world.getGameTime();
            int floorDistance = testFloorDistance(this, this.world);
            if (floorDistance >= 0) {
                setFloorDistance(floorDistance);
            }
        }

        Vector3d vector3d = this.getMotion();
        double d1 = vector3d.x;
        double d3 = vector3d.y;
        double d5 = vector3d.z;

        if (Math.abs(vector3d.x) < 0.003D) {
            d1 = 0.0D;
        }

        if (Math.abs(vector3d.y) < 0.003D) {
            d3 = 0.0D;
        }

        if (Math.abs(vector3d.z) < 0.003D) {
            d5 = 0.0D;
        }


        this.setMotion(d1, d3, d5);
        this.world.getProfiler().startSection("ai");
        if (this.isServerWorld()) {
            this.world.getProfiler().startSection("newAi");
            this.updateEntityActionState();
            this.world.getProfiler().endSection();
        }

        this.world.getProfiler().endSection();

        this.world.getProfiler().startSection("travel");
        this.moveStrafing *= 0.98F;
        this.moveForward *= 0.98F;
        this.vehicleTravel(new Vector3d((double) this.moveStrafing, (double) this.moveVertical, (double) this.moveForward));
        this.world.getProfiler().endSection();

        this.world.getProfiler().startSection("push");
        this.world.getProfiler().endSection();
    }


    protected void updateEntityActionState() {
    }

    @OnlyIn(Dist.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.interpTargetX = x;
        this.interpTargetY = y;
        this.interpTargetZ = z;
        this.interpTargetYaw = (double) yaw;
        this.interpTargetPitch = (double) pitch;
        this.newPosRotationIncrements = posRotationIncrements;
    }

    public boolean isServerWorld() {
        return !this.world.isRemote;
    }

    public void vehicleTravel(Vector3d vec3d) {
        if (isInWater()) {
            if (getControllingPassenger() instanceof LivingEntity) {
                float speed = getTravelSpeed();
                LivingEntity entity = (LivingEntity) getControllingPassenger();
                double moveY = vec3d.y;
                double moveX = vec3d.x;
                double moveZ = entity.moveForward;

                rotationYaw = entity.rotationYaw;
                rotationPitch = entity.rotationPitch * 0.5F;

                double lookY = entity.getLookVec().y;

                if (entity.moveForward != 0 && (canSwim() || lookY < 0)) moveY = lookY;
                setAIMoveSpeed(speed);

                vec3d = new Vector3d(moveX, moveY, moveZ);


            }
            moveRelative(getAIMoveSpeed(), vec3d);
            move(MoverType.SELF, getMotion());
            setMotion(getMotion().scale(0.9d));

            if (vec3d.z == 0) {
                setMotion(getMotion().add(0, -0.003d, 0));
            }
        } else if (isOnGround()) {
            if (getControllingPassenger() instanceof LivingEntity) {
                float speed = getTravelSpeed();
                LivingEntity entity = (LivingEntity) getControllingPassenger();
                double moveY = vec3d.y;
                double moveX = vec3d.x;
                double moveZ = entity.moveForward;

                rotationYaw = entity.rotationYaw;

                setAIMoveSpeed(speed);
                vec3d = new Vector3d(moveX, moveY, moveZ);

            }
            moveRelative(getAIMoveSpeed(), vec3d);
            move(MoverType.SELF, getMotion());
            setMotion(getMotion().scale(0.9d));

            if (vec3d.z == 0) {
                setMotion(getMotion().add(0, -0.003d, 0));
            }
        } else if (!isOnGround()) {
            if (getControllingPassenger() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) getControllingPassenger();
                rotationYaw = entity.rotationYaw;
                rotationPitch = entity.rotationPitch * 0.5F;
            }
            double d0 = 0.08D;
            BlockPos blockpos = this.getPositionUnderneath();
            float f3 = this.world.getBlockState(this.getPositionUnderneath()).getSlipperiness(world, this.getPositionUnderneath(), this);
            Vector3d vec5 = handleRelativeFrictionAndCalculateMovement(vec3d, f3);
            double d2 = vec5.y;
            if (this.world.isRemote && !this.world.isBlockLoaded(blockpos)) {
                if (this.getPosY() > 0.0D) {
                    d2 = -0.1D;
                } else {
                    d2 = 0.0D;
                }
            } else if (!this.hasNoGravity()) {
                d2 -= d0;
            }
            this.setMotion(getMotion().getX(), d2 * (double) 0.98F, getMotion().getZ());
        }
    }

    public Vector3d handleRelativeFrictionAndCalculateMovement(Vector3d vec3d, float d3) {
        this.moveRelative(this.getRelevantMoveFactor(d3), vec3d);
        this.setMotion(this.getMotion());
        this.move(MoverType.SELF, this.getMotion());
        return this.getMotion();
    }

    private float getRelevantMoveFactor(float p_213335_1_) {
        return this.onGround ? this.getAIMoveSpeed() * (0.21600002F / (p_213335_1_ * p_213335_1_ * p_213335_1_)) : this.jumpMovementFactor;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!this.world.isRemote && this.isAlive()) {
            Entity trueSource = source.getTrueSource();
            if (source instanceof IndirectEntityDamageSource && trueSource != null && this.isPassenger(trueSource)) {
                return false;
            } else {
                float adjustedAmount = (((100-this.getArmor())/100)>0) ? amount*(((100-this.getArmor())/100)) : 0;
                this.setHealth(this.getHealth() - adjustedAmount);

                boolean isCreativeMode = trueSource instanceof PlayerEntity && ((PlayerEntity) trueSource).isCreative();
                if (isCreativeMode || this.getHealth() < 0.0F) {
                    this.remove();
                }
                return true;
            }
        } else {
            return true;
        }
    }



    public void setHealth(float health)
    {
        this.dataManager.set(HEALTH, Math.min(this.getMaxHealth(), health));
    }

    public float getHealth()
    {
        return this.dataManager.get(HEALTH);
    }



    public float getMaxHealth()
    {
        return this.dataManager.get(MAX_HEALTH);
    }


    public void setMaxHealth(float maxHealth)
    {
        this.dataManager.set(MAX_HEALTH, maxHealth);
    }


    public void setArmor(float armor)
    {
        this.dataManager.set(ARMOR, armor);
    }

    public float getArmor()
    {
        return this.dataManager.get(ARMOR);
    }

    public Double testNLFDistance(PanthalassaVehicle vehicle) {
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(vehicle, new AxisAlignedBB(vehicle.getPosX() - 10, vehicle.getPosY() - 10, vehicle.getPosZ() - 10, vehicle.getPosX() + 10, vehicle.getPosY() + 10, vehicle.getPosZ() + 10));
        double closestDistance = 100;
        if (entities.size() != 0) {
            for (int i = 0; i < entities.size(); i++) {
                Entity testEntity = entities.get(i);
                if (testEntity instanceof LivingEntity && !(testEntity instanceof PlayerEntity)) {
                    float distance = getDistance(testEntity);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                    }
                }
            }
            if (closestDistance < 20) {
                return closestDistance;
            }
        }

        return null;
    }

    public void setNLFDistance(double nlfDistance) {
        this.nlfDistance = nlfDistance;
    }

    public Double getNLFDistance() {
        return this.nlfDistance;
    }

    public void setFloorDistance(int floorDistance) {
        this.floorDistance = floorDistance;
    }

    public int getFloorDistance() {
        return this.floorDistance;
    }

    public void getDriverKeybinds(int key, boolean pressed)
    {}
}
