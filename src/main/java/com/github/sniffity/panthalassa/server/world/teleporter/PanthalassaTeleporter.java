package com.github.sniffity.panthalassa.server.world.teleporter;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.block.BlockPortal;
import com.github.sniffity.panthalassa.server.block.BlockPortalTileEntity;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.TeleportationRepositioner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Panthalassa Mod - Class: PanthalassaTeleporter <br></br?>
 * <p>
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 * <p>
 * Acknowledgements: The following class was developed after studying how Atum 2, the Undergarden,
 * UltraAmplifiedDimension and The Twilight Forest mods implement their own respective teleportation systems.
 */

public class PanthalassaTeleporter implements ITeleporter {

    protected final ServerWorld world;

    public PanthalassaTeleporter(ServerWorld worldIn) {
        this.world = worldIn;
    }

    @Nullable
    public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, BlockPos portalBlockPos) {

        Optional<TeleportationRepositioner.Result> result = makePortalFromPos(destWorld, entity.level, portalBlockPos);

        if (result.isPresent()) {
            BlockPos startPos = result.get().minCorner;
            if (destWorld.dimension() == PanthalassaDimension.PANTHALASSA) {
                return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY(), startPos.getZ()), entity.getDeltaMovement(), entity.yRot, entity.xRot);
            } else {
                destWorld.getChunk(new BlockPos(startPos.getX(), startPos.getY(), startPos.getZ()));
                return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY(), startPos.getZ()), entity.getDeltaMovement(), entity.yRot, entity.xRot);
            }
        }
        return null;
    }


    private boolean checkRegionForPlacement(BlockPos potentialPos, World world) {
        BlockPos.Mutable check = new BlockPos.Mutable();
        for (int x = -8; x < 9; ++x) {
            for (int y = -1; y > -5; --y) {
                for (int z = -8; z < 9; ++z) {
                    check.set(potentialPos.getX() + x, potentialPos.getY() + y, potentialPos.getZ() + z);
                    if (!world.getFluidState(check).is(FluidTags.WATER)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Optional<TeleportationRepositioner.Result> makePortalFromPos(ServerWorld destWorld, World originalWorld, @Nonnull BlockPos originalPosition) {
        if (destWorld.dimension() == World.OVERWORLD) {
            Panthalassa.LOGGER.warn("Corresponding Overworld Portal not found!");
            Panthalassa.LOGGER.warn("Teleporting to spawn");
            BlockPos spawnPoint = new BlockPos(destWorld.getSharedSpawnPos().getX(), destWorld.getSharedSpawnPos().getY(), destWorld.getSharedSpawnPos().getZ());
            return Optional.of(new TeleportationRepositioner.Result(spawnPoint.immutable(), 1, 1));
        }

        BlockPos destPosition = new BlockPos(originalPosition.getX(), destWorld.getHeight() - 5, originalPosition.getZ());
        destPosition = getValidPortalLocation(destWorld, originalPosition, destPosition);

        BlockPortal.matchShapeSize portalMatch = new BlockPortal.matchShapeSize(destWorld, destPosition, true);

        // Link the two portal's portal blocks together by finding center of original portal
        BlockPortalTileEntity tempTE = getPortalTE(originalWorld, originalPosition);
        if (tempTE != null) {
            BlockPortalTileEntity centerTE = getPortalTE(originalWorld, originalPosition.subtract(tempTE.offsetFromCenter));
            if (centerTE != null) {
                portalMatch.linkPortalCenters(originalWorld, centerTE.getBlockPos());
            }
        }

        return Optional.of(new TeleportationRepositioner.Result(destPosition.immutable(), 15, 1));
    }

    private BlockPos getValidPortalLocation(ServerWorld destWorld, BlockPos originalPosition, BlockPos destPosition) {
        int newSpotOffset = 16;
        int radiusCheckLimit = 500;
        BlockPos.Mutable currentPosition = new BlockPos.Mutable().set(destPosition);

        for (int currentRadius = 0; currentRadius <= radiusCheckLimit; currentRadius += newSpotOffset) {
            for (int x = -currentRadius; x <= currentRadius; x += newSpotOffset) {
                for (int z = -currentRadius; z <= currentRadius; z += newSpotOffset) {
                    if (Math.abs(x) != currentRadius) continue; // Skip area in center we already checked

                    // move to a new location
                    currentPosition.set(destPosition).move(x, 0, z);

                    // Move down until it hits a ceiling with enough valid space around
                    while (currentPosition.getY() > -1) {
                        currentPosition.move(Direction.DOWN);
                        if (destWorld.getFluidState(currentPosition).is(FluidTags.WATER) && checkRegionForPlacement(currentPosition, destWorld)) {
                            return currentPosition.immutable();
                        }
                    }

                    // no valid spot found in this column. Move outward to a new spot.
                }
            }
        }

        Panthalassa.LOGGER.error("Panthalassa: Unable to find valid portal location after searching {} radius. Force spawning portal regardless of conditions now.", radiusCheckLimit);
        return new BlockPos(originalPosition.getX(), 64, originalPosition.getZ());
    }

    private BlockPortalTileEntity getPortalTE(IWorld world, BlockPos pos) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof BlockPortalTileEntity) {
            return (BlockPortalTileEntity) tileEntity;
        } else {
            Panthalassa.LOGGER.error("Panthalassa: Failed to grab portal block entity from entity position for connecting the two portals");
        }
        return null;
    }

}