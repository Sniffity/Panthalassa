package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaSwimmingHelper;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;

import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;

import java.util.Random;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraftforge.common.Tags;

/**
 * Panthalassa Mod - Class: PanthalassaEntity <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements:
 * The methods for switching between land and water navigators was taken from Ice and Fire Sea Serpents mobs,
 * with permission from the mod's creator Alexthe666. All credit goes to him.
 * The methods for dynamically changing the entity's Yaw and Pitch were developed after discussing animation options with
 * BobMowzie.
 */

public abstract class PanthalassaEntity extends PathfinderMob {

    public boolean isLandNavigator;
    public float rotationPitch;
    public float prevRotationPitch;
    public float prevYRot;
    public float deltaYRot;
    public float adjustYaw;
    public float adjustment;


    protected static final EntityDataAccessor<Boolean> ATTACKING_STATE = SynchedEntityData.defineId(PanthalassaEntity.class, EntityDataSerializers.BOOLEAN);

    public PanthalassaEntity(EntityType<? extends PanthalassaEntity> type, Level worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        this.switchNavigators(false);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ATTACKING_STATE, Boolean.FALSE);
        super.defineSynchedData();
    }

    @Override
    public void tick() {
        super.tick();
        //YAW OPERATIONS:
        //The following lines of code handle the dynamic yaw animations for entities...
        //Grab the change in the entity's Yaw, deltaYRot...
        //deltaYaw will tell us in which direction the entity is rotating...
        deltaYRot = this.yRot - prevYRot;
        //Stor the previous yaw value, so we can use itn ext tick to calculate deltaYaw...
        prevYRot = this.yRot;
        //adjustYaw is a local variable that changes to try and match the change in Yaw....
        //So, adjustYaw starts at 0.
        // If it's rotating in the negative direction (deltaYRot negative), adjustYaw will start decreasing to catch up...
        // Likewise, if it's rotating in the positive direction (deltaYRot positive) adjustYaw will start increasing to catch up...
        //The increase or decrease always depends on the adjustment variable. This determines how "fast" adjustYaw will catch up.
        //The max and min functions ensure that adjustYaw doesn't overshoot deltaYRot...
        //Thus, adjustment will determine --how fast-- the pieces of the entity's model change their rotation.
        //The multiplying factor in the corresponding entity's model will determine --how far-- they rotate.
        //Troubleshooting:
        // If the rotation "lags behind" (does not change directions fast enough) increase adjustment.
        // If the rotation looks choppy (adjusts too fast), decrease adjustment
        // If the entity seems to "dislocate", reduce the multipliers for bone rotation in the Model class.
        // Reducing rotation multiplier in model class can also reduce choppiness, at the cost of how wide the bone rotation is.
        if (adjustYaw > deltaYRot) {
            adjustYaw = adjustYaw - adjustment;
            adjustYaw = Math.max(adjustYaw, deltaYRot);
        } else if (adjustYaw < deltaYRot) {
            adjustYaw = adjustYaw + adjustment;
            adjustYaw = Math.min(adjustYaw, deltaYRot);
        }

        prevRotationPitch = rotationPitch;
        rotationPitch = (float)(Mth.atan2((this.getDeltaMovement().y),Mth.sqrt((float) ((this.getDeltaMovement().x)*(this.getDeltaMovement().x)+(this.getDeltaMovement().z)*(this.getDeltaMovement().z)))));

        //NAVIGATOR SWITCH OPERATIONS
        //All entities require a land navigator, to have them not get stuck in 1-block-deep water
        //If it's using the land naviagtor....
        if (this.isLandNavigator) {
            //...and it's in swimmable water (meaning, at least two blocks deep), switch to water navigator
            if (this.isInWater() && !this.level.getBlockState(blockPosition().below()).canOcclude()) {
                switchNavigators(false);
            }
            //If it's not using the land navigator....
        } else {
            //...and it's in 1-block-deep water, switch to land navigator
            if (this.isInWater() && this.level.getBlockState(blockPosition().below()).canOcclude() && this.level.getBlockState(blockPosition().above()).is(Blocks.AIR)) {
                switchNavigators(true);
            }
            //...else, if it's not in water and it's on the ground, switch to land navigator
            else if (!this.isInWater() && this.isOnGround()) {
                switchNavigators(true);
            }
        }
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.90));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        int i = 0;

        if (entityIn instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) entityIn).getMobType());
            i += EnchantmentHelper.getKnockbackBonus(this);
        }

        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f);

        if (flag) {
            if (i > 0) {
                ((Player) entityIn).knockback(i * 0.5F, Mth.sin(this.yRot * 0.017453292F), (-Mth.cos(this.yRot * 0.017453292F)));
            }

            int j = EnchantmentHelper.getFireAspect(this);

            if (j > 0) {
                entityIn.setSecondsOnFire(j * 4);
            }

            if (entityIn instanceof Player) {
                Player entityplayer = (Player) entityIn;
                ItemStack itemstack = this.getMainHandItem();
                ItemStack itemstack1 = entityplayer.isUsingItem() ? entityplayer.getUseItem() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.is(Items.SHIELD)) {
                    float f1 = 0.25F + EnchantmentHelper.getBlockEfficiency(this) * 0.05F;

                    if (this.random.nextFloat() < f1) {
                        entityplayer.getCooldowns().addCooldown(itemstack1.getItem(), 100);
                        this.level.broadcastEntityEvent(entityplayer, (byte) 30);
                    }
                }
            }
            this.doEnchantDamageEffects(this, entityIn);
        }
        return flag;
    }

    public static boolean canPanthalassaEntitySpawn(EntityType<? extends PanthalassaEntity> type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random randomIn) {
        if (pos.getY()>20 && pos.getY()<110) {
            return true;
        }
        return false;
    }

    //Method is overriden to ensure these entities can spawn in water.
    @Override
    public boolean checkSpawnObstruction(LevelReader p_205019_1_) {
        return p_205019_1_.containsAnyLiquid(this.getBoundingBox());
    }

    //Method is overriden to ensure these entities do not despawn, despite them being in the MONSTER MobCategory..
    @Override
    public void checkDespawn() {
    }

    //TODO: Add look controls
    public void switchNavigators(boolean isOnLand){
        if (isOnLand) {
            this.moveControl = new MoveControl(this);
            this.lookControl = new LookControl(this);
            this.navigation = new GroundPathNavigation(this, level);
            this.isLandNavigator = true;
        } else {
            this.moveControl = new PanthalassaSwimmingHelper(this, 85, 0.02F, 0.1F, true);
            this.lookControl = new SmoothSwimmingLookControl(this, 10);
            this.navigation = new WaterBoundPathNavigation(this, level);
            this.isLandNavigator = false;
        }
    }

    //Attacking state used for purposes of attack animations
    public void setAttackingState(boolean isAttacking) {
        this.entityData.set(ATTACKING_STATE,isAttacking);
    }

    public boolean getAttackingState() {
        return this.entityData.get(ATTACKING_STATE);
    }

}