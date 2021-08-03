package com.github.sniffity.panthalassa.common.world;

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
 * Panthalassa Mod - Class: TeleporterLogic <br></br?>
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
        return world.getSavedData().getOrCreate(PanthalassaWorldSavedData::new, DATA_KEY);
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
            ServerWorld targetWorld = server.getWorld(entry.targetWorld);
            ServerWorld originalWorld = server.getWorld(entry.originalWorld);
            RegistryKey<World> targetWorldKey = entry.targetWorld;
            RegistryKey<World> originalWorldKey = entry.originalWorld;
            Vector3d targetVec = entry.targetVec;
            float yaw = entry.yaw;
            float pitch = entry.pitch;


            Entity vehicleSnapshot = vehicle;

            if (targetWorld != null && vehicle != null) {
                Entity vehicle2 = vehicle.getType().create(targetWorld);
                ChunkPos entityChunkpos = new ChunkPos(vehicle.getPosition());
                targetWorld.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, entityChunkpos, 1, vehicle.getEntityId());
                assert vehicle2 != null;
                vehicle2.copyDataFromOld(vehicle);
                vehicle2.moveToBlockPosAndAngles(new BlockPos(targetVec.getX(), targetVec.getY(), targetVec.getZ()), vehicle.rotationYaw, vehicle.rotationPitch);
                vehicle2.setMotion(vehicle.getMotion());
                targetWorld.addFromAnotherDimension(vehicle2);

                vehicle.remove();

                targetWorld.getProfiler().endSection();
                assert originalWorld != null;
                originalWorld.resetUpdateEntityTick();
                targetWorld.resetUpdateEntityTick();

                for (int i = 0; i < vehicleSnapshot.getPassengers().size(); i++) {
                    Entity passenger = vehicleSnapshot.getPassengers().get(i);
                    if (passenger instanceof PlayerEntity) {
                        ServerPlayerEntity player = (ServerPlayerEntity) passenger;
                        ChunkPos playerChunkPos = new ChunkPos(passenger.getPosition());
                        targetWorld.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, playerChunkPos, 1, passenger.getEntityId());

                        player.fallDistance = 0;
                        player.prevPosY = 0;
                        player.teleport(
                                targetWorld,
                                entry.targetVec.getX(),
                                entry.targetVec.getY() + 0.2D,
                                entry.targetVec.getZ(),
                                entry.yaw,
                                entry.pitch);
                        player.startRiding(vehicle2);

                    } else {
                        Entity passenger2 = passenger.getType().create(targetWorld);
                        ChunkPos entityChunkpos2 = new ChunkPos(passenger.getPosition());
                        targetWorld.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, entityChunkpos2, 1, passenger.getEntityId());

                        assert passenger2 != null;
                        passenger2.copyDataFromOld(passenger);
                        passenger2.moveToBlockPosAndAngles(new BlockPos(targetVec.getX(), targetVec.getY(), targetVec.getZ()), passenger.rotationYaw, passenger.rotationPitch);
                        passenger2.setMotion(passenger.getMotion());
                        targetWorld.addFromAnotherDimension(passenger2);

                        passenger.remove();

                        targetWorld.getProfiler().endSection();
                        originalWorld.resetUpdateEntityTick();
                        targetWorld.resetUpdateEntityTick();
                        passenger2.startRiding(vehicle2);
                    }
                }
            }
        }

        for (PlayerTeleportEntry entry : playerList) {
            ServerPlayerEntity player = server.getPlayerList().getPlayerByUUID(entry.playerUUID);
            ServerWorld targetWorld = server.getWorld(entry.targetWorld);
            if (player != null && targetWorld != null) {
                ChunkPos playerChunkPos = new ChunkPos(player.getPosition());
                targetWorld.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, playerChunkPos, 1, player.getEntityId());

                    player.fallDistance = 0;
                    player.prevPosY = 0;
                    player.teleport(
                            targetWorld,
                            entry.targetVec.getX(),
                            entry.targetVec.getY() + 0.2D,
                            entry.targetVec.getZ(),
                            entry.yaw,
                            entry.pitch);
                }
            }

            for (EntityTeleportEntry entry : entityList) {
                Entity entity = entry.entity;
                ServerWorld targetWorld = server.getWorld(entry.targetWorld);
                ServerWorld originalWorld = server.getWorld(entry.originalWorld);
                RegistryKey<World> targetWorldKey = entry.targetWorld;
                RegistryKey<World> originalWorldKey = entry.originalWorld;
                BlockPos targetBlock = entry.targetBlock;

                assert targetWorld != null;
                assert originalWorld != null;

                Entity entity2 = entity.getType().create(targetWorld);

                if (entity2 != null) {
                    ChunkPos entityChunkpos = new ChunkPos(entity.getPosition());
                    targetWorld.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, entityChunkpos, 1, entity.getEntityId());

                    entity2.copyDataFromOld(entity);
                    entity2.moveToBlockPosAndAngles(targetBlock, entity.rotationYaw, entity.rotationPitch);
                    entity2.setMotion(entity.getMotion());
                    targetWorld.addFromAnotherDimension(entity2);

                    entity.remove();

                    targetWorld.getProfiler().endSection();
                    originalWorld.resetUpdateEntityTick();
                    targetWorld.resetUpdateEntityTick();

                }
            }
        }


        public void addPlayerTP (PlayerEntity player, RegistryKey < World > destination, Vector3d targetVec,float yaw,
        float pitch){
            this.playerTeleportQueue.add(new PlayerTeleportEntry(PlayerEntity.getUUID(player.getGameProfile()), destination, targetVec, yaw, pitch));
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
        public void read (CompoundNBT nbt){
        }

        @Override
        public CompoundNBT write (CompoundNBT compound){
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
                this.targetBlock = new BlockPos(targetVec.getX(), targetVec.getY(), targetVec.getZ());
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
                this.targetBlock = new BlockPos(targetVec.getX(), targetVec.getY(), targetVec.getZ());
                this.yaw = yaw;
                this.pitch = pitch;
            }
    }
}