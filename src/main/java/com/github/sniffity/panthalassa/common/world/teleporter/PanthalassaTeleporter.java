package com.github.sniffity.panthalassa.common.world.teleporter;


import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.common.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.common.registry.PanthalassaPOI;
import net.minecraft.block.BlockState;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.state.properties.BlockStateProperties;
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
import java.util.function.Function;

public class PanthalassaTeleporter implements ITeleporter {

    protected final ServerWorld world;

    public PanthalassaTeleporter(ServerWorld worldIn) {
        this.world = worldIn;
    }



    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
        Optional<TeleportationRepositioner.Result> result = teleporterResult(destWorld, entity.getPosition());
        if (result.isPresent()) {
            BlockPos startPos = result.get().startPos;
            return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY(), startPos.getZ()), entity.getMotion(), entity.rotationYaw, entity.rotationPitch);
        } else {
            return new PortalInfo(entity.getPositionVec(), Vector3d.ZERO, entity.rotationYaw, entity.rotationPitch);
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
        int i = 128;
        pointofinterestmanager.ensureLoadedAndValid(serverWorld, pos, i);
        Optional<PointOfInterest> optional = pointofinterestmanager.getInSquare((poiType) -> {
            return poiType == PanthalassaPOI.PANTHALASSA_POI_PORTAL.get();
        }, pos, i, PointOfInterestManager.Status.ANY).sorted(Comparator.<PointOfInterest>comparingDouble((poi) -> {
            return poi.getPos().distanceSq(pos);
        }).thenComparingInt((poi) -> {
            return poi.getPos().getY();
        })).filter((poi) -> {
            return this.world.getBlockState(poi.getPos()).hasProperty(BlockStateProperties.HORIZONTAL_AXIS);
        }).findFirst();
        return optional.map((poi) -> {
            BlockPos blockpos = poi.getPos();
            serverWorld.getChunkProvider().registerTicket(TicketType.PORTAL, new ChunkPos(blockpos), 3, blockpos);
            BlockState blockstate = serverWorld.getBlockState(blockpos);
            return TeleportationRepositioner.findLargestRectangle(blockpos, blockstate.get(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, (posIn) -> {
                return this.world.getBlockState(posIn) == blockstate;
            });
        });
    }

    public Optional<TeleportationRepositioner.Result> makePortalFromPos(ServerWorld world, @Nonnull BlockPos pos) {
        BlockState portalCenter = PanthalassaBlocks.PORTAL.get().getDefaultState();
        BlockState portalFrame = PanthalassaBlocks.PORTAL_FRAME.get().getDefaultState();
        BlockPos pos1 = new BlockPos(pos.getX(),60, pos.getZ());


        if (world.getDimensionKey() == World.OVERWORLD) {
            Panthalassa.LOGGER.error("Corresponding Overworld Portal not found");
            Panthalassa.LOGGER.error("Panthalassa Portal was probably built manually without a corresponding Overworld Portal");
            Panthalassa.LOGGER.error("Teleporting to spawn");
            BlockPos spawnPoint = new BlockPos(world.getSpawnPoint().getX(), world.getSpawnPoint().getY(), world.getSpawnPoint().getZ());
            return Optional.of(new TeleportationRepositioner.Result(spawnPoint.toImmutable(), 1, 1));
        }

        while (pos1.getY() < 128 && world.getFluidState(new BlockPos(pos1)).isTagged(FluidTags.WATER)) {
                pos1 = pos1.up();
        }

        while (pos1.getY()>-1 &&!world.getFluidState(new BlockPos(pos1.down())).isTagged(FluidTags.WATER)) {
            pos1 = pos1.down();
        }

            for (int z = -2; z < 3; z++) {
                world.setBlockState(pos1.add(-7, -1, z), portalFrame, 2);
            }
            for (int z = -2; z < 3; z++) {
                world.setBlockState(pos1.add(7, -1, z), portalFrame, 2);
            }

            for (int z = -4; z < -1; z++) {
                world.setBlockState(pos1.add(-6, -1, z), portalFrame, 2);
            }
            for (int z = 2; z < 5; z++) {
                world.setBlockState(pos1.add(-6, -1, z), portalFrame, 2);
            }
            for (int z = -4; z < -1; z++) {
                world.setBlockState(pos1.add(6, -1, z), portalFrame, 2);
            }
            for (int z = 2; z < 5; z++) {
                world.setBlockState(pos1.add(6, -1, z), portalFrame, 2);
            }

            for (int z = -5; z < -3; z++) {
                world.setBlockState(pos1.add(-5, -1, z), portalFrame, 2);
            }
            for (int z = 4; z < 6; z++) {
                world.setBlockState(pos1.add(-5, -1, z), portalFrame, 2);
            }
            for (int z = -5; z < -3; z++) {
                world.setBlockState(pos1.add(5, -1, z), portalFrame, 2);
            }
            for (int z = 4; z < 6; z++) {
                world.setBlockState(pos1.add(5, -1, z), portalFrame, 2);
            }


            for (int z = -6; z < -4; z++) {
                world.setBlockState(pos1.add(-4, -1, z), portalFrame, 2);
            }
            for (int z = 5; z < 7; z++) {
                world.setBlockState(pos1.add(-4, -1, z), portalFrame, 2);
            }
            for (int z = -6; z < -4; z++) {
                world.setBlockState(pos1.add(4, -1, z), portalFrame, 2);
            }
            for (int z = 5; z < 7; z++) {
                world.setBlockState(pos1.add(4, -1, z), portalFrame, 2);
            }

            world.setBlockState(pos1.add(-3, -1, -6), portalFrame, 2);
            world.setBlockState(pos1.add(-3, -1, 6), portalFrame, 2);
            world.setBlockState(pos1.add(3, -1, -6), portalFrame, 2);
            world.setBlockState(pos1.add(3, -1, 6), portalFrame, 2);

            for (int z = -7; z < -5; z++) {
                world.setBlockState(pos1.add(-2, -1, z), portalFrame, 2);
            }
            for (int z = 6; z < 8; z++) {
                world.setBlockState(pos1.add(-2, -1, z), portalFrame, 2);
            }
            for (int z = -7; z < -5; z++) {
                world.setBlockState(pos1.add(2, -1, z), portalFrame, 2);
            }
            for (int z = 6; z < 8; z++) {
                world.setBlockState(pos1.add(2, -1, z), portalFrame, 2);
            }

            for (int x = -2; x < 3; x++) {
                world.setBlockState(pos1.add(x, -1, -7), portalFrame, 2);
            }

            for (int x = -2; x < 3; x++) {
                world.setBlockState(pos1.add(x, -1, 7), portalFrame, 2);
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



