package com.github.sniffity.panthalassa.common.block;

import com.github.sniffity.panthalassa.common.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.common.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.common.world.teleporter.PanthalassaTeleporter;
import com.github.sniffity.panthalassa.common.world.teleporter.TeleporterLogic;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
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
import net.minecraft.util.Direction;
import javax.annotation.Nonnull;

/**
 * Panthalassa Mod - Class: BlockPortal <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Atum 2, the Undergarden,
 * UltraAmplifiedDiemsnion and The Twilight Forest mods implement their own respective teleportation systems.
*/

public class BlockPortal extends Block {

    private static final VoxelShape portalShape = VoxelShapes.create(0.0D, 0.0, 0.0D, 1.0D, 1.0D, 1.0D);

    public BlockPortal() {
        super(Properties.create(
                Material.PORTAL,
                MaterialColor.CYAN)
                .hardnessAndResistance(-1.0F,3600000.0F)
                .sound(SoundType.GLASS)
                .setLightLevel((state) -> 10)
                .tickRandomly());
    }

    @Override
        public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
        return 5.0F;    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (trySpawnPortal(worldIn, pos)) {
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
    public boolean isReplaceable(@Nonnull BlockState state, @Nonnull Fluid fluid) {
        return false;
    }

    public boolean trySpawnPortal(World world, BlockPos pos) {
        BlockPortal.matchShapeSize check = new BlockPortal.matchShapeSize(world, pos);

        if (check.match) {
            check.placePortalBlocks();
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
        if (neighborBlock == this || check.isPanthalassaPortalFrame(neighborBlock.getDefaultState())) {
            if (!check.match) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
    }

    @Override
    public void onEntityCollision(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (world instanceof ServerWorld) {
            changeDimension((ServerWorld) world, entity, new PanthalassaTeleporter((ServerWorld) world));
        }
    }

    public static void changeDimension(ServerWorld initialWorld, Entity entity, PanthalassaTeleporter teleporter) {
        RegistryKey<World> targetWorldKey = initialWorld.getDimensionKey() == PanthalassaDimension.PANTHALASSA ? World.OVERWORLD : PanthalassaDimension.PANTHALASSA;
        ServerWorld targetWorld = initialWorld.getServer().getWorld(targetWorldKey);

        if (targetWorld == null) {
            return;
        }
        if (!entity.func_242280_ah()) {
            entity.world.getProfiler().startSection("panthalassa_portal");
            new TeleporterLogic(entity, targetWorld, initialWorld, teleporter);
            entity.func_242279_ag();
            entity.world.getProfiler().endSection();
        }
    }

    public static class matchShapeSize {
        private final IWorld world;
        BlockPos centerPosition;

        boolean isPanthalassaPortalFrame(BlockState state) {
            return state == PanthalassaBlocks.PORTAL_FRAME.get().getDefaultState();
        }

        boolean isWaterOrPortal(BlockState state) {
            return (state.getFluidState().isTagged(FluidTags.WATER) || state.getBlockState() == PanthalassaBlocks.PORTAL.get().getDefaultState());
        }

        boolean match = true;

        public matchShapeSize(IWorld world, BlockPos pos) {

            this.world = world;

            int offsetN = centerPortal(pos, Direction.NORTH);
            int offsetS = centerPortal(pos, Direction.SOUTH);
            int offsetE = centerPortal(pos, Direction.EAST);
            int offsetW = centerPortal(pos, Direction.WEST);

            centerPosition = new BlockPos(pos.getX() + ((offsetE - offsetW) / 2), pos.getY(), pos.getZ() - ((offsetN - offsetS) / 2));


            for (int z = -2; z < 3; z++) {
                if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(-7, 0, z)))) {
                    match = false;
                    break;
                }
            }

            if (match) {
                for (int z = -2; z < 3; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(7, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -4; z < -1; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(-6, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = 2; z < 5; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(-6, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -4; z < -1; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(6, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = 2; z < 5; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(6, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -5; z < -3; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(-5, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                for (int z = 4; z < 6; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(-5, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -5; z < -3; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(5, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                for (int z = 4; z < 6; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(5, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                for (int z = -6; z < -4; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(-4, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                for (int z = 5; z < 7; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(-4, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                for (int z = -6; z < -4; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(4, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = 5; z < 7; z++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(4, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(-3, 0, -6)))) {
                    match = false;
                }
            }

            if (match) {
                if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(-3, 0, 6)))) {
                    match = false;
                }
            }

            if (match) {
                if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(3, 0, -6)))) {
                    match = false;
                }
            }

            if (match) {
                if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(3, 0, 6)))) {
                    match = false;
                }
            }

            if (match) {
                for (int x = -2; x < 3; x++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(x, 0, -7)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int x = -2; x < 3; x++) {
                    if (!isPanthalassaPortalFrame(world.getBlockState(centerPosition.add(x, 0, 7)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -4; z < 4; z++) {
                    for (int x = -4; x < 4; x++) {
                        if (!isWaterOrPortal(world.getBlockState(centerPosition.add(x, 0, z)))) {
                            match = false;
                            break;
                        }
                    }
                }
            }

            if (match) {
                for (int z = -3; z < 4; z++) {
                    if (!isWaterOrPortal(world.getBlockState(centerPosition.add(-5, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -3; z < 4; z++) {
                    if (!isWaterOrPortal(world.getBlockState(centerPosition.add(5, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -1; z < 2; z++) {
                    if (!isWaterOrPortal(world.getBlockState(centerPosition.add(-6, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -1; z < 2; z++) {
                    if (!isWaterOrPortal(world.getBlockState(centerPosition.add(6, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int x = -1; x < 2; x++) {
                    if (!isWaterOrPortal(world.getBlockState(centerPosition.add(x, 0, -6)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int x = -1; x < 2; x++) {
                    if (!isWaterOrPortal(world.getBlockState(centerPosition.add(x, 0, 6)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int x = -3; x < 4; x++) {
                    if (!isWaterOrPortal(world.getBlockState(centerPosition.add(x, 0, -5)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int x = -3; x < 4; x++) {
                    if (!isWaterOrPortal(world.getBlockState(centerPosition.add(x, 0, 5)))) {
                        match = false;
                        break;
                    }
                }
            }
        }

        int centerPortal(BlockPos pos, Direction direction) {
            int distance;
            for (distance = 1; distance < 16; ++distance) {
                BlockPos blockpos = pos.offset(direction, distance);
                if (isPanthalassaPortalFrame(world.getBlockState(blockpos))) {
                    break;
                }
                if (distance == 15) {
                    distance = 0;
                    blockpos = pos.offset(direction, distance);
                    if (!isPanthalassaPortalFrame(world.getBlockState(blockpos))) {
                        distance = 15;
                        break;
                    }
                    break;
                }
            }
            return distance;
        }

        void placePortalBlocks() {
            for (int x = -4; x < 5; x++) {
                for (int z = -4; z < 5; z++) {
                    this.world.setBlockState(centerPosition.add(x, 0, z), PanthalassaBlocks.PORTAL.get().getDefaultState(), 2);
                }
                for (int z = -3; z < 4; z++) {
                    this.world.setBlockState(centerPosition.add(-5, 0, z), PanthalassaBlocks.PORTAL.get().getDefaultState(), 2);
                }
                for (int z = -3; z < 4; z++) {
                    this.world.setBlockState(centerPosition.add(5, 0, z), PanthalassaBlocks.PORTAL.get().getDefaultState(), 2);
                }
                for (int z = -1; z < 2; z++) {
                    this.world.setBlockState(centerPosition.add(-6, 0, z), PanthalassaBlocks.PORTAL.get().getDefaultState(), 2);
                }
                for (int z = -1; z < 2; z++) {
                    this.world.setBlockState(centerPosition.add(6, 0, z), PanthalassaBlocks.PORTAL.get().getDefaultState(), 2);
                }
            }
            for (int x = -3; x < 4; x++) {
                this.world.setBlockState(centerPosition.add(x, 0, -5), PanthalassaBlocks.PORTAL.get().getDefaultState(), 2);
            }
            for (int x = -3; x < 4; x++) {
                this.world.setBlockState(centerPosition.add(x, 0, 5), PanthalassaBlocks.PORTAL.get().getDefaultState(), 2);
            }
            for (int x = -1; x < 2; x++) {
                this.world.setBlockState(centerPosition.add(x, 0, -6), PanthalassaBlocks.PORTAL.get().getDefaultState(), 2);
            }
            for (int x = -1; x < 2; x++) {
                this.world.setBlockState(centerPosition.add(x, 0, 6), PanthalassaBlocks.PORTAL.get().getDefaultState(), 2);
            }
        }
        void destroyPortalBlocks() {
            for (int x = -4; x < 5; x++) {
                for (int z = -4; z < 5; z++) {
                    this.world.setBlockState(centerPosition.add(x, 0, z), Blocks.WATER.getDefaultState(), 2);
                }
                for (int z = -3; z < 4; z++) {
                    this.world.setBlockState(centerPosition.add(-5, 0, z), Blocks.WATER.getDefaultState(), 2);
                }
                for (int z = -3; z < 4; z++) {
                    this.world.setBlockState(centerPosition.add(5, 0, z), Blocks.WATER.getDefaultState(), 2);
                }
                for (int z = -1; z < 2; z++) {
                    this.world.setBlockState(centerPosition.add(-6, 0, z), Blocks.WATER.getDefaultState(), 2);
                }
                for (int z = -1; z < 2; z++) {
                    this.world.setBlockState(centerPosition.add(6, 0, z), Blocks.WATER.getDefaultState(), 2);
                }
            }
            for (int x = -3; x < 4; x++) {
                this.world.setBlockState(centerPosition.add(x, 0, -5), Blocks.WATER.getDefaultState(), 2);
            }
            for (int x = -3; x < 4; x++) {
                this.world.setBlockState(centerPosition.add(x, 0, 5), Blocks.WATER.getDefaultState(), 2);
            }
            for (int x = -1; x < 2; x++) {
                this.world.setBlockState(centerPosition.add(x, 0, -6), Blocks.WATER.getDefaultState(), 2);
            }
            for (int x = -1; x < 2; x++) {
                this.world.setBlockState(centerPosition.add(x, 0, 6), Blocks.WATER.getDefaultState(), 2);
            }
        }
    }
}