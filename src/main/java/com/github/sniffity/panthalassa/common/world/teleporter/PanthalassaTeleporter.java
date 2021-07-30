package com.github.sniffity.panthalassa.common.world.teleporter;

import com.github.sniffity.panthalassa.common.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.common.registry.PanthalassaPOI;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.TeleportationRepositioner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.common.util.ITeleporter;
import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.Optional;

public class PanthalassaTeleporter implements ITeleporter {

    protected final ServerWorld world;

    public PanthalassaTeleporter(ServerWorld worldIn) {
        this.world = worldIn;
    }


    public Optional<TeleportationRepositioner.Result> getExistingPortal(BlockPos pos, boolean isNether) {
        PointOfInterestManager pointofinterestmanager = this.world.getPointOfInterestManager();
        int i = 200;
        pointofinterestmanager.ensureLoadedAndValid(this.world, pos, i);
        Optional<PointOfInterest> optional = pointofinterestmanager.getInSquare((poiType) -> {
            return poiType == PanthalassaPOI.PANTHALASSA_PORTAL.get();
        }, pos, i, PointOfInterestManager.Status.ANY).sorted(Comparator.<PointOfInterest>comparingDouble((poi) -> {
            return poi.getPos().distanceSq(pos);
        }).thenComparingInt((poi) -> {
            return poi.getPos().getY();
        })).filter((poi) -> {
            return this.world.getBlockState(poi.getPos()).hasProperty(BlockStateProperties.HORIZONTAL_AXIS);
        }).findFirst();
        return optional.map((poi) -> {
            BlockPos blockpos = poi.getPos();
            this.world.getChunkProvider().registerTicket(TicketType.PORTAL, new ChunkPos(blockpos), 3, blockpos);
            BlockState blockstate = this.world.getBlockState(blockpos);
            return TeleportationRepositioner.findLargestRectangle(blockpos, blockstate.get(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, (posIn) -> {
                return this.world.getBlockState(posIn) == blockstate;
            });
        });
    }


    public Optional<TeleportationRepositioner.Result> makePortal(ServerWorld world, @Nonnull Entity entity) {
        BlockPos entityPos = new BlockPos(MathHelper.floor(entity.getPosX()), MathHelper.floor(entity.getPosY()), MathHelper.floor(entity.getPosZ()));
        BlockState portalCenter = PanthalassaBlocks.PANTHALASSA_ROCK.get().getDefaultState();
        BlockState portalFrame = PanthalassaBlocks.PORTAL_FRAME.get().getDefaultState();


            while (entityPos.getY() > 1 && world.isAirBlock(entityPos)) {
                entityPos = entityPos.down();
            }

            while (!world.isAirBlock(entityPos.up())) {
                entityPos = entityPos.up();
            }



            for (int z = -2; z < 3; z++) {
                world.setBlockState(entityPos.add(-7, 1, z), portalFrame, 2);
            }
            for (int z = -2; z < 3; z++) {
                world.setBlockState(entityPos.add(7, 1, z), portalFrame, 2);
            }
            for (int z = -4; z < -1; z++) {
                world.setBlockState(entityPos.add(-6, 1, z), portalFrame, 2);
            }
            for (int z = 2; z < 5; z++) {
                world.setBlockState(entityPos.add(-6, 1, z), portalFrame, 2);
            }
            for (int z = -2; z < -1; z++) {
                world.setBlockState(entityPos.add(6, 1, z), portalFrame, 2);
            }
            for (int z = 2; z < 5; z++) {
                world.setBlockState(entityPos.add(6, 1, z), portalFrame, 2);
            }
            for (int z = -5; z < -3; z++) {
                world.setBlockState(entityPos.add(-5, 1, z), portalFrame, 2);
            }
            for (int z = 4; z < 6; z++) {
                world.setBlockState(entityPos.add(-5, 1, z), portalFrame, 2);
            }
            for (int z = -5; z < -3; z++) {
                world.setBlockState(entityPos.add(5, 1, z), portalFrame, 2);
            }
            for (int z = 4; z < 6; z++) {
                world.setBlockState(entityPos.add(5, 1, z), portalFrame, 2);
            }


            for (int z = -6; z < -4; z++) {
                world.setBlockState(entityPos.add(-4, 1, z), portalFrame, 2);
            }
            for (int z = 5; z < 7; z++) {
                world.setBlockState(entityPos.add(-4, 1, z), portalFrame, 2);
            }
            for (int z = -6; z < -4; z++) {
                world.setBlockState(entityPos.add(4, 1, z), portalFrame, 2);
            }
            for (int z = 5; z < 7; z++) {
                world.setBlockState(entityPos.add(4, 1, z), portalFrame, 2);
            }

            world.setBlockState(entityPos.add(-3, 1, -6), portalFrame, 2);
            world.setBlockState(entityPos.add(-3, 1, 6), portalFrame, 2);
            world.setBlockState(entityPos.add(3, 1, -6), portalFrame, 2);
            world.setBlockState(entityPos.add(3, 1, 6), portalFrame, 2);

            for (int z = -7; z < -5; z++) {
                world.setBlockState(entityPos.add(-2, 1, z), portalFrame, 2);
            }
            for (int z = 6; z < 8; z++) {
                world.setBlockState(entityPos.add(-2, 1, z), portalFrame, 2);
            }
            for (int z = -7; z < -5; z++) {
                world.setBlockState(entityPos.add(2, 1, z), portalFrame, 2);
            }
            for (int z = 6; z < 8; z++) {
                world.setBlockState(entityPos.add(2, 1, z), portalFrame, 2);
            }

            for (int x = -2; x < 3; x++) {
                world.setBlockState(entityPos.add(x, 1, -7), portalFrame, 2);
            }
            for (int x = -2; x < 3; x++) {
                world.setBlockState(entityPos.add(x, 1, 7), portalFrame, 2);
            }
            return Optional.of(new TeleportationRepositioner.Result(entityPos.toImmutable(), 13, 1));

    }

}



    //Make a new portal if it doesn't find one.
    //NULL IF NOT IN OVERWORLD.