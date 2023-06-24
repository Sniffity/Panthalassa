package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.server.world.teleporter.PanthalassaTeleporter;
import com.github.sniffity.panthalassa.server.world.teleporter.TeleporterLogic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

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

public class BlockPortal extends Block implements ITileEntityProvider {

    private static final VoxelShape portalShape = VoxelShapes.box(0.0D, 0.0, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockPortal() {
        super(Properties.of(
                Material.PORTAL,
                MaterialColor.COLOR_CYAN)
                .strength(-1.0F, 3600000.0F)
                .sound(SoundType.GLASS)
                .lightLevel((state) -> 10)
                .randomTicks());
    }

    public static void changeDimension(ServerWorld initialWorld, Entity entity, BlockPos portalBlockPos, PanthalassaTeleporter teleporter) {

        // If portal block is connected to a portal in other dimension already, just teleport right away
        TileEntity tileEntity = initialWorld.getBlockEntity(portalBlockPos);
        if (tileEntity instanceof BlockPortalTileEntity) {
            BlockPortalTileEntity portalTE = (BlockPortalTileEntity) tileEntity;
            ServerWorld targetWorld = initialWorld.getServer().getLevel(portalTE.destinationWorld);
            if (targetWorld != null && portalTE.destinationPos != null && targetWorld.getBlockState(portalTE.destinationPos).is(PanthalassaBlocks.PORTAL.get())) {
                TeleporterLogic.teleport(entity, targetWorld, initialWorld, portalTE.destinationPos);
                return;
            }
        }

        // Portal block is unlinked. Time to create new portal and link the two together.
        RegistryKey<World> targetWorldKey = initialWorld.dimension() == PanthalassaDimension.PANTHALASSA ? World.OVERWORLD : PanthalassaDimension.PANTHALASSA;
        ServerWorld targetWorld = initialWorld.getServer().getLevel(targetWorldKey);

        if (targetWorld != null) {
            TeleporterLogic.teleportAndCreatePortal(entity, portalBlockPos, targetWorld, initialWorld, teleporter);
            entity.setPortalCooldown();
        } else {
            Panthalassa.LOGGER.error("Panthalassa: Portal block is unable to find this dimension for teleporting to: {}", targetWorldKey);
        }
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader blockReader) {
        return new BlockPortalTileEntity();
    }

    @Override
    public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
        return 5.0F;
    }

    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide() && trySpawnPortal(worldIn, pos)) {
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.FAIL;
        }
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return portalShape;
    }

    @Override
    @Nonnull
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean canBeReplaced(@Nonnull BlockState state, @Nonnull Fluid fluid) {
        return false;
    }

    public boolean trySpawnPortal(World world, BlockPos pos) {
        BlockPortal.matchShapeSize check = new BlockPortal.matchShapeSize(world, pos);

        if (check.match) {
            check.createPortalCenter();
            return true;
        } else {
            return false;
        }
    }

    public boolean tryDestoyPortal(World world, BlockPos pos) {
        BlockPortal.matchShapeSize check = new BlockPortal.matchShapeSize(world, pos);
        if (check.match) {
            check.destroyPortalBlocks();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block neighborBlock, @Nonnull BlockPos neighborPos, boolean isMoving) {
        BlockPortal.matchShapeSize check = new BlockPortal.matchShapeSize(world, pos);
        if (neighborBlock == this || check.isPanthalassaPortalFrame(neighborBlock.defaultBlockState())) {
            if (!check.match) {
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
    }

    @Override
    public void entityInside(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (world instanceof ServerWorld && !entity.isOnPortalCooldown()) {
            changeDimension((ServerWorld) world, entity, pos, new PanthalassaTeleporter((ServerWorld) world));
        }
    }

    public static class matchShapeSize {
        private final IWorld world;
        public boolean match = true;
        BlockPos centerPosition;
        float minPortalFrameRadius = 6.1f;
        float maxPortalFrameRadius = 7.5f;

        public matchShapeSize(IWorld world, BlockPos pos, boolean createPortal) {
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

        public matchShapeSize(IWorld world, BlockPos pos) {
            this(world, pos, false);
        }

        public static void recursivelyFindPortalPositions(IWorld world, BlockPos portalCenter, BlockPos currentOffset, Set<BlockPos> savedOffsets) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
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
            return (state.getFluidState().is(FluidTags.WATER) || state.getBlockState() == PanthalassaBlocks.PORTAL.get().defaultBlockState());
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
                this.world.setBlock(centerPosition.offset(pos), Blocks.WATER.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
            }
        }

        public boolean checkIfValidPortalFrame(BlockPos pos) {
            float minRadiusSq = minPortalFrameRadius * minPortalFrameRadius;
            float maxRadiusSq = maxPortalFrameRadius * maxPortalFrameRadius;
            BlockPos.Mutable mutable = new BlockPos.Mutable();
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
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            for (int x = (int) -maxPortalFrameRadius; x < maxPortalFrameRadius; x++) {
                for (int z = (int) -maxPortalFrameRadius; z < maxPortalFrameRadius; z++) {
                    int distSq = x * x + z * z;
                    if (distSq > minRadiusSq && distSq < maxRadiusSq) {
                        mutable.set(this.centerPosition).move(x, 0, z);
                        this.world.setBlock(mutable, PanthalassaBlocks.PORTAL_FRAME.get().defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);

                        // Helps prevent portal frame from sticking out over ledges
                        if (((ServerWorld) this.world).dimension() == PanthalassaDimension.PANTHALASSA) {
                            while (mutable.move(Direction.UP).getY() < this.world.getHeight() && !this.world.getBlockState(mutable).is(Blocks.BEDROCK)) {
                                this.world.setBlock(mutable, PanthalassaBlocks.PANTHALASSA_ROCK.get().defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                            }
                        }
                    }
                }
            }
        }

        public void createPortalCenter() {
            float minRadiusSq = minPortalFrameRadius * minPortalFrameRadius;
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            for (int x = (int) -minPortalFrameRadius; x < minPortalFrameRadius; x++) {
                for (int z = (int) -minPortalFrameRadius; z < minPortalFrameRadius; z++) {
                    int distSq = x * x + z * z;
                    if (distSq <= minRadiusSq) {
                        mutable.set(this.centerPosition).move(x, 0, z);
                        this.world.setBlock(mutable, PanthalassaBlocks.PORTAL.get().defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);

                        TileEntity tileEntity = this.world.getBlockEntity(mutable);
                        if (tileEntity instanceof BlockPortalTileEntity) {
                            ((BlockPortalTileEntity) tileEntity).offsetFromCenter = new BlockPos(x, 0, z);
                        }
                        tileEntity.setChanged();
                        // Helps create some space for mobs to swim into portal
                        if (((ServerWorld) this.world).dimension() == PanthalassaDimension.PANTHALASSA) {
                            while (mutable.move(Direction.UP).getY() < this.world.getHeight() && !this.world.getBlockState(mutable).is(Blocks.BEDROCK) && mutable.getY() < this.centerPosition.getY() + 7) {
                                this.world.setBlock(mutable, Blocks.WATER.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                            }
                        }
                    }
                }
            }
        }

        public void linkPortalCenters(IWorld otherWorld, BlockPos centerOfOtherPortal) {
            float minRadiusSq = minPortalFrameRadius * minPortalFrameRadius;
            BlockPos.Mutable mutable1 = new BlockPos.Mutable();
            BlockPos.Mutable mutable2 = new BlockPos.Mutable();
            for (int x = (int) -maxPortalFrameRadius; x < maxPortalFrameRadius; x++) {
                for (int z = (int) -maxPortalFrameRadius; z < maxPortalFrameRadius; z++) {
                    int distSq = x * x + z * z;
                    if (distSq <= minRadiusSq) {
                        mutable1.set(centerPosition).move(x, 0, z);
                        mutable2.set(centerOfOtherPortal).move(x, 0, z);

                        TileEntity tileEntity1 = world.getBlockEntity(mutable1);
                        TileEntity tileEntity2 = otherWorld.getBlockEntity(mutable2);
                        if (tileEntity1 instanceof BlockPortalTileEntity && tileEntity2 instanceof BlockPortalTileEntity) {
                            BlockPortalTileEntity portal1 = ((BlockPortalTileEntity) tileEntity1);
                            BlockPortalTileEntity portal2 = ((BlockPortalTileEntity) tileEntity2);

                            portal1.destinationPos = portal2.getBlockPos();
                            portal1.destinationWorld = portal2.getLevel().dimension();

                            portal2.destinationPos = portal1.getBlockPos();
                            portal2.destinationWorld = portal1.getLevel().dimension();

                            portal1.setChanged();
                            portal2.setChanged();
                        }
                    }
                }
            }
        }
    }
}