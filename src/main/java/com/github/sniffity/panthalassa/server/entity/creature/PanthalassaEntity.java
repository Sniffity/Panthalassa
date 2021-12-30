package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaSwimmingHelper;
import net.minecraft.tags.ItemTags;
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

/**
 * Panthalassa Mod - Class: PanthalassaEntity <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The methods for switching between land and water navigators was taken from Ice and Fire Sea Serpents mobs,
 * with permission from the mod's creator Alexthe666. All credit goes to him.
 */

public abstract class PanthalassaEntity extends PathfinderMob {

    public boolean isTryingToBreach;
    public boolean isLandNavigator;

    protected static final EntityDataAccessor<Boolean> ATTACKING_STATE = SynchedEntityData.defineId(PanthalassaEntity.class, EntityDataSerializers.BOOLEAN);

    public PanthalassaEntity(EntityType<? extends PanthalassaEntity> type, Level worldIn) {
        super(type, worldIn);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.switchToLandNavigator(false);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ATTACKING_STATE, Boolean.FALSE);

        super.defineSynchedData();
    }

    @Override
    public void tick() {
        super.tick();

        boolean ground = !this.isInLava() && !this.isInWater() && this.isOnGround();

        if (!ground && this.isLandNavigator) {
            switchToLandNavigator(false);
        }
        if (ground && !this.isLandNavigator) {
            switchToLandNavigator(true);
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

    @Override
    public boolean checkSpawnObstruction(LevelReader p_205019_1_) {
        return p_205019_1_.containsAnyLiquid(this.getBoundingBox());
    }

    public void switchToLandNavigator(boolean isOnLand){
        if (isOnLand) {
            this.moveControl = new MoveControl(this);
            this.navigation = new GroundPathNavigation(this, level);
            this.isLandNavigator = true;
        } else {
            this.moveControl = new PanthalassaSwimmingHelper(this, 85, 0.02F, 0.1F, true);
            this.navigation = new WaterBoundPathNavigation(this, level);
            this.isLandNavigator = false;
        }
    }

    public void setAttackingState(boolean isAttacking) {
        this.entityData.set(ATTACKING_STATE,isAttacking);
    }

    public boolean getAttackingState() {
        return this.entityData.get(ATTACKING_STATE);
    }

}

