package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.server.world.teleporter.PanthalassaTeleporter;
import com.github.sniffity.panthalassa.server.world.teleporter.TeleporterLogic;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

/**
 * Panthalassa Mod - Class: BlockPortal <br></br?>
 * <p>
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 * <p>
 * Acknowledgements: The following class was developed after studying how Atum 2, the Undergarden,
 * UltraAmplifiedDimension and The Twilight Forest mods implement their own respective teleportation systems.
 */

public class BlockPortal extends Block implements EntityBlock {

    private static final VoxelShape portalShape = Shapes.box(0.0D, 0.0, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockPortal() {
        super(Properties.of(
                Material.PORTAL,
                MaterialColor.COLOR_CYAN)
                .strength(-1.0F, 3600000.0F)
                .sound(SoundType.GLASS)
                .lightLevel((state) -> 10)
                .randomTicks());
    }

    public static void changeDimension(ServerLevel initialWorld, Entity entity, BlockPos portalBlockPos, PanthalassaTeleporter teleporter) {

        // If portal block is connected to a portal in other dimension already, just teleport right away
        BlockEntity tileEntity = initialWorld.getBlockEntity(portalBlockPos);
        if (tileEntity instanceof BlockPortalBlockEntity) {
            BlockPortalBlockEntity portalTE = (BlockPortalBlockEntity) tileEntity;
            ServerLevel targetWorld = initialWorld.getServer().getLevel(portalTE.destinationWorld);
            if (targetWorld != null && portalTE.destinationPos != null && targetWorld.getBlockState(portalTE.destinationPos).is(PanthalassaBlocks.PORTAL.get())) {
                TeleporterLogic.teleport(entity, targetWorld, initialWorld, portalTE.destinationPos);
                return;
            }
        }

        // Portal block is unlinked. Time to create new portal and link the two together.
        ResourceKey<Level> targetWorldKey = initialWorld.dimension() == PanthalassaDimension.PANTHALASSA ? Level.OVERWORLD : PanthalassaDimension.PANTHALASSA;
        ServerLevel targetWorld = initialWorld.getServer().getLevel(targetWorldKey);

        if (targetWorld != null) {
            TeleporterLogic.teleportAndCreatePortal(entity, portalBlockPos, targetWorld, initialWorld, teleporter);
            entity.setPortalCooldown();
        } else {
            Panthalassa.LOGGER.error("Panthalassa: Portal block is unable to find this dimension for teleporting to: {}", targetWorldKey);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockPortalBlockEntity(blockPos, blockState);
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
        return 5.0F;
    }

    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide() && trySpawnPortal(worldIn, pos)) {
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return portalShape;
    }

    @Override
    @Nonnull
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean canBeReplaced(@Nonnull BlockState state, @Nonnull Fluid fluid) {
        return false;
    }

    public boolean trySpawnPortal(Level world, BlockPos pos) {
        BlockPortal.matchShapeSize check = new BlockPortal.matchShapeSize(world, pos);

        if (check.match) {
            check.createPortalCenter();
            return true;
        } else {
            return false;
        }
    }

    public boolean tryDestoyPortal(Level world, BlockPos pos) {
        BlockPortal.matchShapeSize check = new BlockPortal.matchShapeSize(world, pos);
        if (check.match) {
            check.destroyPortalBlocks();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Block neighborBlock, @Nonnull BlockPos neighborPos, boolean isMoving) {
        BlockPortal.matchShapeSize check = new BlockPortal.matchShapeSize(world, pos);
        if (neighborBlock == this || check.isPanthalassaPortalFrame(neighborBlock.defaultBlockState())) {
            if (!check.match) {
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
    }

    @Override
    public void entityInside(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (world instanceof ServerLevel && !entity.isOnPortalCooldown()) {
            changeDimension((ServerLevel) world, entity, pos, new PanthalassaTeleporter((ServerLevel) world));
        }
    }

    public static class matchShapeSize {
        private final LevelAccessor world;
        public boolean match = true;
        BlockPos centerPosition;
        float minPortalFrameRadius = 6.1f;
        float maxPortalFrameRadius = 7.5f;

        public matchShapeSize(LevelAccessor world, BlockPos pos, boolean createPortal) {
            this.world = world;
            if (createPortal) {
                centerPosition = pos;
                createPortalFrame();
                createPortalCenter();
                match = true;
            } else {
                int offsetN = centerPortal(pos, Direction.NORTH);
                int offsetS = centerPortal(pos, Direction.SOUTH);
                int offsetE = centerPortal(pos, Direction.EAST);
                int offsetW = centerPortal(pos, Direction.WEST);

                centerPosition = new BlockPos(pos.getX() + ((offsetE - offsetW) / 2), pos.getY(), pos.getZ() - ((offsetN - offsetS) / 2));
                if (match) match = checkIfValidPortalFrame(centerPosition);
            }
        }

        public matchShapeSize(LevelAccessor world, BlockPos pos) {
            this(world, pos, false);
        }

        public static void recursivelyFindPortalPositions(LevelAccessor world, BlockPos portalCenter, BlockPos currentOffset, Set<BlockPos> savedOffsets) {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (Direction side : Direction.Plane.HORIZONTAL) {
                // Check if we hit frame or not in world space
                mutable.set(portalCenter).move(currentOffset).move(side);
                if (!world.getBlockState(mutable).is(PanthalassaBlocks.PORTAL_FRAME.get())) {

                    // check if this offset has already been saved or not
                    mutable.set(currentOffset).move(side);
                    if (!savedOffsets.contains(mutable)) {
                        savedOffsets.add(mutable.immutable());
                        recursivelyFindPortalPositions(world, portalCenter, mutable.immutable(), savedOffsets);
                    }
                }
            }
        }

        boolean isPanthalassaPortalFrame(BlockState state) {
            return state == PanthalassaBlocks.PORTAL_FRAME.get().defaultBlockState();
        }

        boolean isWaterOrPortal(BlockState state) {
            return (state.getFluidState().is(FluidTags.WATER) || state == PanthalassaBlocks.PORTAL.get().defaultBlockState());
        }

        int centerPortal(BlockPos pos, Direction direction) {
            int distance;
            for (distance = 1; distance < 16; ++distance) {
                BlockPos blockpos = pos.relative(direction, distance);
                if (isPanthalassaPortalFrame(world.getBlockState(blockpos))) {
                    break;
                }
                if (distance == 15) {
                    distance = 0;
                    blockpos = pos.relative(direction, distance);
                    if (!isPanthalassaPortalFrame(world.getBlockState(blockpos))) {
                        distance = 15;
                        break;
                    }
                    break;
                }
            }
            return distance;
        }

        void destroyPortalBlocks() {
            Set<BlockPos> portalPositionsOffsets = new HashSet<>();
            portalPositionsOffsets.add(BlockPos.ZERO);
            recursivelyFindPortalPositions(this.world, this.centerPosition, BlockPos.ZERO, portalPositionsOffsets);

            for (BlockPos pos : portalPositionsOffsets) {
                this.world.setBlock(centerPosition.offset(pos), Blocks.WATER.defaultBlockState(), (1 << 1));
            }
        }

        public boolean checkIfValidPortalFrame(BlockPos pos) {
            float minRadiusSq = minPortalFrameRadius * minPortalFrameRadius;
            float maxRadiusSq = maxPortalFrameRadius * maxPortalFrameRadius;
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (int x = (int) -maxPortalFrameRadius; x < maxPortalFrameRadius; x++) {
                for (int z = (int) -maxPortalFrameRadius; z < maxPortalFrameRadius; z++) {
                    int distSq = x * x + z * z;
                    if (distSq > minRadiusSq && distSq < maxRadiusSq) {
                        mutable.set(pos).move(x, 0, z);
                        if (!isPanthalassaPortalFrame(world.getBlockState(mutable))) {
                            return false;
                        }
                    } else if (distSq <= minRadiusSq) {
                        mutable.set(pos).move(x, 0, z);
                        if (!isWaterOrPortal(world.getBlockState(mutable))) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        public void createPortalFrame() {
            float minRadiusSq = minPortalFrameRadius * minPortalFrameRadius;
            float maxRadiusSq = maxPortalFrameRadius * maxPortalFrameRadius;
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (int x = (int) -maxPortalFrameRadius; x < maxPortalFrameRadius; x++) {
                for (int z = (int) -maxPortalFrameRadius; z < maxPortalFrameRadius; z++) {
                    int distSq = x * x + z * z;
                    if (distSq > minRadiusSq && distSq < maxRadiusSq) {
                        mutable.set(this.centerPosition).move(x, 0, z);
                        this.world.setBlock(mutable, PanthalassaBlocks.PORTAL_FRAME.get().defaultBlockState(), (1 << 1));

                        // Helps prevent portal frame from sticking out over ledges
                        if (((ServerLevel) this.world).dimension() == PanthalassaDimension.PANTHALASSA) {
                            while (mutable.move(Direction.UP).getY() < this.world.getHeight() && !this.world.getBlockState(mutable).is(Blocks.BEDROCK)) {
                                this.world.setBlock(mutable, PanthalassaBlocks.PANTHALASSA_ROCK.get().defaultBlockState(), (1 << 1));
                            }
                        }
                    }
                }
            }
        }

        public void createPortalCenter() {
            float minRadiusSq = minPortalFrameRadius * minPortalFrameRadius;
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (int x = (int) -minPortalFrameRadius; x < minPortalFrameRadius; x++) {
                for (int z = (int) -minPortalFrameRadius; z < minPortalFrameRadius; z++) {
                    int distSq = x * x + z * z;
                    if (distSq <= minRadiusSq) {
                        mutable.set(this.centerPosition).move(x, 0, z);
                        this.world.setBlock(mutable, PanthalassaBlocks.PORTAL.get().defaultBlockState(), (1 << 1));

                        BlockEntity tileEntity = this.world.getBlockEntity(mutable);
                        if (tileEntity instanceof BlockPortalBlockEntity) {
                            ((BlockPortalBlockEntity) tileEntity).offsetFromCenter = new BlockPos(x, 0, z);
                        }

                        // Helps create some space for mobs to swim into portal
                        if (((ServerLevel) this.world).dimension() == PanthalassaDimension.PANTHALASSA) {
                            while (mutable.move(Direction.UP).getY() < this.world.getHeight() && !this.world.getBlockState(mutable).is(Blocks.BEDROCK) && mutable.getY() < this.centerPosition.getY() + 7) {
                                this.world.setBlock(mutable, Blocks.WATER.defaultBlockState(), (1 << 1));
                            }
                        }
                    }
                }
            }
        }

        public void linkPortalCenters(LevelAccessor otherWorld, BlockPos centerOfOtherPortal) {
            float minRadiusSq = minPortalFrameRadius * minPortalFrameRadius;
            BlockPos.MutableBlockPos mutable1 = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos mutable2 = new BlockPos.MutableBlockPos();
            for (int x = (int) -maxPortalFrameRadius; x < maxPortalFrameRadius; x++) {
                for (int z = (int) -maxPortalFrameRadius; z < maxPortalFrameRadius; z++) {
                    int distSq = x * x + z * z;
                    if (distSq <= minRadiusSq) {
                        mutable1.set(this.centerPosition).move(x, 0, z);
                        mutable2.set(centerOfOtherPortal).move(x, 0, z);

                        BlockEntity tileEntity1 = this.world.getBlockEntity(mutable1);
                        BlockEntity tileEntity2 = otherWorld.getBlockEntity(mutable2);
                        if (tileEntity1 instanceof BlockPortalBlockEntity && tileEntity2 instanceof BlockPortalBlockEntity) {
                            BlockPortalBlockEntity portal1 = ((BlockPortalBlockEntity) tileEntity1);
                            BlockPortalBlockEntity portal2 = ((BlockPortalBlockEntity) tileEntity2);

                            portal1.destinationPos = portal2.getBlockPos();
                            portal1.destinationWorld = portal2.getLevel().dimension();

                            portal2.destinationPos = portal1.getBlockPos();
                            portal2.destinationWorld = portal1.getLevel().dimension();
                        }
                    }
                }
            }
        }
    }
}