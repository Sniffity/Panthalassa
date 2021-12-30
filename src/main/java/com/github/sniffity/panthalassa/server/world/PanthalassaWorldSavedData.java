package com.github.sniffity.panthalassa.server.world;

import com.github.sniffity.panthalassa.Panthalassa;
import com.ibm.icu.impl.Pair;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.saveddata.SavedData;
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

public class PanthalassaWorldSavedData extends SavedData {

    public static final String DATA_KEY = Panthalassa.MODID + ":world_data";
    private static final PanthalassaWorldSavedData CLIENT_DUMMY = new PanthalassaWorldSavedData();

    public PanthalassaWorldSavedData() {
    }

    public static PanthalassaWorldSavedData create() {
        return new PanthalassaWorldSavedData();
    }

    private List<PlayerTeleportEntry> playerTeleportQueue = new ArrayList<>();
    private List<EntityTeleportEntry> entityTeleportQueue = new ArrayList<>();
    private List<VehicleCompundTeleportEntry> vehicleCompundTeleportQueue = new ArrayList<>();


    public static PanthalassaWorldSavedData get(Level world) {
        if (!(world instanceof ServerLevel)) {
            return CLIENT_DUMMY;
        }

        DimensionDataStorage storage = ((ServerLevel)world).getDataStorage();
        return storage.computeIfAbsent(PanthalassaWorldSavedData::load, PanthalassaWorldSavedData::new, DATA_KEY);
    }



    public static void tick(ServerLevel world) {

        MinecraftServer server = world.getServer();
        PanthalassaWorldSavedData data = get(world);

        if (data == null) {
            return;
        }

        List<PlayerTeleportEntry> playerList = data.playerTeleportQueue;
        List<EntityTeleportEntry> entityList = data.entityTeleportQueue;
        List<VehicleCompundTeleportEntry> compoundList = data.vehicleCompundTeleportQueue;


        data.playerTeleportQueue = new ArrayList<>();
        data.entityTeleportQueue = new ArrayList<>();
        data.vehicleCompundTeleportQueue = new ArrayList<>();

        for (VehicleCompundTeleportEntry entry : compoundList) {
            Entity vehicle = entry.entity;
            ServerLevel targetWorld = server.getLevel(entry.targetWorld);
            ServerLevel originalWorld = server.getLevel(entry.originalWorld);
            Vec3 targetVec = entry.targetVec;

            if (targetWorld != null && vehicle != null) {
                Entity vehicle2 = vehicle.getType().create(targetWorld);
                ChunkPos entityChunkpos = new ChunkPos(vehicle.blockPosition());
                targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, entityChunkpos, 1, vehicle.getId());
                originalWorld.getChunkSource().addRegionTicket(TicketType.PORTAL, entityChunkpos, 5, vehicle.blockPosition());


                assert vehicle2 != null;
                vehicle2.restoreFrom(vehicle);
                vehicle2.moveTo(new BlockPos(targetVec.x(), targetVec.y(), targetVec.z()), vehicle.yRot, vehicle.xRot);
                vehicle2.setDeltaMovement(0, 0, 0);
                targetWorld.addDuringTeleport(vehicle2);
                vehicle2.setPortalCooldown();

                vehicle.discard();

                assert originalWorld != null;
                originalWorld.resetEmptyTime();
                targetWorld.resetEmptyTime();

                while (vehicle.getPassengers().size() > 0) {
                    Entity passenger = vehicle.getPassengers().get(0);
                    passenger.stopRiding();

                    if (passenger instanceof Player) {
                        ServerPlayer player = (ServerPlayer) passenger;
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
                        passenger2.setDeltaMovement(0, 0, 0);
                        targetWorld.addDuringTeleport(passenger2);

                        passenger.discard();

                        originalWorld.resetEmptyTime();
                        targetWorld.resetEmptyTime();
                        passenger2.startRiding(vehicle2);
                        passenger2.setPortalCooldown();
                    }
                }
            }
        }

