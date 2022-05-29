package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Objects;
import java.util.Random;

public class FeaturePanthalassaSpawner extends Feature<ConfigurationPanthalassaSpawner> {

    public FeaturePanthalassaSpawner(Codec<ConfigurationPanthalassaSpawner> codec) {
        super(codec);
    }
    //TODO: Could perhaps add IF conditions here...
    //TODO: IF entity = Kronosaurus, do Kronosaurus weights instead of spawn weights...
    //TODO: What about biomes? DataPack?
    public boolean place(FeaturePlaceContext<ConfigurationPanthalassaSpawner> context) {
        ConfigurationPanthalassaSpawner panathalassaSpawnerConfiguration = context.config();
        //Get all the relevant JSON values.
        WorldGenLevel worldGenLevel = context.level();
        EntityType entityType = panathalassaSpawnerConfiguration.type;
        int spawnWeight = panathalassaSpawnerConfiguration.spawnWeight;
        int minGroupSize = panathalassaSpawnerConfiguration.minCount;
        int maxGroupSize = panathalassaSpawnerConfiguration.maxCount;

        //Find a random position for spawning.
        Random rand = context.random();
        BlockPos pos = context.origin();
        int i = rand.nextInt(8) - rand.nextInt(8);
        int j = rand.nextInt(8) - rand.nextInt(8);
        double r = Math.floor(Math.random() * (81) + 20);
        BlockPos blockpos = new BlockPos(pos.getX() + i, r, pos.getZ() + j);

        //We will now begin to spawn the entities...
        //Verify that we are server side...
        if (!worldGenLevel.isClientSide()) {
            if (context.level().isAreaLoaded(blockpos, 0)) {
                //Only carry on with the spawning procedure if the block we have found corresponds to water..
                if (worldGenLevel.getBlockState(blockpos).is(PanthalassaBlocks.PANTHALASSA_WATER.get())) {
                    //For each whole spawning attempt, ensure that we meet the percentage chance.
                    if (spawnWeight > rand.nextDouble() * 100) {
                        //Ensure that we have a valid minimum group size...
                        //Create entities until we have reached the minGroup
                        if (minGroupSize > 0) {
                            for (int k = 0; k < minGroupSize + 1; k++) {
                                //Define the entity...
                                Entity addedEntity = entityType.create(Objects.requireNonNull(Objects.requireNonNull(context.level().getServer()).getLevel(PanthalassaDimension.PANTHALASSA)));
                                if (addedEntity != null) {
                                    //Add it in...
                                    //TODO: will all entities just collide into each other?
                                    context.level().addFreshEntity(addedEntity);
                                    //Reposition it...
                                    addedEntity.moveTo(new BlockPos(blockpos.getX(), blockpos.getY(), blockpos.getZ()), 0, 0);
                                    //Initial movement = 0
                                    addedEntity.setDeltaMovement(0, 0, 0);
                                }
                            }
                        }

                        //We have reached the minGropSize...
                        if (maxGroupSize > minGroupSize) {
                            //Loop until we have reached the maxGroupSize...
                            for (int k = minGroupSize; k < maxGroupSize + 1; k++)
                                //For each attempt at spawning entities between the minGroupSize and the maxGroupSize, give it a 50% chance to spawn
                                if (rand.nextDouble() > 0.5) {
                                    //Define the entity...
                                    //TODO: Add fresh entity
                                    Entity addedEntity = entityType.create(Objects.requireNonNull(Objects.requireNonNull(context.level().getServer()).getLevel(PanthalassaDimension.PANTHALASSA)));
                                    //TODO: will all entities just collide into each other?
                                    if (addedEntity != null) {
                                        //Add it in...
                                        context.level().addFreshEntity(addedEntity);
                                        //Reposition it...
                                        addedEntity.moveTo(new BlockPos(blockpos.getX(), blockpos.getY(), blockpos.getZ()), 0, 0);
                                        //Initial movement = 0
                                        addedEntity.setDeltaMovement(0, 0, 0);
                                    }
                                }

                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
