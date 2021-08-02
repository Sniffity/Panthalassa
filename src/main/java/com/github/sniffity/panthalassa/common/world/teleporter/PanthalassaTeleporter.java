package com.github.sniffity.panthalassa.common.world.teleporter;


import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.common.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.common.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.common.registry.PanthalassaPOI;
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


public class PanthalassaTeleporter implements ITeleporter {

    protected final ServerWorld world;

    /*
     * Called by onEntityCollision method in {@link com.github.sniffity.panthalassa.common.block.PanthalassaPortalBlock}, returns a new PanthalassaTeleport object.
     * For this PanthalassaTeleport object, the instance field world is assigned to the passed argument, worldIn.
     * PanthalassaPortalBlock will input the new teleporter object as an argument into different method calls.
     */

    public PanthalassaTeleporter(ServerWorld worldIn) {
        this.world = worldIn;
    }

    /*
     Within {@link com.github.sniffity.panthalassa.common.block.PanthalassaPortalBlock}, a call is made to the changeDimesion() method, in the {@link net.minecraft.entity.Entity} class.
     This call is made upon an entity collision.
     The teleporter is passed as an argument.
     The changeDimension() method will instantiate a PortalInfo object by calling the getPortalInfo method within the teleporter.
     */


    /*
     We override the ITeleporter method getPortalInfo was we want more complex behavior.
     */
    @Nullable
    /*
    In this context, getPortalInfo will be called with the following arguments:
    -entity: The entity which collided with the portal.
    -destWorld: The world where the entity will be teleported to.
    -defaultPortalInfO: A reference to the vanilla method for getting portal info.
     */
    public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld) {

        /*
        getPortalInfo first calls the TeleportationRepositioner.
        This will return an optional field, meaning it may have non-null value, or it may not.
        We can check for whether or not he value is null.
         */
        Optional<TeleportationRepositioner.Result> result = teleporterResult(destWorld, entity.getPosition());

            BlockPos startPos = result.get().startPos;
            if (result.isPresent()) {
                if (destWorld.getDimensionKey() == PanthalassaDimension.PANTHALASSA) {
                    //If we got an existing portal...
                    //Set the location to which we will teleport the entity to the portal's location.
                    //Adjust the entity's position slightly:
                    //The portal in the dimension will be on the roof, hence move the entity down.
                    //destWorld.getChunk(new BlockPos(startPos.getX(),startPos.getY(),startPos.getZ()));
                    return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY() - 5, startPos.getZ()), entity.getMotion(), entity.rotationYaw, entity.rotationPitch);

                } else {
                    // The portal in the overworld will be on the floor, hence move the entity up.
                    destWorld.getChunk(new BlockPos(startPos.getX(),startPos.getY(),startPos.getZ()));
                    return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY() + 5, startPos.getZ()-1), entity.getMotion(), entity.rotationYaw, entity.rotationPitch);
                }
            }

            //If we did not get an existing portal...
            //Set the location to which we will teleport the entity to the entity's location in the overworld.
            else {

                if (destWorld.getDimensionKey() != PanthalassaDimension.PANTHALASSA) {
                    return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY() - 5, startPos.getZ()), entity.getMotion(), entity.rotationYaw, entity.rotationPitch);
                } else {

                    return new PortalInfo(new Vector3d(startPos.getX(), startPos.getY() + 5, startPos.getZ()-1), entity.getMotion(), entity.rotationYaw, entity.rotationPitch);
                }

            }

    }



    protected Optional<TeleportationRepositioner.Result> teleporterResult(ServerWorld serverWorld, BlockPos pos) {
        /*
        teleporterResult will decide what the teleporter will do.
        Say we are teleporting from the Overworld to, in this case, our custom dimension, Panthalassa.
        If we step into a portal in the Overworld, we can either:
        -Be placed in an existing portal in Panthalassa.
        -Generate a new portal as we enter Panthalassa.
        This method "decides" what will happen, it gives the "result for the teleporter"
        It does so by attempting to get an existing portal...
         */
        Optional<TeleportationRepositioner.Result> optional = getExistingPortal(serverWorld, pos);

        if (optional.isPresent()) {
            /*
            If it finds an existing portal (hence, optional is present, it will return a non-0ull value, optional
            This value will correspond to the rectangle of portal blocks that was found.
            */
            return optional;
        } else {

            //Else, if the check for an existing portal failed, we will make a new portal, using the makePortalFromPos method..
            //And return its position
            return makePortalFromPos(serverWorld, pos);

        }
    }


    public Optional<TeleportationRepositioner.Result> getExistingPortal(@Nonnull ServerWorld serverWorld, BlockPos pos) {
        //getExistingPortal will declare a PointofInterestManager, pointofinterestmanager.
        PointOfInterestManager pointofinterestmanager = serverWorld.getPointOfInterestManager();
        //This is the coordinate offset we will use for the following check...
        //Larger numbers mean that the section checked for an existing portal will be larger.
        int i = 256;
        //Which verifies whether the chunks being targeted have their POI manager loaded and whether it's valid.
        pointofinterestmanager.ensureLoadedAndValid(serverWorld, pos, i);
        /*
        Here, we will attempt to search for the poi we wish to "target", in this case, the portal.
        Because of the way we register our poi, the block that defines it is the portal block.
        */
        Optional<PointOfInterest> optional = pointofinterestmanager
                //Search for the poi PANTHALASSA_POI_PORTAL in a square, with coordinates offset 128.
                .getInSquare(
                        (poiType) -> { return poiType == PanthalassaPOI.PANTHALASSA_POI_PORTAL.get(); },
                        pos,
                        i,
                        PointOfInterestManager.Status.ANY)
                //Once found, proceed to sort the pois found according to their distance from the position at which entity teleported.
                //Additionally, sort them by their Y position.
                //Thus: Creates a "list" of sorts. Where the first poi will be the one closest to the entry portal.
                //This distance will be measure by a vector.
                //If two pois are at the same distance from the entry portal, compare them next by Y value.
                //In this case, I've told it to select the one with the **highest** Y value first.
                .sorted(
                        Comparator
                                .<PointOfInterest>comparingDouble((poi) -> {return poi.getPos().distanceSq(pos);})
                                .thenComparingInt((poi) -> {return -poi.getPos().getY();}))
                //Finally, get the first poi from the "list", the one at the top.
                .findFirst();

        return optional.map(
                (poi) -> {
                    //Optionally, if it has found a poi, it will get its position (set that blockpos)...
                    BlockPos blockpos = poi.getPos();
                    //And register a Ticket for the ChunkProvider, of type PORTAL (basically telling the chunk provider "there's a portal here"
                    serverWorld.getChunkProvider().registerTicket(TicketType.PORTAL, new ChunkPos(blockpos), 3, blockpos);
                    //Finally, it will get the blockstate at the poi position.
                    //A reminder that the poi we are looking for is a portal block.
                    // If multiple are present, one of them will be selected (after the above filtration).
                    BlockState blockstate = serverWorld.getBlockState(blockpos);
                    //Finally, the return value:
                    return TeleportationRepositioner
                            //It will find the largest rectangle...
                            .findLargestRectangle(
                                    //Centered on the poi's position...
                                    blockpos,
                                    //A rectangle with axis X...
                                    Direction.Axis.X,
                                    //And a maximum distance of 9 on that axis...
                                    9,
                                    //And axis Z...
                                    Direction.Axis.Z,
                                    //And a maximum distance of 9 on that axis...
                                    9,
                                    //And only select blocks that have the same blockstate as our poi.
                                    //Our poi was a portal block, it will basically only select portal blocks
                                    (posIn) -> this.world.getBlockState(posIn) == blockstate);
                    //We now know where to teleport to, if there's an existing portal.
                    //Of note: we search for a rectangle of blocks because, in this case, our portal is flat!
        });
    }

    private boolean checkRegionForPlacement(BlockPos potentialPos, World world) {
        //Checks region for placement.
        // Basically, checks for a 16*5*16 box below the portal.
        BlockPos check;
        int i;
        int j;
        int k;
        for (i = -8; i < 9; ++i) {
            for (j = -1; j > -5; --j) {
                for (k = -8; k < 9; ++k) {
                    check = new BlockPos(potentialPos.getX() + i, potentialPos.getY() + j, potentialPos.getZ() + k);

                    if (!(world.getBlockState(check) == Blocks.WATER.getDefaultState())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Optional<TeleportationRepositioner.Result> makePortalFromPos(ServerWorld world, @Nonnull BlockPos pos) {
        //This method is only called if the check for an existing portal fails.
        //Here, we will specify how we want to build our portal.

        //We have received the world where we will build the portal and the position where we would *like to build it*.
        //This may not always be possible.

        //For simplicity, we declare the blocks we will be using for the portal frame and portal center.
        BlockState portalCenter = PanthalassaBlocks.PORTAL.get().getDefaultState();
        BlockState portalFrame = PanthalassaBlocks.PORTAL_FRAME.get().getDefaultState();

        //We will now proceed to perform several checks until we find a suitable position.
        //First, since our target dimension has a roof a Y=128, we will start at middle Y, 64.
        BlockPos pos1 = new BlockPos(pos.getX(),64, pos.getZ());
        //Note that X and Z are given to this function, they are the position of the entity that collided with the PortalBlock

        //This is a check I added; if I am  attempting to teleport to the overworld, and I failed to find a portal before...
        if (world.getDimensionKey() == World.OVERWORLD) {
            //Output an erorr message: Do not build one.
            //This is because our overworld portals will spawn in structures, we only want a teleport if there's a matching structure.
            //So, basically, all entry portals into our dimension and exit portals from our dimension should be located at strucutre coordinates.
            Panthalassa.LOGGER.warn("Corresponding Overworld Portal not found!");
            Panthalassa.LOGGER.warn("Teleporting to spawn");
            BlockPos spawnPoint = new BlockPos(world.getSpawnPoint().getX(), world.getSpawnPoint().getY(), world.getSpawnPoint().getZ());
            return Optional.of(new TeleportationRepositioner.Result(spawnPoint.toImmutable(), 1, 1));
        }

        //We start by declaring the location as invalid.
        boolean validLocation;
        validLocation = false;

        //So long as the location is still marked as invalid, keep performing these checks.
        while (!validLocation) {
            //If our Y position is less than 128, and we are in water, we go up, we increase Y.
            //This is because our dimension is underwater, hence, water is equivalent to air.
            //We go up because I want to place the portal on a "roof" within the dimension.
            //We are going up. As soon as we hit a block that is not water, this loop will stop.
            //This has a check to avoid it going up infinitely.
            while (pos1.getY() < 128
                    && world.getFluidState(new BlockPos(pos1)).isTagged(FluidTags.WATER)
                    || world.getBlockState(pos1) == Blocks.KELP.getDefaultState()
                    || world.getBlockState(pos1) == PanthalassaBlocks.PANTHALASSA_ROCK.get().getDefaultState())  {
                pos1 = pos1.up();
            }

            //Once we have hit a solid block, or hit our roof if we didn't find one, we start going down.
            //We only go down until while we are not in water.
            //The moment we hit water, we stop going down.
            //So, if the dimension is filled with water, and we find a solid block on a roof, it is likely that we will have water beneath us.
            //We don't want the portal inserted into the roof itself, but rather coming out from the roof.
            //Hence, one block down.
            while (pos1.getY() > -1 && !world.getFluidState(new BlockPos(pos1)).isTagged(FluidTags.WATER)) {
                pos1 = pos1.down();
            }



            //However, if we got too low or too high... or the region for placement is in valid, we want to do this again, at another random position close by.
            //The position will be within +/- 20 X and +/- 20 Z.
            if (pos1.getY() < 10 || pos1.getY() > 118 || !checkRegionForPlacement(pos1, world )) {
                pos1 = new BlockPos(pos1.getX()+(int)Math.floor((Math.random())*(20))-20,64,pos1.getZ()+(int)Math.floor((Math.random())*(20))-20);
            } else {
                //If we are not below 10, or above 110, we mark the location as valid. This stops the whole loop.
                validLocation=true;
            }
        }

        //We then proceed to build the portal. The following conditions specify a circle of diameter 15.

        for (int z = -2; z < 3; z++) {
                world.setBlockState(pos1.add(-7, 0, z), portalFrame, 2);
            }
            for (int z = -2; z < 3; z++) {
                world.setBlockState(pos1.add(7, 0, z), portalFrame, 2);
            }

            for (int z = -4; z < -1; z++) {
                world.setBlockState(pos1.add(-6, 0, z), portalFrame, 2);
            }
            for (int z = 2; z < 5; z++) {
                world.setBlockState(pos1.add(-6, 0, z), portalFrame, 2);
            }
            for (int z = -4; z < -1; z++) {
                world.setBlockState(pos1.add(6, 0, z), portalFrame, 2);
            }
            for (int z = 2; z < 5; z++) {
                world.setBlockState(pos1.add(6, 0, z), portalFrame, 2);
            }

            for (int z = -5; z < -3; z++) {
                world.setBlockState(pos1.add(-5, 0, z), portalFrame, 2);
            }
            for (int z = 4; z < 6; z++) {
                world.setBlockState(pos1.add(-5, 0, z), portalFrame, 2);
            }
            for (int z = -5; z < -3; z++) {
                world.setBlockState(pos1.add(5, 0, z), portalFrame, 2);
            }
            for (int z = 4; z < 6; z++) {
                world.setBlockState(pos1.add(5, 0, z), portalFrame, 2);
            }


            for (int z = -6; z < -4; z++) {
                world.setBlockState(pos1.add(-4, 0, z), portalFrame, 2);
            }
            for (int z = 5; z < 7; z++) {
                world.setBlockState(pos1.add(-4, 0, z), portalFrame, 2);
            }
            for (int z = -6; z < -4; z++) {
                world.setBlockState(pos1.add(4, 0, z), portalFrame, 2);
            }
            for (int z = 5; z < 7; z++) {
                world.setBlockState(pos1.add(4, 0, z), portalFrame, 2);
            }

            world.setBlockState(pos1.add(-3, 0, -6), portalFrame, 2);
            world.setBlockState(pos1.add(-3, 0, 6), portalFrame, 2);
            world.setBlockState(pos1.add(3, 0, -6), portalFrame, 2);
            world.setBlockState(pos1.add(3, 0, 6), portalFrame, 2);

            for (int z = -7; z < -5; z++) {
                world.setBlockState(pos1.add(-2, 0, z), portalFrame, 2);
            }
            for (int z = 6; z < 8; z++) {
                world.setBlockState(pos1.add(-2, 0, z), portalFrame, 2);
            }
            for (int z = -7; z < -5; z++) {
                world.setBlockState(pos1.add(2, 0, z), portalFrame, 2);
            }
            for (int z = 6; z < 8; z++) {
                world.setBlockState(pos1.add(2, 0, z), portalFrame, 2);
            }

            for (int x = -2; x < 3; x++) {
                world.setBlockState(pos1.add(x, 0, -7), portalFrame, 2);
            }

            for (int x = -2; x < 3; x++) {
                world.setBlockState(pos1.add(x, 0, 7), portalFrame, 2);
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
            //We return our position to the teleporterResult method...
        return Optional.of(new TeleportationRepositioner.Result(pos1.toImmutable(), 15, 1));
    }

}