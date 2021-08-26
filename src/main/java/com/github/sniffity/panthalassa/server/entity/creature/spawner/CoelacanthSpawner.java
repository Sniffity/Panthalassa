package com.github.sniffity.panthalassa.server.entity.creature.spawner;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityCoelacanth;
import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
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

public class CoelacanthSpawner  {

    //The class needs a constructor, so it can be initialized and then called from a static context.
    public CoelacanthSpawner(){
    }

    //This tick method is called on the WorldTick Event. Returns methods being currently used so the method can stop ticking if conditions are not meant.
    public void tick(ServerWorld worldIn) {
        if (worldIn.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            PlayerEntity playerentity = worldIn.getRandomPlayer();
            //Only execute this method in Panthalassa Dimension.
            if (!(playerentity == null || (playerentity.level.dimension() != PanthalassaDimension.PANTHALASSA))) {
                Random random = worldIn.random;
                /*From the player, this is casting out two radii on the XZ plane:
                An inner one of 16 blocks, and an outer one of 80 blocks.
                The entities will spawn somewhere within the outer and the inner radius, if conditions were met.
                Y position is entirely randomized.
                * */
                double i = (Math.floor(Math.random() * (65) + 16) * ((new Random().nextBoolean()) ? -1 : 1));
                double j = (Math.floor(Math.random() * (65) + 16) * ((new Random().nextBoolean()) ? -1 : 1));
                double r = Math.floor(Math.random() * (81) + 20);
                //For Kronosaurus, the idea is to spawn them in packs of 3. Once spawned, if during random movement two schools ever cross over, school mixing can occurr.
                // So, grab a random BlockPos (blockpos)....
                BlockPos blockpos = new BlockPos(playerentity.blockPosition().getX() + i, r, playerentity.blockPosition().getZ() + j);
                //And then create two other random BlockPos close to that first blockpos.
                BlockPos blockpos0 = blockpos.offset((random.nextInt(3) * ((new Random().nextBoolean()) ? -1 : 1)), (random.nextInt(3) * ((new Random().nextBoolean()) ? -1 : 1)), (random.nextInt(3) * ((new Random().nextBoolean()) ? -1 : 1)));
                BlockPos blockpos1 = blockpos.offset((random.nextInt(3) * ((new Random().nextBoolean()) ? -1 : 1)), (random.nextInt(3) * ((new Random().nextBoolean()) ? -1 : 1)), (random.nextInt(3) * ((new Random().nextBoolean()) ? -1 : 1)));

                //Ensure that all the BlockPos that have been selected are different, and are loaded, and have space around them to spawn the entities without suffocation, and there's an actual water block where to spawn the Kronosaurus....
                if (blockpos != blockpos0 && blockpos != blockpos1 && blockpos0 != blockpos1) {
                    if (worldIn.hasChunksAt(blockpos.getX() - 10, blockpos.getY() - 10, blockpos.getZ() - 10, blockpos.getX() + 10, blockpos.getY() + 10, blockpos.getZ() + 10)) {
                        if ((checkValidSpawnArea(worldIn, blockpos)) && (checkValidSpawnArea(worldIn, blockpos0)) && (checkValidSpawnArea(worldIn, blockpos1))) {
                            if (WorldEntitySpawner.isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType.IN_WATER, worldIn, blockpos, PanthalassaEntityTypes.COELACANTH.get())
                                    && WorldEntitySpawner.isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType.IN_WATER, worldIn, blockpos0, PanthalassaEntityTypes.COELACANTH.get())
                                    && WorldEntitySpawner.isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType.IN_WATER, worldIn, blockpos1, PanthalassaEntityTypes.COELACANTH.get())) {
                                //Proceed to do spawnInBiome...
                                this.spawnInBiome(worldIn, blockpos, blockpos0, blockpos1);
                            }
                        }
                    }
                }
            }
        }
    }

    private void spawnInBiome(ServerWorld worldIn, BlockPos blockPos, BlockPos blockPos0, BlockPos blockPos1) {
        //If for some reason the biome is null, do not spawn...
        if (worldIn.getBiome(blockPos).getRegistryName() != null) {
            //If the biome we're in is the Abyssal Overgrowth....
            if (Objects.equals(worldIn.getBiome(blockPos).getRegistryName(), new ResourceLocation(Panthalassa.MODID, "abyssal_overgrowth"))) {
                //And, within a 96x96x96 Bounding Box, there's less than 10 Kronosaurus already spawned... (so basically, a maximum of 3 spawn "groups" of 3 within the 96x96x96 Bounding Box)
                List<EntityCoelacanth> list = worldIn.getEntitiesOfClass(EntityCoelacanth.class, (new AxisAlignedBB(blockPos)).inflate(36.0D, 36.0D, 36.0D));
                //...and within a smaller 24x64x24 bounding box there's no other Kronosaurus (ensures separation of same species - separation will be different for same vs. different species)
                List<EntityCoelacanth> list0 = worldIn.getEntitiesOfClass(EntityCoelacanth.class, (new AxisAlignedBB(blockPos)).inflate(24.0D, 24.0D, 24.0D));
                //...and within a 12x12x12 bounding box there's no other Panthalassa Entities (ensures separation of different species - separation will be different for same vs. different species)
                List<PanthalassaEntity> list1 = worldIn.getEntitiesOfClass(PanthalassaEntity.class, (new AxisAlignedBB(blockPos)).inflate(12.0D, 12.0D, 12.0D));

                if (list.size() < 10 && list0.isEmpty() && list1.isEmpty()) {
                    //Proceed to spawn the Kronosaurus school at the selected BlockPositions
                    this.spawnCoelacanth(blockPos, blockPos0, blockPos1, worldIn);
                }
            }
        }
    }

    private void spawnCoelacanth(BlockPos blockPos, BlockPos blockPos0, BlockPos blockPos1, ServerWorld worldIn) {
        EntityCoelacanth coelacanth = PanthalassaEntityTypes.COELACANTH.get().create(worldIn);
        EntityCoelacanth coelacanth0 = PanthalassaEntityTypes.COELACANTH.get().create(worldIn);
        EntityCoelacanth coelacanth1 = PanthalassaEntityTypes.COELACANTH.get().create(worldIn);

        //Proceed to spawn each Kronosaurus and position them, verifying for possible null values.
        if (coelacanth != null){
            coelacanth.setLeader(true);
            coelacanth.moveTo(blockPos, 0.0F, 0.0F);
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(coelacanth, worldIn, blockPos.getX(), blockPos.getY(), blockPos.getZ(), null, SpawnReason.NATURAL) != -1) {
                coelacanth.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos), SpawnReason.NATURAL, (ILivingEntityData)null, (CompoundNBT)null);
                worldIn.addFreshEntityWithPassengers(coelacanth);
            }
        }

        if (coelacanth0 != null){
            coelacanth0.moveTo(blockPos0, 0.0F, 0.0F);
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(coelacanth0, worldIn, blockPos0.getX(), blockPos0.getY(), blockPos0.getZ(), null, SpawnReason.NATURAL) != -1) {
                coelacanth0.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos), SpawnReason.NATURAL, (ILivingEntityData)null, (CompoundNBT)null);
                worldIn.addFreshEntityWithPassengers(coelacanth0);
            }

        }

        if (coelacanth1 != null) {
            coelacanth1.moveTo(blockPos1, 0.0F, 0.0F);
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(coelacanth1, worldIn, blockPos1.getX(), blockPos1.getY(), blockPos1.getZ(), null, SpawnReason.NATURAL) != -1) {
                coelacanth1.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos1), SpawnReason.NATURAL, (ILivingEntityData)null, (CompoundNBT)null);
                worldIn.addFreshEntityWithPassengers(coelacanth1);
            }
        }
    }

    public boolean checkValidSpawnArea(ServerWorld worldIn, BlockPos blockPos){
        //Analyze a 3x3x3 cube, around the intended spawn position, to ensure the enttiy will not suffocate.
        for (int i = -1; i<2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    if (worldIn.getBlockState(blockPos.offset(i,j,k)).canOcclude()){
                        return false;
                    }

                }
            }
        }
        return true;
    }
}


