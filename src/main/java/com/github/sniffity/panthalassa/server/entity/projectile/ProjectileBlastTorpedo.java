package com.github.sniffity.panthalassa.server.entity.projectile;

import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Panthalassa Mod - Class: ProjectileBlastTorpedo <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Wyrmroost
 * implements projectiles generated from entities.
 */


public class ProjectileBlastTorpedo extends Entity implements IEntityAdditionalSpawnData, IAnimatable {
    @Nullable
    public PanthalassaVehicle source;
    public Vec3 acceleration;
    public float life;

    public ProjectileBlastTorpedo(EntityType<?> type, Level level) {
        super(type, level);
    }

    public ProjectileBlastTorpedo(EntityType<? extends ProjectileBlastTorpedo> type, PanthalassaVehicle source, Vec3 position, Vec3 direction) {
        super(type, source.level);
        //Direction is taken and a small "push" is added to it, this will very slightly change the direction of this vector...
        direction = direction.add(new Vec3(random.nextGaussian() * getAccelerationOffset(), random.nextGaussian() * getAccelerationOffset(), random.nextGaussian() * getAccelerationOffset()));
        //The new direction unit vector is taken, and then scaled up...
        //This ensures that all vectors for projectiles are of the same "size", despite their differences in direction...
        double length = direction.length();
        this.acceleration = new Vec3(direction.x / length * getMotionFactor(), direction.y / length * getMotionFactor(), direction.z / length * getMotionFactor());
        this.source = source;
        this.life = 200;
        //deltaMovement is set to the previously defined vector...
        this.setDeltaMovement(acceleration);
        //The starting position is ever so slightly adjusted, to the center of its bounding box
        //Of note: This will not move the entity, it will simply change what we define as its position for fuutre calculations.
        position = position.add(this.getDeltaMovement()).subtract(0, getBbHeight() / 2, 0);

        moveTo(position.x, position.y, position.z, yRot, xRot);
    }

    @Override
    public void tick() {
        //If the source dies, or the projectile has existed for over 200 ticks, or the chunk the projectile's at is not loaded...
        if ((!level.isClientSide && (tickCount > life )) || !level.hasChunkAt(this.blockPosition())) {
            //Remove the projectile...
            this.discard();
            return;
        }
        ProjectileUtil.rotateTowardsMovement(this, 1);

        super.tick();

        //Check for collisions, either with entities or with blocks...
        //Define a bounding box....
        AABB boundingBox = getBoundingBox().inflate(0.05);
        //If any of the entities that meet the canImpactEntity condition are within the bounding box...
        List<Entity> entities = level.getEntities(this, boundingBox, this::canImpactEntity);
        if (!entities.isEmpty()){
            //Proceed to register an impact...
            impact(new BlockPos(this.position()));
        } else {
            //Else, check for block collision...
            Vec3 position = this.position();
            Vec3 end = position.add(getDeltaMovement());
            HitResult raytraceresult = level.clip(new ClipContext(position, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if (raytraceresult.getType() != HitResult.Type.MISS){
                //If there is a collision, proceed to register an impact...
                impact(new BlockPos(this.position()));
            }
        }


        //Continue moving the projectile along its path...
        Vec3 motion = getDeltaMovement();
        double x = getX() + motion.x;
        double y = getY() + motion.y;
        double z = getZ() + motion.z;
        //Of note, this will not change the actual position of the entity, it will not move it...
        //Instead it will update the values we are using to calculate collisions...
        absMoveTo(x, y, z);

        if (level.isClientSide){
            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    this.level.addParticle(ParticleTypes.BUBBLE, x - motion.x * 0.25D, y - motion.y * 0.25D, z - motion.z * 0.25D, motion.x, motion.y, motion.z);
                }
            } else {
                this.level.addParticle(ParticleTypes.CLOUD, x - motion.x * 0.25D, y - motion.y * 0.25D, z - motion.z * 0.25D, motion.x, motion.y, motion.z);
            }
        }
    }

    public boolean canImpactEntity(Entity entity) {
        if (entity == source) {
            return false;
        }
        if (!entity.isAlive()) {
            return false;
        }
        if (!(entity instanceof LivingEntity)) {
            return false;
        }
        if (entity.getRootVehicle() == source) {
            return false;
        }
        if (entity.isSpectator() || !entity.isPickable() || entity.noPhysics) {
            return false;
        }
        return source != null && !entity.isAlliedTo(source);
    }

    public void impact(BlockPos impactPos) {
        this.level.explode(null, impactPos.getX(), impactPos.getY(), impactPos.getZ(), 5.0F, true, Explosion.BlockInteraction.DESTROY);
        AABB boundingBox = getBoundingBox().inflate(4);
        List<Entity> entities = level.getEntities(this, boundingBox, this::canImpactEntity);
        if (!entities.isEmpty()) {
            for (Entity entity : entities) {
                entity.hurt(DamageSource.explosion((LivingEntity) entity),50F);
            }
        }
        this.discard();
    }

    @Override
    public void setDeltaMovement(Vec3 motionIn) {
        super.setDeltaMovement(motionIn);
        //TODO: Examine this
        //TODO: On render class, rotate bone to match xRot and yRot?
        //TODO: This is only being called once, initially. Perhaps call it again to re-adjust.
        ProjectileUtil.rotateTowardsMovement(this, 1);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = getBoundingBox().getSize() * 4;
        if (Double.isNaN(d0)) d0 = 4;
        d0 *= 64;
        return distance < d0 * d0;
    }

    protected float getMotionFactor() {
        return 0.95f;
    }

    protected double getAccelerationOffset() {
        return 0.1;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public float getBrightness() {
        return 1f;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    public float getPickRadius() {
        return getBbWidth();
    }

    @Override
    protected void defineSynchedData() {
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(source.getId());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.source = (PanthalassaVehicle) level.getEntity(buffer.readInt());

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
}