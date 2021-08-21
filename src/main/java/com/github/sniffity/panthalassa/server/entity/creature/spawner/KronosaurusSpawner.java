package com.github.sniffity.panthalassa.server.entity.creature.spawner;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.server.registry.PanthalassaEntityTypes;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.List;
import java.util.Objects;
import java.util.Random;

//Class adapted from Vanilla Minecraft's CatSpawner, still needs some tweaking.

public class KronosaurusSpawner  {
    private int nextTick;

    //The class needs a constructor, so it can be initialized and then called from a static context.
    public KronosaurusSpawner(){
    }

    //This tick method is called on the WorldTick Event.
    public int tick(ServerWorld worldIn) {
        if (worldIn.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            --this.nextTick;
            if (this.nextTick > 0) {
                return 0;
            } else {
                this.nextTick = 1200;
                PlayerEntity playerentity = worldIn.getRandomPlayer();
                //Only execute this method in Panthalassa Dimension.
                if (playerentity == null || (playerentity.level.dimension() != PanthalassaDimension.PANTHALASSA)) {
                    return 0;
                } else {
                    Random random = worldIn.random;
                    /*From the player, this is casting out two radii: An inner one of 8 blocks, and an outer one of 32 blocks. The entities will spawn somewhere within
                    the outer and the inner radius
                    * */
                    int i = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    int j = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    double r = Math.floor(Math.random() * (81) + 20);
                    //For Kronosaurus, the idea is to spawn them in packs of 3. So, grab a random BlockPos (blockpos)....
                    BlockPos blockpos = new BlockPos(playerentity.blockPosition().getX() + i, r, playerentity.blockPosition().getX() + j);
                    //And then create two other random BlockPos close to that first blockpos.
                    BlockPos blockpos0 = blockpos.offset((random.nextInt(3) * (random.nextBoolean() ? -1 : 1)), (random.nextInt(3) * (random.nextBoolean() ? -1 : 1)), (random.nextInt(3) * (random.nextBoolean() ? -1 : 1)));
                    BlockPos blockpos1 = blockpos.offset(1, 1, 1);

                    //Ensure that all the BlockPos that have been selected are different
                    if (blockpos != blockpos0 && blockpos != blockpos1 && blockpos0 != blockpos1) {
                        if (!worldIn.hasChunksAt(blockpos.getX() - 10, blockpos.getY() - 10, blockpos.getZ() - 10, blockpos.getX() + 10, blockpos.getY() + 10, blockpos.getZ() + 10)) {
                            return 0;
                        } else {
                            //If all BlockPos are okay to spawn...
                            if (WorldEntitySpawner.isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType.IN_WATER, worldIn, blockpos, PanthalassaEntityTypes.KRONOSAURUS.get())
                            && WorldEntitySpawner.isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType.IN_WATER, worldIn, blockpos0, PanthalassaEntityTypes.KRONOSAURUS.get())
                            && WorldEntitySpawner.isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType.IN_WATER, worldIn, blockpos1, PanthalassaEntityTypes.KRONOSAURUS.get())) {
                                //Proceed to do spawnInBiome...
                                return this.spawnInBiome(worldIn, blockpos, blockpos0, blockpos1);

                            }
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                }
            }
        }
        return 0;
    }


    private int spawnInBiome(ServerWorld p_221121_1_, BlockPos blockPos, BlockPos blockPos0, BlockPos blockPos1) {
        //If for some reason the biome is null, do not spawn...
        if (p_221121_1_.getBiome(blockPos).getRegistryName() == null) {
            return 0;
            //If the biome for the first blockpos is "abyssal_overgrowth"....
        } else if (Objects.equals(p_221121_1_.getBiome(blockPos).getRegistryName(), new ResourceLocation(Panthalassa.MODID, "abyssal_overgrowth"))){
            //And there's less than 7 Kronosaurus already spawned (hence, only 2 packs of 3) in the 96*96*96 Bounding Box
            List<EntityKronosaurus> list = p_221121_1_.getEntitiesOfClass(EntityKronosaurus.class, (new AxisAlignedBB(blockPos)).inflate(96.0D, 96.0D, 96.0D));
            if (list.size() < 7) {
                //Proceed to spawn the Kronosaurus pack at the selected BlockPositions
                return this.spawnKronosaurus(blockPos, blockPos0, blockPos1, p_221121_1_);
            }
        }
        return 0;
    }

    private int spawnKronosaurus(BlockPos blockPos, BlockPos blockPos0, BlockPos blockPos1, ServerWorld worldIn) {
        EntityKronosaurus kronosaurus = PanthalassaEntityTypes.KRONOSAURUS.get().create(worldIn);
        EntityKronosaurus kronosaurus0 = PanthalassaEntityTypes.KRONOSAURUS.get().create(worldIn);
        EntityKronosaurus kronosaurus1 = PanthalassaEntityTypes.KRONOSAURUS.get().create(worldIn);

        
        //Proceed to spawn each Kronosaurus and position them, verifying for possible null values.
        if (kronosaurus != null){
            kronosaurus.moveTo(blockPos, 0.0F, 0.0F);
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(kronosaurus, worldIn, blockPos.getX(), blockPos.getY(), blockPos.getZ(), null, SpawnReason.NATURAL) == -1) return 0;
            kronosaurus.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos), SpawnReason.NATURAL, (ILivingEntityData)null, (CompoundNBT)null);
            worldIn.addFreshEntityWithPassengers(kronosaurus);

        }

        if (kronosaurus0 != null){
            kronosaurus0.moveTo(blockPos0, 0.0F, 0.0F);
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(kronosaurus0, worldIn, blockPos0.getX(), blockPos0.getY(), blockPos0.getZ(), null, SpawnReason.NATURAL) == -1) return 0;
            kronosaurus0.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos), SpawnReason.NATURAL, (ILivingEntityData)null, (CompoundNBT)null);
            worldIn.addFreshEntityWithPassengers(kronosaurus0);

        }

        if (kronosaurus1 != null) {
            kronosaurus1.moveTo(blockPos1, 0.0F, 0.0F);
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(kronosaurus, worldIn, blockPos1.getX(), blockPos1.getY(), blockPos1.getZ(), null, SpawnReason.NATURAL) == -1) return 0;
            kronosaurus1.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos1), SpawnReason.NATURAL, (ILivingEntityData)null, (CompoundNBT)null);
            worldIn.addFreshEntityWithPassengers(kronosaurus1);
        }
        return 1;
    }
}


