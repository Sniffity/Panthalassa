package com.github.sniffity.panthalassa.common.block;

import com.github.sniffity.panthalassa.common.registry.PanthalassaBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
        return check.match;
    }



    public static class MatchShapeSize {

        private final IWorld world;


        boolean isPanthalssaPortalFrame(BlockState state) {

            return state == PanthalassaBlocks.PORTAL_FRAME.get().getDefaultState();
        }


        boolean match = true;



        //Verify that there is an exact match for the expected shape, a circle of diameter 15.

        //Exact is here: https://donatstudios.com/PixelCircleGenerator, width and height = 15.

        public MatchShapeSize(IWorld world, BlockPos pos) {


            //Given a random position within the circular portal frame, this will attempt to find te center position.

            //It does so by calling the centerPortal method.

            int offsetNS = centerPortal(pos, Direction.NORTH);

            int offsetEW = centerPortal(pos, Direction.EAST);

            this.world = world;

        //Once the center position is found, it will declared a BlockPos field named centerPosition with that value.
        BlockPos centerPosition = new BlockPos(pos.getX() + offsetNS, pos.getY(), pos.getZ() + offsetEW);

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
}

}


