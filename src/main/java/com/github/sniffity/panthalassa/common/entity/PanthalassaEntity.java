package com.github.sniffity.panthalassa.common.entity;

import com.github.sniffity.panthalassa.common.registry.PanthalassaDimension;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.DolphinLookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class PanthalassaEntity extends WaterMobEntity {



    public PanthalassaEntity(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.lookController = new DolphinLookController(this, 10);
        this.moveController = new PanthalassaEntity.PanthalassaSwimmingHelper(this);
    }


    public PathNavigator createNavigator(World worldIn) {
        return new SwimmerPathNavigator(this, worldIn);
    }

    public void travel(Vector3d travelVector) {
        if (this.isServerWorld() && this.isInWater()) {
            this.moveRelative(this.getAIMoveSpeed(), travelVector);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.90));
            if (this.getAttackTarget() == null) {
                this.setMotion(this.getMotion().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVector);

        }

    }


//Proper Attack Behavior
    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        int i = 0;

        if (entityIn instanceof LivingEntity) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((LivingEntity) entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag) {
            if (i > 0) {
                ((PlayerEntity) entityIn).applyKnockback(i * 0.5F, MathHelper.sin(this.rotationYaw * 0.017453292F), (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if(j > 0) {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof PlayerEntity) {
                PlayerEntity entityplayer = (PlayerEntity) entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if(!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer)) {
                    float f1 = 0.25F + EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if(this.rand.nextFloat() < f1) {
                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                        this.world.setEntityState(entityplayer, (byte) 30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }




    static class PanthalassaSwimmingHelper extends MovementController {

        private final PanthalassaEntity entityPanthalassa;

        public PanthalassaSwimmingHelper(PanthalassaEntity entity) {
            super(entity);
            this.entityPanthalassa = entity;
        }

        public void tick() {
            if (this.entityPanthalassa.areEyesInFluid(FluidTags.WATER)) {
                this.entityPanthalassa.setMotion(this.entityPanthalassa.getMotion().add(0.0D, 0.005D, 0.0D));
            }

            if (this.action == MovementController.Action.MOVE_TO && !this.entityPanthalassa.getNavigator().noPath()) {
                double d0 = this.posX - this.entityPanthalassa.getPosX();
                double d1 = this.posY - this.entityPanthalassa.getPosY();
                double d2 = this.posZ - this.entityPanthalassa.getPosZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < (double) 2.5000003E-7F) {
                    this.mob.setMoveForward(0.0F);
                } else {
                    float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                    this.entityPanthalassa.rotationYaw = this.limitAngle(this.entityPanthalassa.rotationYaw, f, 10.0F);
                    this.entityPanthalassa.renderYawOffset = this.entityPanthalassa.rotationYaw;
                    this.entityPanthalassa.rotationYawHead = this.entityPanthalassa.rotationYaw;
                    float f1 = (float) (this.speed * this.entityPanthalassa.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    if (this.entityPanthalassa.isInWater()) {
                        this.entityPanthalassa.setAIMoveSpeed(f1 * 0.02F);
                        float f2 = -((float) (MathHelper.atan2(d1, (double) MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double) (180F / (float) Math.PI)));
                        f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                        this.entityPanthalassa.rotationPitch = this.limitAngle(this.entityPanthalassa.rotationPitch, f2, 5.0F);
                        float f3 = MathHelper.cos(this.entityPanthalassa.rotationPitch * ((float) Math.PI / 180F));
                        float f4 = MathHelper.sin(this.entityPanthalassa.rotationPitch * ((float) Math.PI / 180F));
                        this.entityPanthalassa.moveForward = f3 * f1;
                        this.entityPanthalassa.moveVertical = -f4 * f1;
                    } else {
                        this.entityPanthalassa.setAIMoveSpeed(f1 * 0.1F);
                    }

                }

            } else {

                if (entityPanthalassa.world.getDimensionKey() != PanthalassaDimension.PANTHALASSA) {
                    this.entityPanthalassa.setAIMoveSpeed(0.5F);
                    this.entityPanthalassa.setMoveStrafing(0.0F);
                    this.entityPanthalassa.setMoveVertical(0.05F);
                    this.entityPanthalassa.setMoveForward(0.5F);
                }

                if (entityPanthalassa.world.getDimensionKey() == PanthalassaDimension.PANTHALASSA) {
                    this.entityPanthalassa.setAIMoveSpeed(0.01F);
                    this.entityPanthalassa.setMoveStrafing(0.0F);
                    this.entityPanthalassa.setMoveVertical(0.01F);
                    this.entityPanthalassa.setMoveForward(0.01F);


                }
            }
        }
    }






    protected void registerGoals() {
        super.registerGoals();
        //Custom coded in goals, RandomSwimming (AM), efficientMoveTowardsTarget (BAP), etc.
        this.goalSelector.addGoal(0, new MoveTowardsTargetGoal(this, 1.0, 16.0F));
        this.goalSelector.addGoal(1, new FindWaterGoal(this));
        this.goalSelector.addGoal(3, new LookAtGoal(this, LivingEntity.class, 12.0F));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        }



}
