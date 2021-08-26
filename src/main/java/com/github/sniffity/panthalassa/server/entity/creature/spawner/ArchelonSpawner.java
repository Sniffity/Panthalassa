package com.github.sniffity.panthalassa.server.entity.creature.spawner;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityArchelon;
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


public class ArchelonSpawner  {

    public ArchelonSpawner(){
    }

    public void tick(ServerWorld worldIn) {
        if (worldIn.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            PlayerEntity playerentity = worldIn.getRandomPlayer();
            if (!(playerentity == null || (playerentity.level.dimension() != PanthalassaDimension.PANTHALASSA))) {
                Random random = worldIn.random;

                double i = (Math.floor(Math.random() * (55) + 8) * ((new Random().nextBoolean()) ? -1 : 1));
                double j = (Math.floor(Math.random() * (55) + 8) * ((new Random().nextBoolean()) ? -1 : 1));
                double r = Math.floor(Math.random() * (81) + 20);
                BlockPos blockpos = new BlockPos(playerentity.blockPosition().getX() + i, r, playerentity.blockPosition().getZ() + j);
                BlockPos blockpos0 = blockpos.offset((Math.floor(Math.random() * (4) + 2)  * ((new Random().nextBoolean()) ? -1 : 1)), (Math.floor(Math.random() * (4) + 2)  * ((new Random().nextBoolean()) ? -1 : 1)), (random.nextInt(3) * ((new Random().nextBoolean()) ? -1 : 1)));

                if (blockpos != blockpos0) {
                    if (worldIn.hasChunksAt(blockpos.getX() - 10, blockpos.getY() - 10, blockpos.getZ() - 10, blockpos.getX() + 10, blockpos.getY() + 10, blockpos.getZ() + 10)) {
                        if ((checkValidSpawnArea(worldIn, blockpos)) && (checkValidSpawnArea(worldIn, blockpos0))) {
                            if (WorldEntitySpawner.isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType.IN_WATER, worldIn, blockpos, PanthalassaEntityTypes.ARCHELON.get())
                                    && WorldEntitySpawner.isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType.IN_WATER, worldIn, blockpos0, PanthalassaEntityTypes.ARCHELON.get()))
                                this.spawnInBiome(worldIn, blockpos, blockpos0);
                        }
                    }
                }
            }
        }
    }

    private void spawnInBiome(ServerWorld worldIn, BlockPos blockPos, BlockPos blockPos0) {
        if (worldIn.getBiome(blockPos).getRegistryName() != null) {
            if (Objects.equals(worldIn.getBiome(blockPos).getRegistryName(), new ResourceLocation(Panthalassa.MODID, "ancient_caverns"))) {
                List<EntityArchelon> list = worldIn.getEntitiesOfClass(EntityArchelon.class, (new AxisAlignedBB(blockPos)).inflate(36.0D, 36.0D, 36.0D));
                List<EntityArchelon> list0 = worldIn.getEntitiesOfClass(EntityArchelon.class, (new AxisAlignedBB(blockPos)).inflate(24.0D, 24.0D, 24.0D));
                List<PanthalassaEntity> list1 = worldIn.getEntitiesOfClass(PanthalassaEntity.class, (new AxisAlignedBB(blockPos)).inflate(12.0D, 12.0D, 12.0D));
                if (list.size() < 9 && list0.isEmpty() && list1.isEmpty()) {
                    this.spawnArchelon(blockPos, blockPos0, worldIn);
                }
            }
        }
    }

    private void spawnArchelon(BlockPos blockPos, BlockPos blockPos0, ServerWorld worldIn) {
        EntityArchelon archelon = PanthalassaEntityTypes.ARCHELON.get().create(worldIn);
        EntityArchelon archelon0 = PanthalassaEntityTypes.ARCHELON.get().create(worldIn);

        if (archelon != null){
            archelon.moveTo(blockPos, 0.0F, 0.0F);
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(archelon, worldIn, blockPos.getX(), blockPos.getY(), blockPos.getZ(), null, SpawnReason.NATURAL) != -1) {
                archelon.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos), SpawnReason.NATURAL, (ILivingEntityData)null, (CompoundNBT)null);
                worldIn.addFreshEntityWithPassengers(archelon);
            }
        }

        if (archelon0 != null){
            archelon0.moveTo(blockPos0, 0.0F, 0.0F);
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(archelon0, worldIn, blockPos0.getX(), blockPos0.getY(), blockPos0.getZ(), null, SpawnReason.NATURAL) != -1) {
                archelon0.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos), SpawnReason.NATURAL, (ILivingEntityData)null, (CompoundNBT)null);
                worldIn.addFreshEntityWithPassengers(archelon0);
            }

        }

    }

    public boolean checkValidSpawnArea(ServerWorld worldIn, BlockPos blockPos){
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