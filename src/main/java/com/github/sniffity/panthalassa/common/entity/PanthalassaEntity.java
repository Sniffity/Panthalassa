package com.github.sniffity.panthalassa.common.entity;

import com.github.sniffity.panthalassa.common.registry.PanthalassaDimension;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.DolphinLookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class PanthalassaEntity extends WaterMobEntity {


    public PanthalassaEntity(EntityType<? extends PanthalassaEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathPriority(PathNodeType.WATER, 3.0F);
        this.lookController = new DolphinLookController(this, 10);
        this.moveController = new PanthalassaEntity.PanthalassaSwimmingHelper(this);
    }

    public PathNavigator createNavigator(World worldIn) {
        return new SwimmerPathNavigator(this, worldIn);
    }

    static class PanthalassaSwimmingHelper extends MovementController {

        private final PanthalassaEntity entityPanthalassa;

        public PanthalassaSwimmingHelper(PanthalassaEntity entity) {
            super(entity);
            this.entityPanthalassa = entity;
        }

        public void tick() {
            if (this.entityPanthalassa.isInWater()) {
                this.entityPanthalassa.setMotion(this.entityPanthalassa.getMotion().add(0.0D, 0.005D, 0.0D));
                System.out.println("First IF statement is true!");
            }

            if (this.action == MovementController.Action.MOVE_TO && !this.entityPanthalassa.getNavigator().noPath()) {
                System.out.println("Second IF statement is true!");
                double d0 = this.posX - this.entityPanthalassa.getPosX();
                double d1 = this.posY - this.entityPanthalassa.getPosY();
                double d2 = this.posZ - this.entityPanthalassa.getPosZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (d3 < (double) 2.5000003E-7F) {
                    this.mob.setMoveForward(0.0F);
                    System.out.println("Third IF statement is true!");
                } else {
                    System.out.println("Third IF statement is false!");
                    float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                    this.entityPanthalassa.rotationYaw = this.limitAngle(this.entityPanthalassa.rotationYaw, f, 10.0F);
                    this.entityPanthalassa.renderYawOffset = this.entityPanthalassa.rotationYaw;
                    this.entityPanthalassa.rotationYawHead = this.entityPanthalassa.rotationYaw;
                    float f1 = (float) (this.speed * this.entityPanthalassa.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    if (this.entityPanthalassa.isInWater()) {
                        System.out.println("Fourth IF statement is true!");
                        this.entityPanthalassa.setAIMoveSpeed(f1 * 0.02F);
                        float f2 = -((float) (MathHelper.atan2(d1, (double) MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double) (180F / (float) Math.PI)));
                        f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                        this.entityPanthalassa.rotationPitch = this.limitAngle(this.entityPanthalassa.rotationPitch, f2, 5.0F);
                        float f3 = MathHelper.cos(this.entityPanthalassa.rotationPitch * ((float) Math.PI / 180F));
                        float f4 = MathHelper.sin(this.entityPanthalassa.rotationPitch * ((float) Math.PI / 180F));
                        this.entityPanthalassa.moveForward = f3 * f1;
                        this.entityPanthalassa.moveVertical = -f4 * f1;
                    } else {
                        System.out.println("Fourth IF statement is false!");
                        this.entityPanthalassa.setAIMoveSpeed(f1 * 0.1F);
                    }

                }
                //IF THIS ELSE STATEMENT NECESSARY?
            } else {
                System.out.println("Second IF statement is false!");
                if (entityPanthalassa.world.getDimensionKey() != PanthalassaDimension.PANTHALASSA) {
                    System.out.println("Fifth IF statement is true!");
                    this.entityPanthalassa.setAIMoveSpeed(0.5F);
                    this.entityPanthalassa.setMoveStrafing(0.0F);
                    this.entityPanthalassa.setMoveVertical(0.0F);
                    this.entityPanthalassa.setMoveForward(0.05F);
                }

                if (entityPanthalassa.world.getDimensionKey() == PanthalassaDimension.PANTHALASSA) {
                    System.out.println("Sixth IF statement is true!");
                    this.entityPanthalassa.setAIMoveSpeed(1.0F);
                    this.entityPanthalassa.setMoveStrafing(0.0F);
                    this.entityPanthalassa.setMoveVertical(0.0F);
                    this.entityPanthalassa.setMoveForward(10.0F);
                    System.out.println("AI Move Speed:");
                    System.out.println( this.entityPanthalassa.getAIMoveSpeed());
                }
//ENTITY MUST HAVE MOVEMENT SPEED AND MOVE FORWARD IN ORDER TO MOVE WHEN IDLE.



            }
        }
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FindWaterGoal(this));
        this.goalSelector.addGoal(3, new LookAtGoal(this, LivingEntity.class, 12.0F));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        }

    public void travel(Vector3d travelVector) {
        if (this.isServerWorld() && this.isInWater()) {
            this.moveRelative(this.getAIMoveSpeed(), travelVector);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.9D));
            if (this.getAttackTarget() == null) {
                this.setMotion(this.getMotion().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }

    }

}
