package com.github.sniffity.panthalassa.server.world;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraft.world.storage.WorldSavedData;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Panthalassa Mod - Class: PanthalassaWorldSavedData <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Atum 2, the Undergarden,
 * UltraAmplifiedDiemsnion and The Twilight Forest mods implement their own respective teleportation systems.
 */

public class PanthalassaWorldSavedData extends WorldSavedData {


    public static final String DATA_KEY = Panthalassa.MODID + ":world_data";
    private List<PlayerTeleportEntry> playerTeleportQueue = new ArrayList<>();
    private List<EntityTeleportEntry> entityTeleportQueue = new ArrayList<>();
    private List<VehicleCompundTeleportEntry> vehicleCompundTeleportQueue = new ArrayList<>();

    public PanthalassaWorldSavedData() {
        super(DATA_KEY);
    }

    public static PanthalassaWorldSavedData get(ServerWorld world) {
        return world.getDataStorage().computeIfAbsent(PanthalassaWorldSavedData::new, DATA_KEY);
    }

    public static void tick(ServerWorld world) {
        MinecraftServer server = world.getServer();
        PanthalassaWorldSavedData data = get(world);

        List<PlayerTeleportEntry> playerList = data.playerTeleportQueue;
        List<EntityTeleportEntry> entityList = data.entityTeleportQueue;
        List<VehicleCompundTeleportEntry> compoundList = data.vehicleCompundTeleportQueue;


        data.playerTeleportQueue = new ArrayList<>();
        data.entityTeleportQueue = new ArrayList<>();
        data.vehicleCompundTeleportQueue = new ArrayList<>();

        for (VehicleCompundTeleportEntry entry : compoundList) {
            Entity vehicle = entry.entity;
            ServerWorld targetWorld = server.getLevel(entry.targetWorld);
            ServerWorld originalWorld = server.getLevel(entry.originalWorld);
            RegistryKey<World> targetWorldKey = entry.targetWorld;
            RegistryKey<World> originalWorldKey = entry.originalWorld;
            Vector3d targetVec = entry.targetVec;
            float yaw = entry.yaw;
            float pitch = entry.pitch;


            Entity vehicleSnapshot = vehicle;

            if (targetWorld != null && vehicle != null) {
                Entity vehicle2 = vehicle.getType().create(targetWorld);
                ChunkPos entityChunkpos = new ChunkPos(vehicle.blockPosition());
                targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, entityChunkpos, 1, vehicle.getId());
                assert vehicle2 != null;
                vehicle2.restoreFrom(vehicle);
                vehicle2.moveTo(new BlockPos(targetVec.x(), targetVec.y(), targetVec.z()), vehicle.yRot, vehicle.xRot);
                vehicle2.setDeltaMovement(vehicle.getDeltaMovement());
                targetWorld.addFromAnotherDimension(vehicle2);

                vehicle.remove();

                targetWorld.getProfiler().pop();
                assert originalWorld != null;
                originalWorld.resetEmptyTime();
                targetWorld.resetEmptyTime();

                for (int i = 0; i < vehicleSnapshot.getPassengers().size(); i++) {
                    Entity passenger = vehicleSnapshot.getPassengers().get(i);
                    if (passenger instanceof PlayerEntity) {
                        ServerPlayerEntity player = (ServerPlayerEntity) passenger;
                        ChunkPos playerChunkPos = new ChunkPos(passenger.blockPosition());
                        targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, playerChunkPos, 1, passenger.getId());

                        player.fallDistance = 0;
                        player.yo = 0;
                        player.teleportTo(
                                targetWorld,
                                entry.targetVec.x(),
                                entry.targetVec.y() + 0.2D,
                                entry.targetVec.z(),
                                entry.yaw,
                                entry.pitch);
                        player.startRiding(vehicle2);

                    } else {
                        Entity passenger2 = passenger.getType().create(targetWorld);
                        ChunkPos entityChunkpos2 = new ChunkPos(passenger.blockPosition());
                        targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, entityChunkpos2, 1, passenger.getId());

                        assert passenger2 != null;
                        passenger2.restoreFrom(passenger);
                        passenger2.moveTo(new BlockPos(targetVec.x(), targetVec.y(), targetVec.z()), passenger.yRot, passenger.xRot);
                        passenger2.setDeltaMovement(passenger.getDeltaMovement());
                        targetWorld.addFromAnotherDimension(passenger2);

                        passenger.remove();

                        targetWorld.getProfiler().pop();
                        originalWorld.resetEmptyTime();
                        targetWorld.resetEmptyTime();
                        passenger2.startRiding(vehicle2);
                    }
                }
            }
        }

        for (PlayerTeleportEntry entry : playerList) {
            ServerPlayerEntity player = server.getPlayerList().getPlayer(entry.playerUUID);
            ServerWorld targetWorld = server.getLevel(entry.targetWorld);
            if (player != null && targetWorld != null) {
                ChunkPos playerChunkPos = new ChunkPos(player.blockPosition());
                targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, playerChunkPos, 1, player.getId());

                    player.fallDistance = 0;
                    player.yo = 0;
                    player.teleportTo(
                            targetWorld,
                            entry.targetVec.x(),
                            entry.targetVec.y() + 0.2D,
                            entry.targetVec.z(),
                            entry.yaw,
                            entry.pitch);
                }
            }

            for (EntityTeleportEntry entry : entityList) {
                Entity entity = entry.entity;
                ServerWorld targetWorld = server.getLevel(entry.targetWorld);
                ServerWorld originalWorld = server.getLevel(entry.originalWorld);
                RegistryKey<World> targetWorldKey = entry.targetWorld;
                RegistryKey<World> originalWorldKey = entry.originalWorld;
                BlockPos targetBlock = entry.targetBlock;

                assert targetWorld != null;
                assert originalWorld != null;

                Entity entity2 = entity.getType().create(targetWorld);

                if (entity2 != null) {
                    ChunkPos entityChunkpos = new ChunkPos(entity.blockPosition());
                    targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, entityChunkpos, 1, entity.getId());

                    entity2.restoreFrom(entity);
                    entity2.moveTo(targetBlock, entity.yRot, entity.xRot);
                    entity2.setDeltaMovement(entity.getDeltaMovement());
                    targetWorld.addFromAnotherDimension(entity2);

                    entity.remove();

                    targetWorld.getProfiler().pop();
                    originalWorld.resetEmptyTime();
                    targetWorld.resetEmptyTime();

                }
            }
        }


        public void addPlayerTP (PlayerEntity player, RegistryKey < World > destination, Vector3d targetVec,float yaw,
        float pitch){
            this.playerTeleportQueue.add(new PlayerTeleportEntry(PlayerEntity.createPlayerUUID(player.getGameProfile()), destination, targetVec, yaw, pitch));
        }


        public void addEntityTP (Entity
        entity, RegistryKey < World > destination, RegistryKey < World > origin, Vector3d targetVec,float yaw,
        float pitch){
            this.entityTeleportQueue.add(new EntityTeleportEntry(entity, destination, origin, targetVec, yaw, pitch));
        }

        public void addCompoundTP (Entity
        compoundEntity, RegistryKey < World > destination, RegistryKey < World > origin, Vector3d targetVec,float yaw,
        float pitch){
            this.vehicleCompundTeleportQueue.add(new VehicleCompundTeleportEntry(compoundEntity, destination, origin, targetVec, yaw, pitch));
        }

        @Override
        public void load (CompoundNBT nbt){
        }

        @Override
        public CompoundNBT save (CompoundNBT compound){
            return null;
        }

        static class PlayerTeleportEntry {
            final UUID playerUUID;
            final RegistryKey<World> targetWorld;
            final Vector3d targetVec;
            final float yaw;
            final float pitch;

            public PlayerTeleportEntry(UUID playerUUID, RegistryKey<World> targetWorld, Vector3d targetVec, float yaw, float pitch) {
                this.playerUUID = playerUUID;
                this.targetWorld = targetWorld;
                this.targetVec = targetVec;
                this.yaw = yaw;
                this.pitch = pitch;
            }
        }


        static class EntityTeleportEntry {
            final Entity entity;
            final RegistryKey<World> targetWorld;
            final RegistryKey<World> originalWorld;
            final BlockPos targetBlock;
            final float yaw;
            final float pitch;
            final Vector3d targetVec;


            public EntityTeleportEntry(Entity entity, RegistryKey<World> targetWorld, RegistryKey<World> originalWorld, Vector3d targetVec, float yaw, float pitch) {
                this.entity = entity;
                this.targetWorld = targetWorld;
                this.originalWorld = originalWorld;
                this.targetBlock = new BlockPos(targetVec.x(), targetVec.y(), targetVec.z());
                this.targetVec = targetVec;
                this.yaw = yaw;
                this.pitch = pitch;
            }
        }


        static class VehicleCompundTeleportEntry {
            final Entity entity;

            final RegistryKey<World> targetWorld;
            final RegistryKey<World> originalWorld;
            final BlockPos targetBlock;
            final Vector3d targetVec;
            final float yaw;
            final float pitch;

            public VehicleCompundTeleportEntry(Entity compoundEntity, RegistryKey<World> targetWorld, RegistryKey<World> originalWorld, Vector3d targetVec, float yaw, float pitch) {
                this.entity = compoundEntity;
                this.targetWorld = targetWorld;
                this.originalWorld = originalWorld;
                this.targetVec = targetVec;
                this.targetBlock = new BlockPos(targetVec.x(), targetVec.y(), targetVec.z());
                this.yaw = yaw;
                this.pitch = pitch;
            }
    }
}