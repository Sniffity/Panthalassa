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
import net.minecraft.world.spawner.ISpecialSpawner;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class KronosaurusSpawner  {
    private int nextTick;

    public KronosaurusSpawner(){
    }


    public int tick(ServerWorld worldIn) {
        if (worldIn.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            --this.nextTick;
            if (this.nextTick > 0) {
                return 0;
            } else {
                this.nextTick = 1200;
                PlayerEntity playerentity = worldIn.getRandomPlayer();
                if (playerentity == null || (playerentity.level.dimension() != PanthalassaDimension.PANTHALASSA)) {
                    return 0;
                } else {
                    Random random = worldIn.random;
                    int i = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    int j = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    double r = Math.floor(Math.random()*(81)+20);
                    BlockPos blockpos = new BlockPos(playerentity.blockPosition().getX()+i, r, playerentity.blockPosition().getX()+j);
                    if (!worldIn.hasChunksAt(blockpos.getX() - 10, blockpos.getY() - 10, blockpos.getZ() - 10, blockpos.getX() + 10, blockpos.getY() + 10, blockpos.getZ() + 10)) {
                        return 0;
                    } else {
                        if (WorldEntitySpawner.isSpawnPositionOk(EntitySpawnPlacementRegistry.PlacementType.IN_WATER, worldIn, blockpos, PanthalassaEntityTypes.KRONOSAURUS.get())) {
                            return this.spawnInPrimevalExpanse(worldIn, blockpos);

                        }
                        return 0;
                    }
                }
            }
        } else {
            return 0;
        }
    }

    private int spawnInPrimevalExpanse(ServerWorld p_221121_1_, BlockPos p_221121_2_) {
        System.out.println((new ResourceLocation(Panthalassa.MODID, "abyssal_overgrowth")));
        System.out.println(p_221121_1_.getBiome(p_221121_2_).getRegistryName());
        if (p_221121_1_.getBiome(p_221121_2_).getRegistryName() == null) {
            return 0;
        } else if (Objects.equals(p_221121_1_.getBiome(p_221121_2_).getRegistryName(), new ResourceLocation(Panthalassa.MODID, "primeval_expanse"))){
            List<EntityKronosaurus> list = p_221121_1_.getEntitiesOfClass(EntityKronosaurus.class, (new AxisAlignedBB(p_221121_2_)).inflate(48.0D, 48.0D, 48.0D));
            if (list.size() < 5) {
                    return this.spawnKronosaurus(p_221121_2_, p_221121_1_);
            }
        }
        return 0;
    }

    private int spawnKronosaurus(BlockPos p_221122_1_, ServerWorld p_221122_2_) {
        EntityKronosaurus kronosaurus = PanthalassaEntityTypes.KRONOSAURUS.get().create(p_221122_2_);
        if (kronosaurus == null) {
            return 0;
        } else {
            kronosaurus.moveTo(p_221122_1_, 0.0F, 0.0F);
            if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(kronosaurus, p_221122_2_, p_221122_1_.getX(), p_221122_1_.getY(), p_221122_1_.getZ(), null, SpawnReason.NATURAL) == -1) return 0;
            kronosaurus.finalizeSpawn(p_221122_2_, p_221122_2_.getCurrentDifficultyAt(p_221122_1_), SpawnReason.NATURAL, (ILivingEntityData)null, (CompoundNBT)null);
            p_221122_2_.addFreshEntityWithPassengers(kronosaurus);
            return 1;
        }
    }
}
