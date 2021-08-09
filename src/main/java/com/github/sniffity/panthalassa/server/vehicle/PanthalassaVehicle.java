package com.github.sniffity.panthalassa.server.vehicle;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Panthalassa Mod - Class: PanthalassaVehicle <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Wyrmroost
 * and Mr. Crayfish's Vehicle Mod handle their own respective vehicles. Additionally,
 * Vanilla Minecraft's BoatEntity class was also studied.
 */

public class PanthalassaVehicle extends Entity {

    protected static final DataParameter<Float> MAX_HEALTH = EntityDataManager.createKey(PanthalassaVehicle.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> HEALTH = EntityDataManager.createKey(PanthalassaVehicle.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> ARMOR = EntityDataManager.createKey(PanthalassaVehicle.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> NLF_DISTANCE = EntityDataManager.createKey(PanthalassaVehicle.class, DataSerializers.FLOAT);
    protected static final DataParameter<Integer> FLOOR_DISTANCE = EntityDataManager.createKey(PanthalassaVehicle.class, DataSerializers.VARINT);
    protected static final DataParameter<Boolean> LIGHTS_ON = EntityDataManager.createKey(PanthalassaVehicle.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> SONAR_LAST_CHECK = EntityDataManager.createKey(PanthalassaVehicle.class, DataSerializers.FLOAT);

    public float waterSpeed;
    public float landSpeed;
    public static float aiMoveSpeed;
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
    public float checkedNLFDistance;
    public int checkedFloorDistance;
    public BlockPos prevPos;
    public BlockState blockLightWater = PanthalassaBlocks.LIGHT_WATER.get().getDefaultState();
    public BlockState blockLightAir = PanthalassaBlocks.LIGHT_AIR.get().getDefaultState();

    public PanthalassaVehicle(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected void registerData() {

        this.dataManager.register(NLF_DISTANCE, -1.00F);
        this.dataManager.register(FLOOR_DISTANCE, -1);
        this.dataManager.register(LIGHTS_ON, Boolean.FALSE);
        this.dataManager.register(SONAR_LAST_CHECK, 0.00F);


    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        if (compound.contains("MaxHealth", Constants.NBT.TAG_FLOAT)) {
            this.setMaxHealth(compound.getFloat("MaxHealth"));
        }
        if (compound.contains("Health", Constants.NBT.TAG_FLOAT)) {
            this.setHealth(compound.getFloat("Health"));
        }
        if (compound.contains("Armor", Constants.NBT.TAG_FLOAT)) {
            this.setArmor(compound.getFloat("Armor"));
        }
        if (compound.contains("NLFDistance", Constants.NBT.TAG_FLOAT)) {
            this.setNLFDistance(compound.getFloat("NLFDistance"));
        }
        if (compound.contains("FloorDistance", Constants.NBT.TAG_FLOAT)) {
            this.setFloorDistance(compound.getInt("FloorDistance"));
        }
        if (compound.contains("LightsOn", Constants.NBT.TAG_BYTE)) {
            this.setLightsOn(compound.getBoolean("LightsOn"));
        }
        if (compound.contains("SonarLastCheck", Constants.NBT.TAG_FLOAT)) {
            this.setSonarLastCheck(compound.getFloat("SonarLastCheck"));
        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        {
            compound.putFloat("MaxHealth", this.getMaxHealth());
            compound.putFloat("Health", this.getHealth());
            compound.putFloat("Armor", this.getArmor());
            compound.putFloat("NLFDistance", this.getNLFDistance());
            compound.putInt("FloorDistance", this.getFloorDistance());
            compound.putBoolean("LightsOn", this.getLightsOn());
            compound.putFloat("SonarLastCheck", this.getSonarLastCheck());
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

    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        if (!this.world.isRemote) {
            return player.startRiding(this) ? ActionResultType.CONSUME : ActionResultType.PASS;
        } else {
            return ActionResultType.SUCCESS;
        }
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < 1;
    }

    @Override
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
        List<Entity> passengers = this.getPassengers();

        if (!passengers.isEmpty()) {
            for (Entity passenger : passengers) {
                LivingEntity currentPassenger = (LivingEntity) passenger;
                currentPassenger.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, 10, 0));
            }
        }

        if (prevPos != null) {
            Vector3d prevPosV = new Vector3d(prevPos.getX(), prevPos.getY(), prevPos.getZ());
            BlockPos vehiclePos = this.getPosition();
            Vector3d vehiclePosV = new Vector3d(vehiclePos.getX(), vehiclePos.getY(), vehiclePos.getZ());
            double distanceMoved = (prevPosV.subtract(vehiclePosV)).length();
            boolean hasLooped = false;
            BlockState vehiclePosBlockState = world.getBlockState(vehiclePos);

            if (((distanceMoved > 1) || ((!this.isInWater() && this.isOnGround()) && (distanceMoved > 0.2)) || !this.getLightsOn())) {
                AxisAlignedBB searchArea = new AxisAlignedBB(this.getPosX() - 20, this.getPosY() - 20, this.getPosZ() - 20, this.getPosX() + 20, this.getPosY() + 20, this.getPosZ() + 20);
                Set<BlockPos> set = BlockPos.getAllInBox(searchArea)
                        .map(pos -> new BlockPos(pos))
                        .filter(state -> (world.getBlockState(state) == blockLightWater || world.getBlockState(state) == blockLightAir))
                        .collect(Collectors.toSet());
                Iterator<BlockPos> it = set.iterator();

                while (it.hasNext()) {
                    BlockPos lightPos = it.next();

                    if (world.getBlockState(lightPos) == blockLightWater) {
                        if (this.getLightsOn() && this.isInWater()) {
                            world.setBlockState(vehiclePos, blockLightWater, 2);
                        } else if (this.getLightsOn() && (!this.isInWater() && this.isOnGround()))
                        {
                            world.setBlockState(vehiclePos,blockLightAir, 2);
                        }
                        world.setBlockState(lightPos, Blocks.WATER.getDefaultState(), 2);
                    }

                    if (world.getBlockState(lightPos) == blockLightAir) {
                        if (this.getLightsOn() && (!this.isInWater() && this.isOnGround())) {
                            world.setBlockState(vehiclePos,blockLightAir, 2);
                        } else if (this.getLightsOn() && this.isInWater())
                        {
                            world.setBlockState(vehiclePos,blockLightWater, 2);
                        }
                        world.setBlockState(lightPos, Blocks.AIR.getDefaultState(), 2);
                    }
                    hasLooped = true;
                }
            }

            if (!hasLooped && (vehiclePosBlockState != blockLightWater || vehiclePosBlockState != blockLightAir)) {
                    if (this.getLightsOn() && this.isInWater()) {
                        world.setBlockState(vehiclePos, blockLightWater, 2);
                    }
                    if (this.getLightsOn() && (!this.isInWater() && this.isOnGround())) {
                        world.setBlockState(vehiclePos, blockLightAir, 2);
                    }
                }
            }

        prevPos = this.getPosition();
        this.vehicleTick();
    }





    public void vehicleTick() {
        if (this.canPassengerSteer()) {
            this.newPosRotationIncrements = 0;
            this.setPacketCoordinates(this.getPosX(), this.getPosY(), this.getPosZ());
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
        this.vehicleTravel(new Vector3d(this.moveStrafing, this.moveVertical, this.moveForward));
        this.world.getProfiler().endSection();

        this.world.getProfiler().startSection("push");
        this.world.getProfiler().endSection();
    }

    protected void updateEntityActionState() {
    }

    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.interpTargetX = x;
        this.interpTargetY = y;
        this.interpTargetZ = z;
        this.interpTargetYaw = yaw;
        this.interpTargetPitch = pitch;
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

            } else {
                setMotion(getMotion().add(0, -0.003, 0));
            }

            moveRelative(getAIMoveSpeed(), vec3d);
            move(MoverType.SELF, getMotion());
            setMotion(getMotion().scale(0.9d));


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
    public boolean attackEntityFrom(@Nullable DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!this.world.isRemote && this.isAlive()) {
            Entity trueSource = source.getTrueSource();
            if (source instanceof IndirectEntityDamageSource && trueSource != null && this.isPassenger(trueSource)) {
                return false;
            } else {
                float adjustedAmount = (((100 - this.getArmor()) / 100) > 0) ? amount * (((100 - this.getArmor()) / 100)) : 0;
                this.setHealth(this.getHealth() - adjustedAmount);

                boolean isCreativeMode = trueSource instanceof PlayerEntity && ((PlayerEntity) trueSource).isCreative();
                if (isCreativeMode || this.getHealth() < 0.0F) {
                    AxisAlignedBB searchArea = new AxisAlignedBB(this.getPosX() - 10, this.getPosY() - 10, this.getPosZ() - 10, this.getPosX() + 10, this.getPosY() + 10, this.getPosZ() + 10);
                    Set<BlockPos> set = BlockPos.getAllInBox(searchArea)
                            .map(pos -> new BlockPos(pos))
                            .filter(state -> (world.getBlockState(state) == PanthalassaBlocks.LIGHT_WATER.get().getDefaultState() || world.getBlockState(state) == PanthalassaBlocks.LIGHT_AIR.get().getDefaultState()))
                            .collect(Collectors.toSet());
                    Iterator<BlockPos> it = set.iterator();

                    while (it.hasNext()) {
                        BlockPos lightPos = it.next();

                        if (world.getBlockState(lightPos) == PanthalassaBlocks.LIGHT_WATER.get().getDefaultState()) {
                                    world.setBlockState(lightPos, Blocks.WATER.getDefaultState(), 2);
                        }
                        if (world.getBlockState(lightPos) == PanthalassaBlocks.LIGHT_AIR.get().getDefaultState()) {
                                world.setBlockState(lightPos, Blocks.AIR.getDefaultState(), 2);
                        }
                    }

                    this.remove();
                }
                return true;
            }
        } else {
            return true;
        }
    }

    public void setHealth(float health) {
        this.dataManager.set(HEALTH, Math.min(this.getMaxHealth(), health));
    }

    public float getHealth() {
        return this.dataManager.get(HEALTH);
    }

    public float getMaxHealth() {
        return this.dataManager.get(MAX_HEALTH);
    }

    public void setMaxHealth(float maxHealth) {
        this.dataManager.set(MAX_HEALTH, maxHealth);
    }

    public void setArmor(float armor) {
        this.dataManager.set(ARMOR, armor);
    }

    public float getArmor() {
        return this.dataManager.get(ARMOR);
    }

    public float testNLFDistance(PanthalassaVehicle vehicle) {
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(vehicle, new AxisAlignedBB(vehicle.getPosX() - 20, vehicle.getPosY() - 20, vehicle.getPosZ() - 20, vehicle.getPosX() + 20, vehicle.getPosY() + 20, vehicle.getPosZ() + 20));
        float closestDistance = 100F;
        if (entities.size() != 0) {
            for (Entity testEntity : entities) {
                if (testEntity instanceof LivingEntity && !(testEntity instanceof PlayerEntity)) {
                    float distance = getDistance(testEntity);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                    }
                }
            }
            if (closestDistance < 20 && closestDistance>0) {
                return closestDistance;
            }
        }
        return -1.00F;
    }

    public void setNLFDistance(float nlfDistance) {
        this.dataManager.set(NLF_DISTANCE, nlfDistance);
    }

    public float getNLFDistance() {
        return this.dataManager.get(NLF_DISTANCE);
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
        return -1;
    }

    public void setFloorDistance(int floorDistance) {
        this.dataManager.set(FLOOR_DISTANCE,floorDistance);
    }

    public int getFloorDistance() {
       return this.dataManager.get(FLOOR_DISTANCE);
    }

    public void setLightsOn(boolean lightState) {
        this.dataManager.set(LIGHTS_ON,lightState);
    }

    public boolean getLightsOn() {
        return this.dataManager.get(LIGHTS_ON);
    }

    public void setSonarLastCheck(float lastCheck) {
        this.dataManager.set(SONAR_LAST_CHECK,lastCheck);
    }

    public float getSonarLastCheck() {
        return this.dataManager.get(SONAR_LAST_CHECK);
    }



    public void respondKeybindSpecial() {
    }

    public void respondKeybindSonar() {
        if (!this.getPassengers().isEmpty()) {
            if (this.world.getGameTime() - getSonarLastCheck() > 10) {
                setSonarLastCheck(this.world.getGameTime());
                this.checkedNLFDistance = testNLFDistance(this);
                setNLFDistance(checkedNLFDistance);
                this.checkedFloorDistance = testFloorDistance(this, this.world);
                setFloorDistance(checkedFloorDistance);
            }
        }
    }

    public void respondKeybindLight() {
        setLightsOn(!getLightsOn());
    }

}