package com.github.sniffity.panthalassa.server.entity.vehicle;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import com.github.sniffity.panthalassa.server.registry.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraftforge.network.NetworkHooks;
import org.lwjgl.system.CallbackI;

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

    protected static final EntityDataAccessor<Float> MAX_HEALTH = SynchedEntityData.defineId(PanthalassaVehicle.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> HEALTH = SynchedEntityData.defineId(PanthalassaVehicle.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> ARMOR = SynchedEntityData.defineId(PanthalassaVehicle.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Float> NLF_DISTANCE = SynchedEntityData.defineId(PanthalassaVehicle.class, EntityDataSerializers.FLOAT);
    protected static final EntityDataAccessor<Integer> FLOOR_DISTANCE = SynchedEntityData.defineId(PanthalassaVehicle.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> LIGHTS_ON = SynchedEntityData.defineId(PanthalassaVehicle.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> ENTRY_X = SynchedEntityData.defineId(PanthalassaVehicle.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> ENTRY_Z = SynchedEntityData.defineId(PanthalassaVehicle.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> LIGHT_X = SynchedEntityData.defineId(PanthalassaVehicle.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> LIGHT_Y = SynchedEntityData.defineId(PanthalassaVehicle.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> LIGHT_Z = SynchedEntityData.defineId(PanthalassaVehicle.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Float> SONAR_COOLDOWN = SynchedEntityData.defineId(PanthalassaVehicle.class, EntityDataSerializers.FLOAT);

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
    public BlockState blockLightWater = PanthalassaBlocks.LIGHT_WATER.get().defaultBlockState();
    public BlockState blockLightAir = PanthalassaBlocks.LIGHT_AIR.get().defaultBlockState();
    public ResourceKey<Level> prevDimension;
    public ItemStack itemStack;
    public static final String VEHICLE_HEALTH = "VehicleHealth";
    public static final String TEXTURE_VARIANT = "TextureVariant";
    public static final String TORPEDO_COUNT = "TorpedoCount";


    public PanthalassaVehicle(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(NLF_DISTANCE, -1.00F);
        this.entityData.define(FLOOR_DISTANCE, -1);
        this.entityData.define(LIGHTS_ON, Boolean.FALSE);
        this.entityData.define(SONAR_COOLDOWN, 0.00F);
        this.entityData.define(ENTRY_X, 0);
        this.entityData.define(ENTRY_Z, 0);
        this.entityData.define(LIGHT_X, 0);
        this.entityData.define(LIGHT_Y, 0);
        this.entityData.define(LIGHT_Z, 0);

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("MaxHealth")) {
            this.setMaxHealth(compound.getFloat("MaxHealth"));
        }
        if (compound.contains("Health")) {
            this.setHealth(compound.getFloat("Health"));
        }
        if (compound.contains("Armor")) {
            this.setArmor(compound.getFloat("Armor"));
        }
        if (compound.contains("NLFDistance")) {
            this.setNLFDistance(compound.getFloat("NLFDistance"));
        }
        if (compound.contains("FloorDistance")) {
            this.setFloorDistance(compound.getInt("FloorDistance"));
        }
        if (compound.contains("LightsOn")) {
            this.setLightsOn(compound.getBoolean("LightsOn"));
        }
        if (compound.contains("SonarCooldown")) {
            this.setSonarCooldown(compound.getFloat("SonarCooldown"));
        }
        if (compound.contains("LightX")) {
            this.setLightPosX(compound.getInt("LightX"));
        }
        if (compound.contains("LightY")) {
            this.setLightPosY(compound.getInt("LightY"));
        }
        if (compound.contains("LightZ")) {
            this.setLightPosZ(compound.getInt("LightZ"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
            compound.putFloat("MaxHealth", this.getMaxHealth());
            compound.putFloat("Health", this.getHealth());
            compound.putFloat("Armor", this.getArmor());
            compound.putFloat("NLFDistance", this.getNLFDistance());
            compound.putInt("FloorDistance", this.getFloorDistance());
            compound.putBoolean("LightsOn", this.getLightsOn());
            compound.putFloat("SonarCooldown", this.getSonarCooldown());
            compound.putInt("LightX", this.getLightPos().getX());
            compound.putInt("LightY", this.getLightPos().getY());
            compound.putInt("LightZ", this.getLightPos().getZ());
    }

    public static boolean canVehicleCollide(Entity p_242378_0_, Entity entity) {
        return (entity.canBeCollidedWith() || entity.isPushable()) && !p_242378_0_.isPassengerOfSameVehicle(entity);
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean isPickable() {
        return this.isAlive();
    }

    public boolean canCollideWith(Entity entity) {
        return canVehicleCollide(this, entity);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.level.isClientSide && stack.getItem() == PanthalassaItems.VEHICLE_PICKUP_TOOL.get()) {
            if (isRemoved())
                return InteractionResult.PASS;
            this.discard();
            CompoundTag tag = itemStack.getOrCreateTag();
            tag.putFloat(VEHICLE_HEALTH,this.getHealth());
            itemStack.setTag(tag);
            if (this instanceof VehicleMRSV) {
                tag.putInt(TEXTURE_VARIANT,((VehicleMRSV) this).getTextureVariant());
            }
            if (this instanceof VehicleAGII) {
                tag.putInt(TEXTURE_VARIANT,((VehicleAGII) this).getTextureVariant());
            }
            if (this instanceof VehiclePCSV) {
                tag.putInt(TORPEDO_COUNT,((VehiclePCSV) this).getTorpedoCount());
            }
            if (this instanceof VehicleECSV) {
                tag.putInt(TORPEDO_COUNT,((VehicleECSV) this).getTorpedoCount());
            }
            this.spawnAtLocation(itemStack, 0.0F);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (!this.level.isClientSide && canAddPassenger(this)) {
            return player.startRiding(this) ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
        } else {
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    @Override
    protected boolean canAddPassenger(Entity vehicle) {
        return this.getPassengers().size() < 1;
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

        if (this.level.dimension() == PanthalassaDimension.PANTHALASSA && prevDimension != PanthalassaDimension.PANTHALASSA) {
            setEntryX((int) this.position().x);
            setEntryZ((int) this.position().z);
        } else if (prevDimension == PanthalassaDimension.PANTHALASSA && this.level.dimension() != PanthalassaDimension.PANTHALASSA) {
            setEntryX(0);
            setEntryZ(0);
        }

        if (getSonarCooldown()>-1) {
            setSonarCooldown(getSonarCooldown() - 1);
        }

        if (!passengers.isEmpty()) {
            for (Entity passenger : passengers) {
                LivingEntity currentPassenger = (LivingEntity) passenger;
                currentPassenger.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 10, 0));
            }
        }

        if (prevPos != null) {
            Vec3 prevPosV = new Vec3(prevPos.getX(), prevPos.getY(), prevPos.getZ());
            BlockPos vehiclePos = this.blockPosition();
            Vec3 vehiclePosV = new Vec3(vehiclePos.getX(), vehiclePos.getY(), vehiclePos.getZ());
            double distanceMoved = (prevPosV.subtract(vehiclePosV)).length();
            boolean hasLooped = false;
            BlockState vehiclePosBlockState = level.getBlockState(vehiclePos);

            if (((distanceMoved > 1) || ((!this.isInWater() && this.isOnGround()) && (distanceMoved > 0.2)) || !this.getLightsOn())) {

                BlockPos prevLight = this.getLightPos();
                if (prevLight != null) {
                    if (level.getBlockState(prevLight) == blockLightWater) {
                        if (this.getLightsOn() && this.isInWater()) {
                            level.setBlock(vehiclePos, blockLightWater, 2);
                            this.setLightPos(vehiclePos);
                        } else if (this.getLightsOn() && (!this.isInWater() && this.isOnGround())) {
                            level.setBlock(vehiclePos, blockLightAir, 2);
                            this.setLightPos(vehiclePos);
                        }
                        level.setBlock(prevLight, Blocks.WATER.defaultBlockState(), 2);
                    }
                    if (level.getBlockState(prevLight) == blockLightAir) {
                        if (this.getLightsOn() && (!this.isInWater() && this.isOnGround())) {
                            level.setBlock(vehiclePos, blockLightAir, 2);
                            this.setLightPos(vehiclePos);
                        } else if (this.getLightsOn() && this.isInWater()) {
                            level.setBlock(vehiclePos, blockLightWater, 2);
                            this.setLightPos(vehiclePos);
                        }
                        level.setBlock(prevLight, Blocks.AIR.defaultBlockState(), 2);
                    }
                    hasLooped = true;
                }
            }

            if (!hasLooped && (vehiclePosBlockState != blockLightWater && vehiclePosBlockState != blockLightAir)) {
                BlockPos prevLight = this.getLightPos();

                if (this.getLightsOn() && this.isInWater() && level.getBlockState(getLightPos()) != PanthalassaBlocks.LIGHT_WATER.get().defaultBlockState()) {
                    level.setBlock(vehiclePos, blockLightWater, 2);
                    this.setLightPos(vehiclePos);
                    if (prevLight != null) {
                        if (level.getBlockState(prevLight) == blockLightWater){
                            level.setBlock(prevLight, Blocks.WATER.defaultBlockState(), 2);


                        }
                        else if (level.getBlockState(prevLight) == blockLightAir){
                            level.setBlock(prevLight, Blocks.AIR.defaultBlockState(), 2);

                        }
                    }
                }
                else if (this.getLightsOn() && (!this.isInWater() && this.isOnGround()) && level.getBlockState(getLightPos()) != PanthalassaBlocks.LIGHT_AIR.get().defaultBlockState()) {
                    level.setBlock(vehiclePos, blockLightAir, 2);
                    this.setLightPos(vehiclePos);
                    if (prevLight != null) {
                        if (level.getBlockState(prevLight) == blockLightWater){
                            level.setBlock(prevLight, Blocks.WATER.defaultBlockState(), 2);

                        }
                        else if (level.getBlockState(prevLight) == blockLightAir){
                            level.setBlock(prevLight, Blocks.AIR.defaultBlockState(), 2);

                        }
                    }
                }
            }
        }
        prevPos = this.blockPosition();
        prevDimension = this.level.dimension();
        this.vehicleTick();
    }

    public void vehicleTick() {
        if (this.isControlledByLocalInstance()) {
            this.newPosRotationIncrements = 0;
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
        }

        Vec3 vector3d = this.getDeltaMovement();
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

        this.setDeltaMovement(d1, d3, d5);
        this.level.getProfiler().push("ai");
        if (this.isServerWorld()) {
            this.level.getProfiler().push("newAi");
            this.updateEntityActionState();
            this.level.getProfiler().pop();
        }

        this.level.getProfiler().pop();

        this.level.getProfiler().push("travel");
        this.moveStrafing *= 0.98F;
        this.moveForward *= 0.98F;
        this.vehicleTravel(new Vec3(this.moveStrafing, this.moveVertical, this.moveForward));
        this.level.getProfiler().pop();

        this.level.getProfiler().push("push");
        this.level.getProfiler().pop();

    }

    protected void updateEntityActionState() {
    }

    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.interpTargetX = x;
        this.interpTargetY = y;
        this.interpTargetZ = z;
        this.interpTargetYaw = yaw;
        this.interpTargetPitch = pitch;
        this.newPosRotationIncrements = posRotationIncrements;
    }

    public boolean isServerWorld() {
        return !this.level.isClientSide;
    }

    public void vehicleTravel(Vec3 vec3d) {
        if (isInWater()) {
            if (getControllingPassenger() instanceof LivingEntity) {
                float speed = getTravelSpeed();
                LivingEntity entity = (LivingEntity) getControllingPassenger();
                double moveY = vec3d.y;
                double moveX = vec3d.x;
                double moveZ = entity.zza;

                yRot = entity.yRot;
                xRot = entity.xRot * 0.5F;

                double lookY = entity.getLookAngle().y;

                if (entity.zza != 0 && (isUnderWater() || lookY < 0)) moveY = lookY;
                setAIMoveSpeed(speed);

                vec3d = new Vec3(moveX, moveY, moveZ);

            } else {
                setDeltaMovement(getDeltaMovement().add(0, -0.003, 0));
            }

            moveRelative(getAIMoveSpeed(), vec3d);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.9d));


        } else if (isOnGround()) {
            if (getControllingPassenger() instanceof LivingEntity) {
                float speed = getTravelSpeed();
                LivingEntity entity = (LivingEntity) getControllingPassenger();
                double moveY = vec3d.y;
                double moveX = vec3d.x;
                double moveZ = entity.zza;

                yRot = entity.yRot;

                setAIMoveSpeed(speed);
                vec3d = new Vec3(moveX, moveY, moveZ);

            }
            moveRelative(getAIMoveSpeed(), vec3d);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.9d));

            if (vec3d.z == 0) {
                setDeltaMovement(getDeltaMovement().add(0, -0.003d, 0));
            }
        } else if (!isOnGround()) {
            if (getControllingPassenger() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) getControllingPassenger();
                yRot = entity.yRot;
                xRot = entity.xRot * 0.5F;
            }
            double d0 = 0.08D;
            BlockPos blockpos = this.getBlockPosBelowThatAffectsMyMovement();
            float f3 = this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getFriction(level, this.getBlockPosBelowThatAffectsMyMovement(), this);
            Vec3 vec5 = handleRelativeFrictionAndCalculateMovement(vec3d, f3);
            double d2 = vec5.y;
            if (this.level.isClientSide && !this.level.hasChunkAt(blockpos)) {
                if (this.getY() > 0.0D) {
                    d2 = -0.1D;
                } else {
                    d2 = 0.0D;
                }
            } else if (!this.isNoGravity()) {
                d2 -= d0;
            }
            this.setDeltaMovement(getDeltaMovement().x(), d2 * (double) 0.98F, getDeltaMovement().z());
        }
    }

    public Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 vec3d, float d3) {
        this.moveRelative(this.getRelevantMoveFactor(d3), vec3d);
        this.setDeltaMovement(this.getDeltaMovement());
        this.move(MoverType.SELF, this.getDeltaMovement());
        return this.getDeltaMovement();
    }

    private float getRelevantMoveFactor(float p_213335_1_) {
        return this.onGround ? this.getAIMoveSpeed() * (0.21600002F / (p_213335_1_ * p_213335_1_ * p_213335_1_)) : this.jumpMovementFactor;
    }

    @Override
    public boolean hurt(@Nullable DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!this.level.isClientSide && this.isAlive()) {
            Entity trueSource = source.getEntity();
            if (source instanceof IndirectEntityDamageSource && trueSource != null && this.hasPassenger(trueSource)) {
                return false;
            } else {
                float adjustedAmount = (((100 - this.getArmor()) / 100) > 0) ? amount * (((100 - this.getArmor()) / 100)) : 0;
                this.setHealth(this.getHealth() - adjustedAmount);

                boolean isCreativeMode = trueSource instanceof Player && ((Player) trueSource).isCreative();
                if (isCreativeMode || this.getHealth() < 0.0F) {
                    AABB searchArea = new AABB(this.getX() - 10, this.getY() - 10, this.getZ() - 10, this.getX() + 10, this.getY() + 10, this.getZ() + 10);
                    Set<BlockPos> set = BlockPos.betweenClosedStream(searchArea)
                            .map(pos -> new BlockPos(pos))
                            .filter(state -> (level.getBlockState(state) == PanthalassaBlocks.LIGHT_WATER.get().defaultBlockState() || level.getBlockState(state) == PanthalassaBlocks.LIGHT_AIR.get().defaultBlockState()))
                            .collect(Collectors.toSet());
                    Iterator<BlockPos> it = set.iterator();

                    while (it.hasNext()) {
                        BlockPos lightPos = it.next();

                        if (level.getBlockState(lightPos) == PanthalassaBlocks.LIGHT_WATER.get().defaultBlockState()) {
                            level.setBlock(lightPos, Blocks.WATER.defaultBlockState(), 2);
                        }
                        if (level.getBlockState(lightPos) == PanthalassaBlocks.LIGHT_AIR.get().defaultBlockState()) {
                                level.setBlock(lightPos, Blocks.AIR.defaultBlockState(), 2);
                        }
                    }

                    this.discard();
                }
                return true;
            }
        } else {
            return true;
        }
    }

    public void setHealth(float health) {
        this.entityData.set(HEALTH, Math.min(this.getMaxHealth(), health));
    }

    public float getHealth() {
        return this.entityData.get(HEALTH);
    }

    public float getMaxHealth() {
        return this.entityData.get(MAX_HEALTH);
    }

    public void setMaxHealth(float maxHealth) {
        this.entityData.set(MAX_HEALTH, maxHealth);
    }

    public void setArmor(float armor) {
        this.entityData.set(ARMOR, armor);
    }

    public float getArmor() {
        return this.entityData.get(ARMOR);
    }

    public float testNLFDistance(PanthalassaVehicle vehicle) {
        List<Entity> entities = level.getEntities(vehicle, new AABB(vehicle.getX() - 20, vehicle.getY() - 20, vehicle.getZ() - 20, vehicle.getX() + 20, vehicle.getY() + 20, vehicle.getZ() + 20));
        float closestDistance = 100F;
        if (entities.size() != 0) {
            for (Entity testEntity : entities) {
                if (testEntity instanceof LivingEntity && !(testEntity instanceof Player)) {
                    if (testEntity instanceof PanthalassaEntity) {
                        if (level.random.nextFloat() < 0.75) {
                            ((PanthalassaEntity) testEntity).addEffect(new MobEffectInstance(PanthalassaEffects.DISORIENT.get(), 30, 0));
                        }
                    }
                    float distance = distanceTo(testEntity);
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
        this.entityData.set(NLF_DISTANCE, nlfDistance);
    }

    public float getNLFDistance() {
        return this.entityData.get(NLF_DISTANCE);
    }

    public int testFloorDistance(PanthalassaVehicle vehicle, Level world) {
        BlockPos pos = vehicle.blockPosition();
        while (pos.getY() > 0) {
            if (!(world.getBlockState(pos).canOcclude())) {
                pos = pos.below();
            } else {
                return (vehicle.blockPosition().getY() - pos.getY());
            }
        }
        return -1;
    }

    public void setFloorDistance(int floorDistance) {
        this.entityData.set(FLOOR_DISTANCE,floorDistance);
    }

    public int getFloorDistance() {
       return this.entityData.get(FLOOR_DISTANCE);
    }

    public void setLightsOn(boolean lightState) {
        this.entityData.set(LIGHTS_ON,lightState);
    }

    public boolean getLightsOn() {
        return this.entityData.get(LIGHTS_ON);
    }

    public void setEntryX(int x) {
        this.entityData.set(ENTRY_X,x);
    }

    public float getEntryX() {
        return this.entityData.get(ENTRY_X);
    }

    public void setEntryZ(int z) {
        this.entityData.set(ENTRY_Z,z);
    }

    public float getEntryZ() {
        return this.entityData.get(ENTRY_Z);
    }

    public void setLightPosX(int x) {
        this.entityData.set(LIGHT_X, x);
    }

    public void setLightPosY(int y) {
        this.entityData.set(LIGHT_Y, y);
    }

    public void setLightPosZ(int z) {
        this.entityData.set(LIGHT_Z, z);
    }

    public void setLightPos(BlockPos blockPos) {
        this.entityData.set(LIGHT_X, blockPos.getX());
        this.entityData.set(LIGHT_Y, blockPos.getY());
        this.entityData.set(LIGHT_Z, blockPos.getZ());
    }

    public void setSonarCooldown(float cooldown) {
        this.entityData.set(SONAR_COOLDOWN,cooldown);
    }

    public float getSonarCooldown() {
        return this.entityData.get(SONAR_COOLDOWN);
    }

    public BlockPos getLightPos() {
        BlockPos blockpos = new BlockPos (this.entityData.get(LIGHT_X),this.entityData.get(LIGHT_Y), this.entityData.get(LIGHT_Z));
        return blockpos;
    }

    public void respondKeybindSpecial() {
    }

    public void respondKeybindSonar() {
        if (!this.getPassengers().isEmpty()) {
            if (this.getSonarCooldown() < 0) {
                this.setSonarCooldown(200);
                this.checkedNLFDistance = testNLFDistance(this);
                setNLFDistance(checkedNLFDistance);
                this.checkedFloorDistance = testFloorDistance(this, this.level);
                setFloorDistance(checkedFloorDistance);
                if (!this.isSilent()) {
                    playSound(PanthalassaSounds.VEHICLE_SONAR.get(), 1, 1);
                }
            }
        }
    }

    public void respondKeybindLight() {
        setLightsOn(!getLightsOn());
    }

    @Override
    public boolean fireImmune(){
        return true;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

}