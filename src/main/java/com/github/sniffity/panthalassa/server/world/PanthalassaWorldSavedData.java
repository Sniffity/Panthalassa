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
 * UltraAmplifiedDimension and The Twilight Forest mods implement their own respective teleportation systems.
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
            Vector3d targetVec = entry.targetVec;

            if (targetWorld != null && vehicle != null) {
                Entity vehicle2 = vehicle.getType().create(targetWorld);
                ChunkPos entityChunkpos = new ChunkPos(vehicle.blockPosition());
                targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, entityChunkpos, 1, vehicle.getId());
                originalWorld.getChunkSource().addRegionTicket(TicketType.PORTAL, entityChunkpos, 5, vehicle.blockPosition());


                assert vehicle2 != null;
                vehicle2.restoreFrom(vehicle);
                vehicle2.moveTo(new BlockPos(targetVec.x(), targetVec.y(), targetVec.z()), vehicle.yRot, vehicle.xRot);
                vehicle2.setDeltaMovement(0,0,0);
                targetWorld.addFromAnotherDimension(vehicle2);
                vehicle2.setPortalCooldown();

                vehicle.remove();

                assert originalWorld != null;
                originalWorld.resetEmptyTime();
                targetWorld.resetEmptyTime();

                while (vehicle.getPassengers().size()>0) {
                    Entity passenger = vehicle.getPassengers().get(0);
                    passenger.stopRiding();

                    if (passenger instanceof PlayerEntity) {
                        ServerPlayerEntity player = (ServerPlayerEntity) passenger;
                        ChunkPos playerChunkPos = new ChunkPos(passenger.blockPosition());
                        targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, playerChunkPos, 1, passenger.getId());
                        originalWorld.getChunkSource().addRegionTicket(TicketType.PORTAL, entityChunkpos, 5, vehicle.blockPosition());


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
                        player.setPortalCooldown();
                    } else {
                        Entity passenger2 = passenger.getType().create(targetWorld);
                        ChunkPos entityChunkpos2 = new ChunkPos(passenger.blockPosition());
                        targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, entityChunkpos2, 1, passenger.getId());
                        originalWorld.getChunkSource().addRegionTicket(TicketType.PORTAL, entityChunkpos, 5, vehicle.blockPosition());


                        assert passenger2 != null;
                        passenger2.restoreFrom(passenger);
                        passenger2.moveTo(new BlockPos(targetVec.x(), targetVec.y(), targetVec.z()), passenger.yRot, passenger.xRot);
                        passenger2.setDeltaMovement(0,0,0);
                        targetWorld.addFromAnotherDimension(passenger2);

                        passenger.remove();

                        originalWorld.resetEmptyTime();
                        targetWorld.resetEmptyTime();
                        passenger2.startRiding(vehicle2);
                        passenger2.setPortalCooldown();
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
                    player.setPortalCooldown();
                }
            }

            for (EntityTeleportEntry entry : entityList) {
                Entity entity = entry.entity;
                ServerWorld targetWorld = server.getLevel(entry.targetWorld);
                ServerWorld originalWorld = server.getLevel(entry.originalWorld);
                BlockPos targetBlock = entry.targetBlock;

                assert targetWorld != null;
                assert originalWorld != null;

                Entity entity2 = entity.getType().create(targetWorld);

                if (entity2 != null) {
                    ChunkPos entityChunkpos = new ChunkPos(entity.blockPosition());
                    targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, entityChunkpos, 1, entity.getId());

                    entity2.restoreFrom(entity);
                    entity2.moveTo(targetBlock, entity.yRot, entity.xRot);
                    entity2.setDeltaMovement(0,0,0);
                    entity2.setPortalCooldown();
                    targetWorld.addFromAnotherDimension(entity2);

                    entity.remove();

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