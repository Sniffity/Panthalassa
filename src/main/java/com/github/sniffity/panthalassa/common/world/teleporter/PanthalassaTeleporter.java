package com.github.sniffity.panthalassa.common.world.teleporter;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.common.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.common.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.common.registry.PanthalassaPOI;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.TeleportationRepositioner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.common.util.ITeleporter;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;

/**
 * Panthalassa Mod - Class: PanthalassaTeleporter <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Atum 2, the Undergarden,
 * UltraAmplifiedDiemsnion and The Twilight Forest mods implement their own respective teleportation systems.
 */

public class PanthalassaTeleporter implements ITeleporter {

    protected final ServerWorld world;

    public PanthalassaTeleporter(ServerWorld worldIn) {
        this.world = worldIn;
    }

    @Nullable
    public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld) {

        Optional<TeleportationRepositioner.Result> result = teleporterResult(destWorld, entity.getPosition());

            BlockPos startPos = result.get().startPos;
            if (result.isPresent()) {
                if (destWorld.getDimensionKey() == PanthalassaDimension.PANTHALASSA) {

                    return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY() - 5, startPos.getZ()), entity.getMotion(), entity.rotationYaw, entity.rotationPitch);

                } else {
                    destWorld.getChunk(new BlockPos(startPos.getX(),startPos.getY(),startPos.getZ()));
                    return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY() + 5, startPos.getZ()-1), entity.getMotion(), entity.rotationYaw, entity.rotationPitch);
                }
            }

            else {

                if (destWorld.getDimensionKey() != PanthalassaDimension.PANTHALASSA) {
                    return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY() - 5, startPos.getZ()), entity.getMotion(), entity.rotationYaw, entity.rotationPitch);
                } else {

                    return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY() + 5, startPos.getZ()-1), entity.getMotion(), entity.rotationYaw, entity.rotationPitch);
                }

            }

    }



    protected Optional<TeleportationRepositioner.Result> teleporterResult(ServerWorld serverWorld, BlockPos pos) {

        Optional<TeleportationRepositioner.Result> optional = getExistingPortal(serverWorld, pos);

        if (optional.isPresent()) {
            return optional;
        } else {
            return makePortalFromPos(serverWorld, pos);
        }
    }


    public Optional<TeleportationRepositioner.Result> getExistingPortal(@Nonnull ServerWorld serverWorld, BlockPos pos) {
        PointOfInterestManager pointofinterestmanager = serverWorld.getPointOfInterestManager();
        int i = 256;
        pointofinterestmanager.ensureLoadedAndValid(serverWorld, pos, i);
        Optional<PointOfInterest> optional = pointofinterestmanager
                .getInSquare(
                        (poiType) -> { return poiType == PanthalassaPOI.PANTHALASSA_POI_PORTAL.get(); },
                        pos,
                        i,
                        PointOfInterestManager.Status.ANY)

                .sorted(
                        Comparator
                                .<PointOfInterest>comparingDouble((poi) -> {return poi.getPos().distanceSq(pos);})
                                .thenComparingInt((poi) -> {return -poi.getPos().getY();}))
                .findFirst();

        return optional.map(
                (poi) -> {
                    BlockPos blockpos = poi.getPos();
                    serverWorld.getChunkProvider().registerTicket(TicketType.PORTAL, new ChunkPos(blockpos), 3, blockpos);
                    BlockState blockstate = serverWorld.getBlockState(blockpos);
                    return TeleportationRepositioner
                            .findLargestRectangle(
                                    blockpos,
                                    Direction.Axis.X,
                                    9,
                                    Direction.Axis.Z,
                                    9,
                                    (posIn) -> this.world.getBlockState(posIn) == blockstate);
        });
    }

    private boolean checkRegionForPlacement(BlockPos potentialPos, World world) {
        BlockPos check;
        int i;
        int j;
        int k;
        for (i = -8; i < 9; ++i) {
            for (j = -1; j > -5; --j) {
                for (k = -8; k < 9; ++k) {
                    check = new BlockPos(potentialPos.getX() + i, potentialPos.getY() + j, potentialPos.getZ() + k);
                    if (!(world.getBlockState(check) == Blocks.WATER.getDefaultState())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Optional<TeleportationRepositioner.Result> makePortalFromPos(ServerWorld world, @Nonnull BlockPos pos) {
        BlockState portalCenter = PanthalassaBlocks.PORTAL.get().getDefaultState();
        BlockState portalFrame = PanthalassaBlocks.PORTAL_FRAME.get().getDefaultState();

        BlockPos pos1 = new BlockPos(pos.getX(),64, pos.getZ());
        if (world.getDimensionKey() == World.OVERWORLD) {
            Panthalassa.LOGGER.warn("Corresponding Overworld Portal not found!");
            Panthalassa.LOGGER.warn("Teleporting to spawn");
            BlockPos spawnPoint = new BlockPos(world.getSpawnPoint().getX(), world.getSpawnPoint().getY(), world.getSpawnPoint().getZ());
            return Optional.of(new TeleportationRepositioner.Result(spawnPoint.toImmutable(), 1, 1));
        }

        boolean validLocation;
        validLocation = false;

        while (!validLocation) {
            while (pos1.getY() < 128
                    && world.getFluidState(new BlockPos(pos1)).isTagged(FluidTags.WATER)
                    || world.getBlockState(pos1) == Blocks.KELP.getDefaultState()
                    || world.getBlockState(pos1) == PanthalassaBlocks.PANTHALASSA_ROCK.get().getDefaultState())  {
                pos1 = pos1.up();
            }
            while (pos1.getY() > -1 && !world.getFluidState(new BlockPos(pos1)).isTagged(FluidTags.WATER)) {
                pos1 = pos1.down();
            }

            if (pos1.getY() < 10 || pos1.getY() > 118 || !checkRegionForPlacement(pos1, world )) {
                pos1 = new BlockPos(pos1.getX()+(int)Math.floor((Math.random())*(20))-20,64,pos1.getZ()+(int)Math.floor((Math.random())*(20))-20);
            } else {
                validLocation=true;
            }
        }


        for (int z = -2; z < 3; z++) {
                world.setBlockState(pos1.add(-7, 0, z), portalFrame, 2);
            }
            for (int z = -2; z < 3; z++) {
                world.setBlockState(pos1.add(7, 0, z), portalFrame, 2);
            }

            for (int z = -4; z < -1; z++) {
                world.setBlockState(pos1.add(-6, 0, z), portalFrame, 2);
            }
            for (int z = 2; z < 5; z++) {
                world.setBlockState(pos1.add(-6, 0, z), portalFrame, 2);
            }
            for (int z = -4; z < -1; z++) {
                world.setBlockState(pos1.add(6, 0, z), portalFrame, 2);
            }
            for (int z = 2; z < 5; z++) {
                world.setBlockState(pos1.add(6, 0, z), portalFrame, 2);
            }

            for (int z = -5; z < -3; z++) {
                world.setBlockState(pos1.add(-5, 0, z), portalFrame, 2);
            }
            for (int z = 4; z < 6; z++) {
                world.setBlockState(pos1.add(-5, 0, z), portalFrame, 2);
            }
            for (int z = -5; z < -3; z++) {
                world.setBlockState(pos1.add(5, 0, z), portalFrame, 2);
            }
            for (int z = 4; z < 6; z++) {
                world.setBlockState(pos1.add(5, 0, z), portalFrame, 2);
            }


            for (int z = -6; z < -4; z++) {
                world.setBlockState(pos1.add(-4, 0, z), portalFrame, 2);
            }
            for (int z = 5; z < 7; z++) {
                world.setBlockState(pos1.add(-4, 0, z), portalFrame, 2);
            }
            for (int z = -6; z < -4; z++) {
                world.setBlockState(pos1.add(4, 0, z), portalFrame, 2);
            }
            for (int z = 5; z < 7; z++) {
                world.setBlockState(pos1.add(4, 0, z), portalFrame, 2);
            }

            world.setBlockState(pos1.add(-3, 0, -6), portalFrame, 2);
            world.setBlockState(pos1.add(-3, 0, 6), portalFrame, 2);
            world.setBlockState(pos1.add(3, 0, -6), portalFrame, 2);
            world.setBlockState(pos1.add(3, 0, 6), portalFrame, 2);

            for (int z = -7; z < -5; z++) {
                world.setBlockState(pos1.add(-2, 0, z), portalFrame, 2);
            }
            for (int z = 6; z < 8; z++) {
                world.setBlockState(pos1.add(-2, 0, z), portalFrame, 2);
            }
            for (int z = -7; z < -5; z++) {
                world.setBlockState(pos1.add(2, 0, z), portalFrame, 2);
            }
            for (int z = 6; z < 8; z++) {
                world.setBlockState(pos1.add(2, 0, z), portalFrame, 2);
            }

            for (int x = -2; x < 3; x++) {
                world.setBlockState(pos1.add(x, 0, -7), portalFrame, 2);
            }

            for (int x = -2; x < 3; x++) {
                world.setBlockState(pos1.add(x, 0, 7), portalFrame, 2);
            }

            for (int x = -4; x < 5; x++) {
                for (int z = -4; z < 5; z++) {
                    world.setBlockState(pos1.add(x, 0, z), portalCenter, 2);
                }

                for (int z = -3; z < 4; z++) {
                        world.setBlockState(pos1.add(-5, 0, z), portalCenter, 2);
                    }

                for (int z = -3; z < 4; z++) {
                        world.setBlockState(pos1.add(5, 0, z), portalCenter, 2);
                    }

                for (int z = -1; z < 2; z++) {
                        world.setBlockState(pos1.add(-6, 0, z), portalCenter, 2);
                    }

                for (int z = -1; z < 2; z++) {
                        world.setBlockState(pos1.add(6, 0, z), portalCenter, 2);
                    }
                }

            for (int x = -3; x < 4; x++) {
                    world.setBlockState(pos1.add(x, 0, -5), portalCenter, 2);
                }

            for (int x = -3; x < 4; x++) {
                    world.setBlockState(pos1.add(x, 0, 5), portalCenter, 2);
                }

            for (int x = -1; x < 2; x++) {
                    world.setBlockState(pos1.add(x, 0, -6), portalCenter, 2);
                }

            for (int x = -1; x < 2; x++) {
                    world.setBlockState(pos1.add(x, 0, 6), portalCenter, 2);
                }
        return Optional.of(new TeleportationRepositioner.Result(pos1.toImmutable(), 15, 1));
    }

}