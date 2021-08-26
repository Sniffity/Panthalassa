package com.github.sniffity.panthalassa.server.entity.creature.spawner;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
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


public class MegalodonSpawner  {

    public MegalodonSpawner(){
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
                if (worldIn.hasChunksAt(blockpos.getX() - 10, blockpos.getY() - 10, blockpos.getZ() - 10, blockpos.getX() + 10, blockpos.getY() + 10, blockpos.getZ() + 10)) {
                    if ((checkValidSpawnArea(worldIn, blockpos))) {
                        if (WorldEntitySpawner.isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType.IN_WATER, worldIn, blockpos, PanthalassaEntityTypes.MEGALODON.get()));
                        this.spawnInBiome(worldIn, blockpos);

                    }
                }
            }
        }
    }

    private void spawnInBiome(ServerWorld worldIn, BlockPos blockPos) {
        if (worldIn.getBiome(blockPos).getRegistryName() != null) {
            if (Objects.equals(worldIn.getBiome(blockPos).getRegistryName(), new ResourceLocation(Panthalassa.MODID, "primeval_expanse"))) {
                List<EntityMegalodon> list = worldIn.getEntitiesOfClass(EntityMegalodon.class, (new AxisAlignedBB(blockPos)).inflate(32.0D, 32.0D, 32.0D));
                List<PanthalassaEntity> list1 = worldIn.getEntitiesOfClass(PanthalassaEntity.class, (new AxisAlignedBB(blockPos)).inflate(24.0D, 24.0D, 24.0D));
                if (list.isEmpty() && list1.isEmpty()) {
                    this.spawnMegalodon(blockPos, worldIn);
                }
            }
        }
    }

    private void spawnMegalodon(BlockPos blockPos, ServerWorld worldIn) {
        EntityMegalodon megalodon = PanthalassaEntityTypes.MEGALODON.get().create(worldIn);

        if (megalodon != null){
            megalodon.moveTo(blockPos, 0.0F, 0.0F);
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(megalodon, worldIn, blockPos.getX(), blockPos.getY(), blockPos.getZ(), null, SpawnReason.NATURAL) != -1) {
                megalodon.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos), SpawnReason.NATURAL, (ILivingEntityData)null, (CompoundNBT)null);
                worldIn.addFreshEntityWithPassengers(megalodon);
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