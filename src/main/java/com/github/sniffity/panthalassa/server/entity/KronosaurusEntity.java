package com.github.sniffity.panthalassa.server.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.w3c.dom.Attr;

public class KronosaurusEntity extends CreatureEntity {
    public KronosaurusEntity(EntityType<? extends CreatureEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute attributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 30.00)
                .add(Attributes.ATTACK_KNOCKBACK, 4.00)
                .add(Attributes.KNOCKBACK_RESISTANCE, 3.00)
                .add(Attributes.FOLLOW_RANGE, 50)
                .add(Attributes.MAX_HEALTH, 25.00)
                .add(Attributes.MOVEMENT_SPEED, 15.00);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 8.0f));
    }
}