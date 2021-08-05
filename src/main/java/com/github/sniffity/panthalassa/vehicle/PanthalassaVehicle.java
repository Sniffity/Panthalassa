package com.github.sniffity.panthalassa.vehicle;

import com.github.sniffity.panthalassa.common.registry.PanthalassaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;


public class PanthalassaVehicle extends Entity {
    private PanthalassaVehicle.Status status;
    private PanthalassaVehicle.Status previousStatus;

    public float waterSpeed;
    public float landSpeed;
    public float aiMoveSpeed;
    public Vector3d travelVec = new Vector3d(0, 0, 0);
    private double waterLevel;
    private float boatGlide;
    private double lastYd;
    private float momentum;
    private float deltaRotation;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;

    private double lerpPitch;
    protected int newPosRotationIncrements;
    protected double interpTargetX;
    protected double interpTargetY;
    protected double interpTargetZ;
    protected double interpTargetYaw;
    protected double interpTargetPitch;
    protected double interpTargetHeadYaw;
    protected int interpTicksHead;
    public float moveStrafing;
    public float moveVertical;
    public float moveForward;
    public float renderYawOffset;
    public float jumpMovementFactor = 0.02F;
    public double nlfLastCheck = 0;
    public double nlfDistance;
    public double floorLastCheck;
    public int floorDistance;
    protected float prevOnGroundSpeedFactor;
    protected float onGroundSpeedFactor;

    public static enum Status {
        IN_WATER,
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR;
    }

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

    private PanthalassaVehicle.Status getVehicleStatus() {
        PanthalassaVehicle.Status vehicleStatus = this.getUnderwaterStatus();
        if (vehicleStatus != null) {
            this.waterLevel = this.getBoundingBox().maxY;
            return vehicleStatus;
        } else if (this.checkInWater()) {
            return PanthalassaVehicle.Status.IN_WATER;
        } else {
            float f = this.getBoatGlide();
            if (f > 0.0F) {
                this.boatGlide = f;
                return PanthalassaVehicle.Status.ON_LAND;
            } else {
                return PanthalassaVehicle.Status.IN_AIR;
            }
        }
    }

