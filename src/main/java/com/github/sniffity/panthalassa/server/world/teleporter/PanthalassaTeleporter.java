package com.github.sniffity.panthalassa.server.world.teleporter;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.server.registry.PanthalassaPOI;
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
import java.util.Random;

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

        Optional<TeleportationRepositioner.Result> result = teleporterResult(destWorld, entity.blockPosition());

            if (result.isPresent()) {
                BlockPos startPos = result.get().minCorner;
                if (destWorld.dimension() == PanthalassaDimension.PANTHALASSA) {

                    return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY() - 5, startPos.getZ()), entity.getDeltaMovement(), entity.yRot, entity.xRot);

                } else {
                    destWorld.getChunk(new BlockPos(startPos.getX(),startPos.getY(),startPos.getZ()));
                    return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY() + 5, startPos.getZ()), entity.getDeltaMovement(), entity.yRot, entity.xRot);
                }
            }
            /*
            else {

                if (destWorld.dimension() != PanthalassaDimension.PANTHALASSA) {
                    return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY() - 5, startPos.getZ()), entity.getDeltaMovement(), entity.yRot, entity.xRot);
                } else {

                    return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY() + 5, startPos.getZ()-1), entity.getDeltaMovement(), entity.yRot, entity.xRot);
                }
            }*/
        return null;
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
        PointOfInterestManager pointofinterestmanager = serverWorld.getPoiManager();
        int i = 384;
        pointofinterestmanager.ensureLoadedAndValid(serverWorld, pos, i);
        Optional<PointOfInterest> optional = pointofinterestmanager
                .getInSquare(
                        (poiType) -> { return poiType == PanthalassaPOI.PANTHALASSA_POI_PORTAL.get(); },
                        pos,
                        i,
                        PointOfInterestManager.Status.ANY)

                .sorted(
                        Comparator
                                .<PointOfInterest>comparingDouble((poi) -> {return poi.getPos().distSqr(pos);})
                                .thenComparingInt((poi) -> {return -poi.getPos().getY();}))
                .findFirst();

        return optional.map(
                (poi) -> {
                    BlockPos blockpos = poi.getPos();
                    serverWorld.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(blockpos), 3, blockpos);
                    BlockState blockstate = serverWorld.getBlockState(blockpos);
                    return TeleportationRepositioner
                            .getLargestRectangleAround(
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
                    if (!(world.getBlockState(check) == Blocks.WATER.defaultBlockState())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Optional<TeleportationRepositioner.Result> makePortalFromPos(ServerWorld world, @Nonnull BlockPos pos) {
        BlockState portalCenter = PanthalassaBlocks.PORTAL.get().defaultBlockState();
        BlockState portalFrame = PanthalassaBlocks.PORTAL_FRAME.get().defaultBlockState();

        BlockPos originalPos = new BlockPos(pos.getX(),64, pos.getZ());
        BlockPos pos1 = new BlockPos(pos.getX(),64, pos.getZ());
        if (world.dimension() == World.OVERWORLD) {
            Panthalassa.LOGGER.warn("Corresponding Overworld Portal not found!");
            Panthalassa.LOGGER.warn("Teleporting to spawn");
            BlockPos spawnPoint = new BlockPos(world.getSharedSpawnPos().getX(), world.getSharedSpawnPos().getY(), world.getSharedSpawnPos().getZ());
            return Optional.of(new TeleportationRepositioner.Result(spawnPoint.immutable(), 1, 1));
        }

        boolean validLocation;
        validLocation = false;

        while (!validLocation) {
            while (pos1.getY() < 128
                    && world.getFluidState(new BlockPos(pos1)).is(FluidTags.WATER)
                    || world.getBlockState(pos1) == Blocks.KELP.defaultBlockState()
                    || world.getBlockState(pos1) == PanthalassaBlocks.PANTHALASSA_ROCK.get().defaultBlockState())  {
                pos1 = pos1.above();
            }
            while (pos1.getY() > -1 && !world.getFluidState(new BlockPos(pos1)).is(FluidTags.WATER)) {
                pos1 = pos1.below();
            }

            if (pos1.getY() < 10 || pos1.getY() > 118 || !checkRegionForPlacement(pos1, world )) {

                double adjustedX = pos1.getX()+(int)Math.floor(Math.random()*(50)+15)*((new Random().nextBoolean()) ? -1 : 1);;
                double adjustedZ = pos1.getZ()+(int)Math.floor(Math.random()*(50)+15)*((new Random().nextBoolean()) ? -1 : 1);
                if (Math.abs(originalPos.getX()-adjustedX) > 70) {
                    if (adjustedX>originalPos.getX()){
                        adjustedX = originalPos.getX()+70;
                    } else {
                        adjustedX = originalPos.getX()-70;
                    }
                }
                if (Math.abs(originalPos.getZ()-adjustedZ) > 70) {
                    if (adjustedZ>originalPos.getZ()){
                        adjustedZ = originalPos.getZ()+70;
                    } else {
                        adjustedZ = originalPos.getZ()-70;
                    }
                }
                pos1 = new BlockPos(adjustedX,64,adjustedZ);
            } else {
                validLocation=true;
            }
        }

        for (int z = -2; z < 3; z++) {
                world.setBlock(pos1.offset(-7, 0, z), portalFrame, 2);
            }
            for (int z = -2; z < 3; z++) {
                world.setBlock(pos1.offset(7, 0, z), portalFrame, 2);
            }

            for (int z = -4; z < -1; z++) {
                world.setBlock(pos1.offset(-6, 0, z), portalFrame, 2);
            }
            for (int z = 2; z < 5; z++) {
                world.setBlock(pos1.offset(-6, 0, z), portalFrame, 2);
            }
            for (int z = -4; z < -1; z++) {
                world.setBlock(pos1.offset(6, 0, z), portalFrame, 2);
            }
            for (int z = 2; z < 5; z++) {
                world.setBlock(pos1.offset(6, 0, z), portalFrame, 2);
            }

            for (int z = -5; z < -3; z++) {
                world.setBlock(pos1.offset(-5, 0, z), portalFrame, 2);
            }
            for (int z = 4; z < 6; z++) {
                world.setBlock(pos1.offset(-5, 0, z), portalFrame, 2);
            }
            for (int z = -5; z < -3; z++) {
                world.setBlock(pos1.offset(5, 0, z), portalFrame, 2);
            }
            for (int z = 4; z < 6; z++) {
                world.setBlock(pos1.offset(5, 0, z), portalFrame, 2);
            }


            for (int z = -6; z < -4; z++) {
                world.setBlock(pos1.offset(-4, 0, z), portalFrame, 2);
            }
            for (int z = 5; z < 7; z++) {
                world.setBlock(pos1.offset(-4, 0, z), portalFrame, 2);
            }
            for (int z = -6; z < -4; z++) {
                world.setBlock(pos1.offset(4, 0, z), portalFrame, 2);
            }
            for (int z = 5; z < 7; z++) {
                world.setBlock(pos1.offset(4, 0, z), portalFrame, 2);
            }

            world.setBlock(pos1.offset(-3, 0, -6), portalFrame, 2);
            world.setBlock(pos1.offset(-3, 0, 6), portalFrame, 2);
            world.setBlock(pos1.offset(3, 0, -6), portalFrame, 2);
            world.setBlock(pos1.offset(3, 0, 6), portalFrame, 2);

            for (int z = -7; z < -5; z++) {
                world.setBlock(pos1.offset(-2, 0, z), portalFrame, 2);
            }
            for (int z = 6; z < 8; z++) {
                world.setBlock(pos1.offset(-2, 0, z), portalFrame, 2);
            }
            for (int z = -7; z < -5; z++) {
                world.setBlock(pos1.offset(2, 0, z), portalFrame, 2);
            }
            for (int z = 6; z < 8; z++) {
                world.setBlock(pos1.offset(2, 0, z), portalFrame, 2);
            }

            for (int x = -2; x < 3; x++) {
                world.setBlock(pos1.offset(x, 0, -7), portalFrame, 2);
            }

            for (int x = -2; x < 3; x++) {
                world.setBlock(pos1.offset(x, 0, 7), portalFrame, 2);
            }

            for (int x = -4; x < 5; x++) {
                for (int z = -4; z < 5; z++) {
                    world.setBlock(pos1.offset(x, 0, z), portalCenter, 2);
                }

                for (int z = -3; z < 4; z++) {
                        world.setBlock(pos1.offset(-5, 0, z), portalCenter, 2);
                    }

                for (int z = -3; z < 4; z++) {
                        world.setBlock(pos1.offset(5, 0, z), portalCenter, 2);
                    }

                for (int z = -1; z < 2; z++) {
                        world.setBlock(pos1.offset(-6, 0, z), portalCenter, 2);
                    }

                for (int z = -1; z < 2; z++) {
                        world.setBlock(pos1.offset(6, 0, z), portalCenter, 2);
                    }
                }

            for (int x = -3; x < 4; x++) {
                    world.setBlock(pos1.offset(x, 0, -5), portalCenter, 2);
                }

            for (int x = -3; x < 4; x++) {
                    world.setBlock(pos1.offset(x, 0, 5), portalCenter, 2);
                }

            for (int x = -1; x < 2; x++) {
                    world.setBlock(pos1.offset(x, 0, -6), portalCenter, 2);
                }

            for (int x = -1; x < 2; x++) {
                    world.setBlock(pos1.offset(x, 0, 6), portalCenter, 2);
                }
        return Optional.of(new TeleportationRepositioner.Result(pos1.immutable(), 15, 1));
    }

}