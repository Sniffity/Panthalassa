package com.github.sniffity.panthalassa.server.entity.creature;

import com.github.sniffity.panthalassa.server.entity.creature.ai.IBreachable;
import com.github.sniffity.panthalassa.server.entity.creature.ai.PanthalassaSwimmingHelper;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.DolphinLookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;

import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public abstract class PanthalassaEntity extends CreatureEntity {

    public boolean isTryingToBreach;
    public boolean isLandNavigator;

    protected static final DataParameter<Boolean> ATTACKING_STATE = EntityDataManager.defineId(PanthalassaEntity.class, DataSerializers.BOOLEAN);

    public PanthalassaEntity(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.lookControl = new DolphinLookController(this, 10);
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
        if (!this.isLandNavigator && this.isInWater() && this.level.getBlockState(this.blockPosition().below()).canOcclude() && this.level.getBlockState(this.blockPosition().above()).is(Blocks.AIR)) {
                switchToLandNavigator(true);
        }

        else if (this.isLandNavigator && this.isInWater() && !(this.level.getBlockState(this.blockPosition().below()).canOcclude() && this.level.getBlockState(this.blockPosition().above()).is(Blocks.AIR))) {
               switchToLandNavigator(false);
        }
        else if (!this.isInWater() && !this.isLandNavigator) {
            switchToLandNavigator(true);
        }
    }


    public boolean canBreatheUnderwater() {
        return true;
    }

    public void travel(Vector3d travelVector) {
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
                ((PlayerEntity) entityIn).knockback(i * 0.5F, MathHelper.sin(this.yRot * 0.017453292F), (-MathHelper.cos(this.yRot * 0.017453292F)));
            }

            int j = EnchantmentHelper.getFireAspect(this);

            if (j > 0) {
                entityIn.setSecondsOnFire(j * 4);
            }

            if (entityIn instanceof PlayerEntity) {
                PlayerEntity entityplayer = (PlayerEntity) entityIn;
                ItemStack itemstack = this.getMainHandItem();
                ItemStack itemstack1 = entityplayer.isUsingItem() ? entityplayer.getUseItem() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer)) {
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
    public static boolean canPanthalassaEntitySpawn(EntityType<? extends PanthalassaEntity> type, IWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn) {
        if (pos.getY()>20 && pos.getY()<110) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(IWorldReader p_205019_1_) {
        return p_205019_1_.containsAnyLiquid(this.getBoundingBox());
    }

    public void switchToLandNavigator(boolean isOnLand){
        if (isOnLand) {
            this.moveControl = new MovementController(this);
            this.navigation = new GroundPathNavigator(this, level);
            this.isLandNavigator = true;
        } else {
            this.moveControl = new PanthalassaSwimmingHelper(this);
            this.navigation = new SwimmerPathNavigator(this, level);
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