        for (PlayerTeleportEntry entry : playerList) {
            ServerPlayer player = server.getPlayerList().getPlayer(entry.playerUUID);
            ServerLevel targetWorld = server.getLevel(entry.targetWorld);
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
            ServerLevel targetWorld = server.getLevel(entry.targetWorld);
            ServerLevel originalWorld = server.getLevel(entry.originalWorld);
            BlockPos targetBlock = entry.targetBlock;

            assert targetWorld != null;
            assert originalWorld != null;

            Entity entity2 = entity.getType().create(targetWorld);

            if (entity2 != null) {
                ChunkPos entityChunkpos = new ChunkPos(entity.blockPosition());
                targetWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, entityChunkpos, 1, entity.getId());

                entity2.restoreFrom(entity);
                entity2.moveTo(targetBlock, entity.yRot, entity.xRot);
                entity2.setDeltaMovement(0, 0, 0);
                entity2.setPortalCooldown();
                targetWorld.addDuringTeleport(entity2);

                entity.discard();

                originalWorld.resetEmptyTime();
                targetWorld.resetEmptyTime();
            }
        }
    }


    public void addPlayerTP(Player player, ResourceKey<Level> destination, Vec3 targetVec, float yaw,
                            float pitch) {
        this.playerTeleportQueue.add(new PlayerTeleportEntry(Player.createPlayerUUID(player.getGameProfile()), destination, targetVec, yaw, pitch));
    }


    public void addEntityTP(Entity entity, ResourceKey<Level> destination, ResourceKey<Level> origin, Vec3 targetVec, float yaw, float pitch) {
        this.entityTeleportQueue.add(new EntityTeleportEntry(entity, destination, origin, targetVec, yaw, pitch));
    }

    public void addCompoundTP(Entity compoundEntity, ResourceKey<Level> destination, ResourceKey<Level> origin, Vec3 targetVec, float yaw, float pitch) {
        this.vehicleCompundTeleportQueue.add(new VehicleCompundTeleportEntry(compoundEntity, destination, origin, targetVec, yaw, pitch));
    }

    @Override
    public CompoundTag save (CompoundTag compound){
        return null;
    }

    public static PanthalassaWorldSavedData load(CompoundTag tag) {
        PanthalassaWorldSavedData data = create();
        return data;
    }


    static class PlayerTeleportEntry {
        final UUID playerUUID;
        final ResourceKey<Level> targetWorld;
        final Vec3 targetVec;
        final float yaw;
        final float pitch;

        public PlayerTeleportEntry(UUID playerUUID, ResourceKey<Level> targetWorld, Vec3 targetVec, float yaw, float pitch) {
            this.playerUUID = playerUUID;
            this.targetWorld = targetWorld;
            this.targetVec = targetVec;
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }

    static class EntityTeleportEntry {
        final Entity entity;
        final ResourceKey<Level> targetWorld;
        final ResourceKey<Level> originalWorld;
        final BlockPos targetBlock;
        final float yaw;
        final float pitch;
        final Vec3 targetVec;


        public EntityTeleportEntry(Entity entity, ResourceKey<Level> targetWorld, ResourceKey<Level> originalWorld, Vec3 targetVec, float yaw, float pitch) {
            this.entity = entity;
            this.targetWorld = targetWorld;
            this.originalWorld = originalWorld;
            this.targetBlock = new BlockPos(targetVec.x(), targetVec.y(), targetVec.z());
            this.targetVec = targetVec;this.yaw = yaw;
            this.pitch = pitch;
        }
    }

    static class VehicleCompundTeleportEntry {
        final Entity entity;
        final ResourceKey<Level> targetWorld;
        final ResourceKey<Level> originalWorld;
        final BlockPos targetBlock;
        final Vec3 targetVec;
        final float yaw;
        final float pitch;
        public VehicleCompundTeleportEntry(Entity compoundEntity, ResourceKey<Level> targetWorld, ResourceKey<Level> originalWorld, Vec3 targetVec, float yaw, float pitch) {
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