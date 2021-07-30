package com.github.sniffity.panthalassa.common.block;

import com.github.sniffity.panthalassa.common.registry.PanthalassaBlocks;
import com.google.common.cache.LoadingCache;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import java.util.Random;


public class PanthalassaPortalBlock extends Block {

    private static final VoxelShape portalShape = VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);

    public PanthalassaPortalBlock() {
        super(Properties.create(
                Material.PORTAL,
                MaterialColor.CYAN)
                .hardnessAndResistance(-1.0F)
                .sound(SoundType.GLASS)
                .notSolid()
                .setLightLevel((state) -> 10)
                .tickRandomly());
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
        PanthalassaPortalBlock.MatchShapeSize check = new PanthalassaPortalBlock.MatchShapeSize(world, pos);

        if (check.match) {
            check.placePortalBlocks();
            //world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 0.7F, 1.0F);
            return true;
        } else {
            PanthalassaPortalBlock.MatchShapeSize check1 = new PanthalassaPortalBlock.MatchShapeSize(world, pos);

            if (check1.match) {
                check1.placePortalBlocks();
                world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            } else {
                return false;
            }
        }
    }


    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block neighborBlock, @Nonnull BlockPos neighborPos, boolean isMoving) {
        PanthalassaPortalBlock.MatchShapeSize check = new PanthalassaPortalBlock.MatchShapeSize(world, pos);
        if (neighborBlock == this || check.isPanthalssaPortalFrame(neighborBlock.getDefaultState())) {
            if (!check.match) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
    }

    /*
    @Override
    public void onEntityCollision(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (world instanceof ServerWorld) {
            changeDimension((ServerWorld) world, entity, new PanthalassaTeleporter());
        }
    }
    */

    /*
    public static void changeDimension(ServerWorld serverWorld, Entity entity, ITeleporter teleporter) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            RegistryKey<World> key = serverWorld.getDimensionKey() == Atum.ATUM ? World.OVERWORLD : Atum.ATUM;
            ServerWorld destWorld = serverWorld.getServer().getWorld(key);
            if (destWorld == null) {
                return;
            }
            if (player.field_242273_aw <= 0) {
                player.world.getProfiler().startSection("portal");
                player.changeDimension(destWorld, teleporter);
                player.field_242273_aw = 300; //Set portal cooldown
                player.world.getProfiler().endSection();
            }
        }


     */

    /*
    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return ItemStack.EMPTY;
    }
*/
    /*
    public static BlockPattern.PatternHelper createPatternHelper(IWorld world, BlockPos pos) {
        PanthalassaPortalBlock.MatchShapeSize check = new PanthalassaPortalBlock.MatchShapeSize(world, pos);
        LoadingCache<BlockPos, CachedBlockInfo> cache = BlockPattern.createLoadingCache(world, true);

        if (!check.match) {
            check = new MatchShapeSize(world, pos);
        }
        if (!check.match) {
            return new BlockPattern.PatternHelper(pos, Direction.NORTH, Direction.SOUTH, cache, 1, 1, 1);
        } else {
            return new BlockPattern.PatternHelper(pos, Direction.NORTH, Direction.EAST, cache, size.width, 4, size.length);
        }

*/

    /*
        if (!size.isValid()) {
            return new BlockPattern.PatternHelper(pos, Direction.NORTH, Direction.SOUTH, cache, 1, 1, 1);
        } else {
            return new BlockPattern.PatternHelper(pos, Direction.NORTH, Direction.EAST, cache, size.width, 4, size.length);
        }

*/


    public static class MatchShapeSize {

        private final IWorld world;
        BlockPos centerPosition;

        boolean isPanthalssaPortalFrame(BlockState state) {

            return state == PanthalassaBlocks.PORTAL_FRAME.get().getDefaultState();
        }


        boolean match = true;


        //Verify that there is an exact match for the expected shape, a circle of diameter 15.
        //Exact is here: https://donatstudios.com/PixelCircleGenerator, width and height = 15.

        public MatchShapeSize(IWorld world, BlockPos pos) {


            //Given a random position within the circular portal frame, this will attempt to find the center position.
            //It does so by calling the centerPortal method.

            int offsetNS = centerPortal(pos, Direction.NORTH);

            int offsetEW = centerPortal(pos, Direction.EAST);

            this.world = world;

            //Once the center position is found, it will declared to the BlockPos field named centerPosition with that value.
            centerPosition = new BlockPos(pos.getX() + offsetNS, pos.getY(), pos.getZ() + offsetEW);

            //Then, it will carry out several checks starting on the BlockPos field, to verify an exact shape match.

            //This series of loops of IF statements only continues if all the blocks match the Portal Frame condition.
            //A single block not matching and the whole sequence stops, declaring a false match.
            for (int z = -2; z < 3; z++) {
                if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(-7, 0, z)))) {
                    match = false;
                    break;
                }
            }

            //An initial check has been carried above. This sequence will only continue if the match has not failed.
            //A failed match skips this series of checks.
            if (match) {
                for (int z = -2; z < 3; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(-7, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -2; z < 3; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(7, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }


            if (match) {
                for (int z = -4; z < -1; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(-6, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                for (int z = 2; z < 5; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(-6, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -4; z < -1; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(6, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                for (int z = 2; z < 5; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(6, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -5; z < -3; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(-5, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                for (int z = 4; z < 6; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(-5, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -5; z < -3; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(5, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                for (int z = 4; z < 6; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(5, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }


            if (match) {
                for (int z = -6; z < -4; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(-4, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }
            if (match) {
                for (int z = 5; z < 7; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(-4, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = -6; z < -4; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(4, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int z = 5; z < 7; z++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(4, 0, z)))) {
                        match = false;
                        break;
                    }
                }
            }


            if (match) {
                if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(-3, 0, -6)))) {
                    match = false;
                }
            }
            if (match) {
                if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(-3, 0, 6)))) {
                    match = false;
                }
            }
            if (match) {
                if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(3, 0, -6)))) {
                    match = false;
                }
            }
            if (match) {
                if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(3, 0, 6)))) {
                    match = false;
                }
            }

            if (match) {
                for (int x = -2; x < 3; x++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(x, 0, -7)))) {
                        match = false;
                        break;
                    }
                }
            }

            if (match) {
                for (int x = -2; x < 3; x++) {
                    if (!isPanthalssaPortalFrame(world.getBlockState(centerPosition.add(x, 0, 7)))) {
                        match = false;
                        break;
                    }
                }
            }
        }

        boolean matchIsPositive() {
            return this.match;
        }


        //Find the center of the portal frame.
        int centerPortal(BlockPos pos, Direction direction) {
            int distance;
            for (distance = 0; distance < 16; ++distance) {
                BlockPos blockpos = pos.offset(direction, distance);
                if (!isPanthalssaPortalFrame(this.world.getBlockState(blockpos))) {
                    break;
                }
            }
            int centerOffset;
            if (distance == 0) {
                centerOffset = 7;
            /*
           NEED TO NULL IF IT'S OUTSIDE.
        } else if (distance == 15) {
            centerOffset = null;
            */
            } else {
                centerOffset = MathHelper.floor(distance / 2);
            }
            return centerOffset;
        }

        void placePortalBlocks() {
            for (int x = -4; x < 5; x++) {
                for (int z = -4; z < 5; z++) {
                    this.world.setBlockState(centerPosition.add(x, 0, z), PanthalassaBlocks.PORTAL.get().getDefaultState(), 2);

                }
            }
        }
    }
}