    @Nullable
    private PanthalassaVehicle.Status getUnderwaterStatus() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        double d0 = axisalignedbb.maxY + 0.001D;
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(d0);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutable.setPos(k1, l1, i2);
                    FluidState fluidstate = this.world.getFluidState(blockpos$mutable);
                    if (fluidstate.isTagged(FluidTags.WATER) && d0 < (double) ((float) blockpos$mutable.getY() + fluidstate.getActualHeight(this.world, blockpos$mutable))) {
                        if (!fluidstate.isSource()) {
                            return PanthalassaVehicle.Status.UNDER_FLOWING_WATER;
                        }

                        flag = true;
                    }
                }
            }
        }

        return flag ? PanthalassaVehicle.Status.UNDER_WATER : null;
    }

    private boolean checkInWater() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.ceil(axisalignedbb.minY + 0.001D);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        this.waterLevel = Double.MIN_VALUE;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutable.setPos(k1, l1, i2);
                    FluidState fluidstate = this.world.getFluidState(blockpos$mutable);
                    if (fluidstate.isTagged(FluidTags.WATER)) {
                        float f = (float) l1 + fluidstate.getActualHeight(this.world, blockpos$mutable);
                        this.waterLevel = Math.max((double) f, this.waterLevel);
                        flag |= axisalignedbb.minY < (double) f;
                    }
                }
            }
        }
        return flag;
    }

    public float getBoatGlide() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY - 0.001D, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        int i = MathHelper.floor(axisalignedbb1.minX) - 1;
        int j = MathHelper.ceil(axisalignedbb1.maxX) + 1;
        int k = MathHelper.floor(axisalignedbb1.minY) - 1;
        int l = MathHelper.ceil(axisalignedbb1.maxY) + 1;
        int i1 = MathHelper.floor(axisalignedbb1.minZ) - 1;
        int j1 = MathHelper.ceil(axisalignedbb1.maxZ) + 1;
        VoxelShape voxelshape = VoxelShapes.create(axisalignedbb1);
        float f = 0.0F;
        int k1 = 0;
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int l1 = i; l1 < j; ++l1) {
            for (int i2 = i1; i2 < j1; ++i2) {
                int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);
                if (j2 != 2) {
                    for (int k2 = k; k2 < l; ++k2) {
                        if (j2 <= 0 || k2 != k && k2 != l - 1) {
                            blockpos$mutable.setPos(l1, k2, i2);
                            BlockState blockstate = this.world.getBlockState(blockpos$mutable);
                            if (!(blockstate.getBlock() instanceof LilyPadBlock) && VoxelShapes.compare(blockstate.getCollisionShape(this.world, blockpos$mutable).withOffset((double) l1, (double) k2, (double) i2), voxelshape, IBooleanFunction.AND)) {
                                f += blockstate.getSlipperiness(this.world, blockpos$mutable, this);
                                ++k1;
                            }
                        }
                    }
                }
            }
        }
        return f / (float) k1;
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
                return (vehicle.getPosition().getY()-pos.getY());
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
                } else  {
                    setNLFDistance(-1);
                }
            }
        }

        if (this.world.getGameTime() - floorLastCheck > 10) {
            floorLastCheck = this.world.getGameTime();
            int floorDistance = testFloorDistance(this, this.world);
            if (floorDistance >=0) {
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
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        this.vehicleTravel(new Vector3d((double) this.moveStrafing, (double) this.moveVertical, (double) this.moveForward));
        this.world.getProfiler().endSection();

        this.world.getProfiler().startSection("push");
        this.collideWithNearbyEntities();
        this.world.getProfiler().endSection();
    }

    public void setMoveVertical(float amount) {
        this.moveVertical = amount;
    }

    protected void collideWithNearbyEntities() {
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox(), EntityPredicates.pushableBy(this));
        if (!list.isEmpty()) {
            int i = this.world.getGameRules().getInt(GameRules.MAX_ENTITY_CRAMMING);
            if (i > 0 && list.size() > i - 1 && this.rand.nextInt(4) == 0) {
                int j = 0;
                for (Entity entity : list) {
                    if (!entity.isPassenger()) {
                        ++j;
                    }
                }
                if (j > i - 1) {
                    this.attackEntityFrom(DamageSource.CRAMMING, 6.0F);
                }
            }
            for (Entity entity : list) {
                this.collideWithEntity(entity);
            }
        }

    }

    protected void collideWithEntity(Entity entityIn) {
        entityIn.applyEntityCollision(this);
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

    public void vehicleTravel(Vector3d vec3d) {
        if (isInWater()) {
            if (getControllingPassenger() instanceof LivingEntity) {
                float speed = getTravelSpeed() * 0.225f;
                LivingEntity entity = (LivingEntity) getControllingPassenger();
                double moveY = vec3d.y;
                double moveX = vec3d.x;
                double moveZ = entity.moveForward;

                rotationYaw = entity.rotationYaw;
                rotationPitch = entity.rotationPitch*0.2F;

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
                float speed = getTravelSpeed() * 0.225f;
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
                rotationPitch = entity.rotationPitch*0.2F;
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

    public float getWaterLevelAbove() {
        AxisAlignedBB axisalignedbb = this.getBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(axisalignedbb.maxY - this.lastYd);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        label39:
        for(int k1 = k; k1 < l; ++k1) {
            float f = 0.0F;

            for(int l1 = i; l1 < j; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    blockpos$mutable.setPos(l1, k1, i2);
                    FluidState fluidstate = this.world.getFluidState(blockpos$mutable);
                    if (fluidstate.isTagged(FluidTags.WATER)) {
                        f = Math.max(f, fluidstate.getActualHeight(this.world, blockpos$mutable));
                    }

                    if (f >= 1.0F) {
                        continue label39;
                    }
                }
            }

            if (f < 1.0F) {
                return (float)blockpos$mutable.getY() + f;
            }
        }

        return (float)(l + 1);
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        this.lastYd = this.getMotion().y;
        if (!this.isPassenger()) {
            if (onGroundIn) {
                if (this.fallDistance > 3.0F) {
                    if (this.status != PanthalassaVehicle.Status.ON_LAND) {
                        this.fallDistance = 0.0F;
                        return;
                    }

                    this.onLivingFall(this.fallDistance, 1.0F);
                    if (!this.world.isRemote && !this.removed) {
                        this.remove();
                    }
                }
            }
            this.fallDistance = 0.0F;
        } else if (!this.world.getFluidState(this.getPosition().down()).isTagged(FluidTags.WATER) && y < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - y);
        }
    }
}
