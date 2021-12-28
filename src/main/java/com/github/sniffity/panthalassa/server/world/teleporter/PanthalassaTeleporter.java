package com.github.sniffity.panthalassa.server.world.teleporter;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.block.BlockPortal;
import com.github.sniffity.panthalassa.server.block.BlockPortalBlockEntity;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
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

    protected final ServerLevel world;

    public PanthalassaTeleporter(ServerLevel worldIn) {
        this.world = worldIn;
    }

    @Nullable
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, BlockPos portalBlockPos) {

        Optional<BlockUtil.FoundRectangle> result = makePortalFromPos(destWorld, entity.level, portalBlockPos);

        if (result.isPresent()) {
            BlockPos startPos = result.get().minCorner;
            if (destWorld.dimension() == PanthalassaDimension.PANTHALASSA) {
                return new PortalInfo(new Vec3(startPos.getX(), startPos.getY(), startPos.getZ()), entity.getDeltaMovement(), entity.yRot, entity.xRot);
            } else {
                destWorld.getChunk(new BlockPos(startPos.getX(), startPos.getY(), startPos.getZ()));
                return new PortalInfo(new Vec3(startPos.getX(), startPos.getY(), startPos.getZ()), entity.getDeltaMovement(), entity.yRot, entity.xRot);
            }
        }
        return null;
    }


    private boolean checkRegionForPlacement(BlockPos potentialPos, Level world) {
        BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos();
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

    public Optional<BlockUtil.FoundRectangle> makePortalFromPos(ServerLevel destWorld, Level originalWorld, @Nonnull BlockPos originalPosition) {
        if (destWorld.dimension() == Level.OVERWORLD) {
            Panthalassa.LOGGER.warn("Corresponding Overworld Portal not found!");
            Panthalassa.LOGGER.warn("Teleporting to spawn");
            BlockPos spawnPoint = new BlockPos(destWorld.getSharedSpawnPos().getX(), destWorld.getSharedSpawnPos().getY(), destWorld.getSharedSpawnPos().getZ());
            return Optional.of(new BlockUtil.FoundRectangle(spawnPoint.immutable(), 1, 1));
        }

        BlockPos destPosition = new BlockPos(originalPosition.getX(), destWorld.getHeight() - 5, originalPosition.getZ());
        destPosition = getValidPortalLocation(destWorld, originalPosition, destPosition);

        BlockPortal.matchShapeSize portalMatch = new BlockPortal.matchShapeSize(destWorld, destPosition, true);

        // Link the two portal's portal blocks together by finding center of original portal
        BlockPortalBlockEntity tempTE = getPortalTE(originalWorld, originalPosition);
        if (tempTE != null) {
            BlockPortalBlockEntity centerTE = getPortalTE(originalWorld, originalPosition.subtract(tempTE.offsetFromCenter));
            if (centerTE != null) {
                portalMatch.linkPortalCenters(originalWorld, centerTE.getBlockPos());
            }
        }

        return Optional.of(new BlockUtil.FoundRectangle(destPosition.immutable(), 15, 1));
    }

    private BlockPos getValidPortalLocation(ServerLevel destWorld, BlockPos originalPosition, BlockPos destPosition) {
        int newSpotOffset = 16;
        int radiusCheckLimit = 500;
        BlockPos.MutableBlockPos currentPosition = new BlockPos.MutableBlockPos().set(destPosition);

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

    private BlockPortalBlockEntity getPortalTE(LevelAccessor world, BlockPos pos) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof BlockPortalBlockEntity) {
            return (BlockPortalBlockEntity) tileEntity;
        } else {
            Panthalassa.LOGGER.error("Panthalassa: Failed to grab portal block entity from entity position for connecting the two portals");
        }
        return null;
    }

}