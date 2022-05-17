package com.github.sniffity.panthalassa.server.entity.projectile;

import com.github.sniffity.panthalassa.server.entity.vehicle.PanthalassaVehicle;
import com.mojang.math.Vector3d;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import javax.annotation.Nullable;


public class ProjectileTorpedo extends Entity implements IEntityAdditionalSpawnData
{
    @Nullable
    public PanthalassaVehicle source;
    public Vec3 acceleration;
    public float growthRate = 1f;
    public int life;
    public boolean hasCollided;

    protected ProjectileTorpedo(EntityType<?> type, Level level)
    {
        super(type, level);
    }

    public ProjectileTorpedo(EntityType<? extends ProjectileTorpedo> type, PanthalassaVehicle source, Vec3 position, Vec3 direction)
    {
        super(type, source.level);
        direction = direction.add(new Vector3d((random.nextGaussian() * getAccelerationOffset(), random.nextGaussian() * getAccelerationOffset(), random.nextGaussian() * getAccelerationOffset()));
        double length = direction.length();
        this.acceleration = new Vec3(direction.x / length * getMotionFactor(), direction.y / length * getMotionFactor(), direction.z / length * getMotionFactor());
        this.source = source;
        this.life = 50;
        this.setDeltaMovement(acceleration);
        position = position.add(this.getDeltaMovement()).subtract(0, getBbHeight() / 2, 0);

        moveTo(position.x, position.y, position.z, yRot, xRot);
    }

    @Override
    public void tick()
    {
        if ((!level.isClientSide && (!source.isAlive() || tickCount > life || tickCount > getMaxLife())) || !level.hasChunkAt(blockPosition()))
        {
            this.discard();
            return;
        }

        super.tick();
        if (growthRate != 1) refreshDimensions();

        AABB boundingBox = getBoundingBox().inflate(0.05);
        if (!level.getEntities(this, boundingBox, this::canImpactEntity).isEmpty()){
            impact();


        } else if () {

        }
        Vec3 position = this.position();
        Vec3 end = position.add(getDeltaMovement());

        BlockRayTraceResult rtr = level.clip(new RayTraceContext(position, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
            if (rtr.getType() != RayTraceResult.Type.MISS) onBlockImpact(rtr.getBlockPos(), rtr.getDirection());



        Vec3 motion = getDeltaMovement();
        if (!isNoGravity()) setDeltaMovement(motion = motion.add(0, -0.05, 0));
        double x = getX() + motion.x;
        double y = getY() + motion.y;
        double z = getZ() + motion.z;
        absMoveTo(x, y, z);

    }

    public boolean canImpactEntity(Entity entity)
    {
        if (entity == source) return false;
        if (!entity.isAlive()) return false;
        if (!(entity instanceof LivingEntity)) return false;
        if (entity.getRootVehicle() == source) return false;
        if (entity.isSpectator() || !entity.isPickable() || entity.noPhysics) return false;
        return source != null && !entity.isAlliedTo(source);
    }

    public void hit(RayTraceResult result)
    {
        RayTraceResult.Type type = result.getType();
        if (type == RayTraceResult.Type.BLOCK)
        {
            final BlockRayTraceResult brtr = (BlockRayTraceResult) result;
            onBlockImpact(brtr.getBlockPos(), brtr.getDirection());
        }
        else if (type == RayTraceResult.Type.ENTITY) onEntityImpact(((EntityRayTraceResult) result).getEntity());
    }

    public void onEntityImpact(Entity entity)
    {
    }

    public void onBlockImpact(BlockPos pos, Direction direction)
    {
    }
    public void impact()
    {
    }
    @Override
    public void setDeltaMovement(Vector3d motionIn)
    {
        super.setDeltaMovement(motionIn);
        ProjectileHelper.rotateTowardsMovement(this, 1);
    }

    @Override
    public EntitySize getDimensions(Pose poseIn)
    {
        if (growthRate == 1) return getType().getDimensions();
        float size = Math.min(getBbWidth() * growthRate, 2.25f);
        return EntitySize.scalable(size, size);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance)
    {
        double d0 = getBoundingBox().getSize() * 4;
        if (Double.isNaN(d0)) d0 = 4;
        d0 *= 64;
        return distance < d0 * d0;
    }

    public DamageSource getDamageSource(String name)
    {
        return new IndirectEntityDamageSource(name, this, shooter).setProjectile().setScalesWithDifficulty();
    }

    protected EffectType getEffectType()
    {
        return EffectType.NONE;
    }

    protected float getMotionFactor()
    {
        return 0.95f;
    }

    protected double getAccelerationOffset()
    {
        return 0.1;
    }

    protected int getMaxLife()
    {
        return 150;
    }

    @Override
    public boolean isNoGravity()
    {
        return true;
    }

    @Override
    protected boolean isMovementNoisy()
    {
        return false;
    }

    @Override
    public float getBrightness()
    {
        return 1f;
    }

    @Override
    public boolean hurt(DamageSource source, float amount)
    {
        return false;
    }

    @Override
    public float getPickRadius()
    {
        return getBbWidth();
    }

    @Override
    protected void defineSynchedData()
    {
    }

    @Override // Does not Serialize
    protected void readAdditionalSaveData(CompoundNBT compound)
    {
    }

    @Override // Does not Serialize
    protected void addAdditionalSaveData(CompoundNBT compound)
    {
    }

    @Override
    public IPacket<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buf)
    {
        buf.writeInt(shooter.getId());
        buf.writeFloat(growthRate);
    }

    @Override
    public void readSpawnData(PacketBuffer buf)
    {
        this.shooter = (TameableDragonEntity) level.getEntity(buf.readInt());
        this.growthRate = buf.readFloat();
    }

    protected enum EffectType
    {
        NONE,
        RAYTRACE,
        COLLIDING
    }
}