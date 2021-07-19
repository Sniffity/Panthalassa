package com.github.sniffity.panthalassa.server.entity;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.World;

public class KronosaurusEntity extends CreatureEntity {
    public KronosaurusEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }
}