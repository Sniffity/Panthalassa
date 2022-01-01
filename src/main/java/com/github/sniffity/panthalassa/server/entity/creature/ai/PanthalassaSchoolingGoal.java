package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;


import javax.xml.crypto.dsig.Transform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Panthalassa Mod - Class: PanthalassaWorldSavedData <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed with the assitance of BobMowzie. Additionally, the following
 * repository was referenced: https://github.com/codatproduction/Boids-simulation/blob/master/src/Boid.gd
 */

public class PanthalassaSchoolingGoal extends Goal {

    PanthalassaEntity panthalassaEntity;
    ISchoolable panthalassaSchoolableEntity;
    float schoolSpeed;
    float schoolMaxSize;
    float schoolAvoidRadius;
    float rayTraceLength = 10;
    int numViewDirections = 300;
    Vec3[] directions;
    List<? extends PanthalassaEntity> school;
    PanthalassaEntity leader;


    public PanthalassaSchoolingGoal(PanthalassaEntity creatureIn, float movementSpeedIn, float schoolMaxSize) {
        this.panthalassaEntity = creatureIn;
        this.panthalassaSchoolableEntity = (ISchoolable) creatureIn;
        this.schoolSpeed = movementSpeedIn;
        this.schoolMaxSize = schoolMaxSize;
        this.schoolAvoidRadius = schoolMaxSize;
    }

    @Override
    public boolean canUse() {
        this.school = panthalassaEntity.level.getEntitiesOfClass(panthalassaEntity.getClass(), panthalassaEntity.getBoundingBox().inflate(10));
        //If there's a single PanthalassaEntity, do not attempt schooling.
        if (school.size()<=1){
            return  false;
        }
        //If the PanthalassaEntity is not in water, do not attempt schooling.
        if (!this.panthalassaEntity.isInWater()) {
            return false;
        }
        //Only attempt schooling if there's a leader PanthalassaEntity nearby, else don't.
        for (int i=0; i<school.size(); i++) {
            PanthalassaEntity testEntity = school.get(i);
            if (((ISchoolable) testEntity).getIsLeader()) {
                return true;

            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        //Leader will be assigned on start. If leader is ever null, or if it dies, stop executing goal.
        if (leader == null || this.leader.isDeadOrDying()) {
            return false;
        }
        //If entity ever gets out of the water, stop schooling.
        if (!panthalassaEntity.isInWater()) {
            return false;
        }
        if (panthalassaEntity.getTarget() != null || leader.getTarget() != null) {
            return false;
        }

        return true;
    }


    @Override
    public void start() {
        processLeader((List<PanthalassaEntity>) school);
    }


    @Override
    public void tick() {
        //Instantiate vectors we will be working with...
        Vec3 attract = new Vec3(0, 0, 0);
        Vec3 repel = new Vec3(0, 0, 0);
        Vec3 follow;
        Vec3 avoid = new Vec3(0, 0, 0);
        Vec3 newMovement;

        //Remove this PanthalassaEntity from the school for operations...
        this.school.remove(panthalassaEntity);
        //Only carry out school operations if the school is not empty
        if (!this.school.isEmpty()) {
            //Repel ensures the PanthalassaEntity do not collide onto each other
            repel = (processRepel(school));
            //Follow is calculated considering the speed of all PanthalassaEntity, but the leader will not have follow applied
            follow = (processFollow(school));
            //Avoid ensures the PanthalassaEntity that are following the leader avoid obstacles..
            avoid = (processAvoid(panthalassaEntity));

            // Now normalize the vectors to get just the directions, and scale by the amount we want each force to have. These values will need fine-tuning
            repel = repel.normalize().scale(1.2);
            follow = follow.normalize().scale(1.0);
            avoid = avoid.normalize().scale(1.5);

            //Only those who are not leaders will have the follow component applied to them.
            //Hence, the leader will retain its previous movement, the rest will adjust to leader.
            //Of note: Leader is following RandomSwimmingAI and utilizing SwimmingHelper - it'll avoid obstacles on its own...
            //Followers must still have some method for avoiding obstacles.
            if (!this.panthalassaSchoolableEntity.getIsLeader()) {
                newMovement = panthalassaEntity.getDeltaMovement().add(repel).add(follow).add(avoid);
            } else {
                newMovement = panthalassaEntity.getDeltaMovement().add(repel);
            }

            // Clamping the vector to the leader's speed, to ensure the followers do not overtake leader in position
            //Initialization of leaderSpeed to 1.0F is present just in case there is no leader, to avoid a NPE crash.
            //This, however, should almost never be the case...
            float leaderSpeed = 1.0F;
            if (leader != null) {
                leaderSpeed = (float) leader.getDeltaMovement().length();
            }

            if (newMovement.length() > leaderSpeed) {
                newMovement = newMovement.normalize().scale(leaderSpeed);
            }

            this.panthalassaEntity.setDeltaMovement(newMovement);



          if (this.leader != null) {
                if (!panthalassaSchoolableEntity.getIsLeader()) {
                    panthalassaEntity.lookAt(EntityAnchorArgument.Anchor.EYES, panthalassaEntity.position().add(panthalassaEntity.getDeltaMovement()));
                }
            }
        }
    }

    protected void processLeader(List<? extends PanthalassaEntity>  school) {
        //Iterate over the school, find the leader, identify its status as leader.
        for (int i = 0; i < school.size(); i++) {
            PanthalassaEntity testEntity = school.get(i);
            if (((ISchoolable) testEntity).getIsLeader()) {
                leader = testEntity;
                break;
            }
        }
    }

    protected Vec3 processRepel(List<? extends PanthalassaEntity>  school) {
        Vec3 separation = new Vec3(0, 0, 0);
        //Create a new list to store PanthalassaEntity that are close to THIS PanthalassaEntity....
        List<PanthalassaEntity> closeEntities = new ArrayList<>();
        int schoolSize = school.size();
        for (int i = 0; i < schoolSize && i < 4; i++) {
            //Get the position of each PanthalassaEntity in school...
            PanthalassaEntity testEntity = school.get(i);
            float distanceToEntity = (float) panthalassaEntity.position().subtract(testEntity.position()).length();
            //Add those that PanthalassaEntity are too close to THIS PanthalassaEntity to the list specified above.
            if (distanceToEntity < schoolAvoidRadius) {
                closeEntities.add(testEntity);
            }
        }
        //If there are no close PanthalassaEntity, return a zero-vector. No adjustment will be made for avoidance.
        if (closeEntities.isEmpty()) {
            return new Vec3(0, 0, 0);
        }

        //Given that the list is not empty...
        int closeSize = closeEntities.size();
        for (int i = 0; i < closeSize; i++) {
            PanthalassaEntity testEntity = school.get(i);
            //For each PanthalassaEntity create a vector...
            //This vector points from the CURRENT PanthalassaEntity being analyzed to THIS PanthalassaEntity
            Vec3 difference = panthalassaEntity.position().subtract(testEntity.position());
            //For this vector, normalize it to get a direction..
            //Then scale it down, its length will depend inversely on the distance to THIS PanthalassaEntity
            separation = separation.add(difference.normalize().scale(1.0f / difference.length()));
        }
        //All of these vectors have been added together...
        //Each PanthalassaEntity analyzed contributed to these vectors..
        //The resulting vector will be one that pushes THIS PanthalassaEntity away from all OTHER PanthalassaEntity
        //Get an average to scale this vector...
        separation = separation.scale(1.0f / closeSize);
        //This vector will be scaled by SCHOOL_SPEED, which is a factor that scales all school "operations"
        Vec3 target = separation.normalize().scale(schoolSpeed);
        //From this vector, subtract the velocity vector of the PanthalassaEntity...

        //This creates a new Vector, attraction, that will attempt to adjust position away from center of group...
        //Taking the speed of THIS Kronosaurus into account
        return target.subtract(panthalassaEntity.getDeltaMovement());
    }

    protected Vec3 processFollow(List<? extends PanthalassaEntity>  school) {
        //We will proceed to set the speed of the followers in school to match that of the leader...
        //Once again, by this point, we only have a single leader, each PanthalassaEntity will search the leader closes to it
        int size = school.size();
        Vec3 speedVector = new Vec3(0, 0, 0);

        for (int i = 0; i < size && i < 4; i++) {
            PanthalassaEntity testEntity = school.get(i);
            if (((ISchoolable)testEntity).getIsLeader()) {
                speedVector = testEntity.getDeltaMovement();
            }
        }
        //Vector3d averageSpeed = speedVector.scale(1.0f / (float) size);
        //This vector will be scaled by SCHOOL_SPEED, which is a factor that scales all school "operations"
        //Vector3d target = (speedVector.subtract(this.position())).normalize().scale(SCHOOL_SPEED);
        Vec3 target = (speedVector.normalize().scale(schoolSpeed));
        //From this vector, subtract the velocity vector of the PanthalassaEntity...

        //This creates a new Vector, follow, that will attempt to adjust speed of THIS PanthalassaEntity to the speed of the group...
        //Taking the speed of THIS Kronosaurus into account
        return target.subtract(panthalassaEntity.getDeltaMovement());
    }

    protected Vec3 processAvoid(PanthalassaEntity entityIn) {
        //Cast a series of vectors that generate a spherical surface
        this.collisionVectors();
        List<Vec3> viableDirections = new ArrayList<>();
        //Analyze those vectors, from the target
        for (int i = 0; i < directions.length; i++) {
            //Position this vector relative to the entity...
            //Skip those vectors which produce too great an angle with the entity's movement vector.
            //Since the entity is looking where it's going, this will ensure we only take vectors that are pointing in the general direction towards where the entity is moving.
            float vecAngle = (float) (Math.acos(entityIn.position().add(directions[i]).dot(entityIn.getDeltaMovement())) * Math.PI / 180);
            if (vecAngle > 50) {
                continue;
            }
            //For those vectors which are not skipped, check for a collision...
            HitResult raytraceresult = entityIn.level.clip(new ClipContext(entityIn.position(), entityIn.position().add(directions[i]).normalize().scale(rayTraceLength), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, entityIn));
            if (!(raytraceresult.getType() == HitResult.Type.BLOCK)) {
                //If it does not collide, add it to a new Vector array.
                viableDirections.add(entityIn.position().add(directions[i]));
            }
        }

        //If this new Vector array has no elements, no directions will result in avoidance. Return a 0 vector.
        if (viableDirections.isEmpty()){
            return new Vec3(0,0,0);
            //Else, get the average x, y and z positions from all vectors in the Array. This will give us the average avoid direction...
        } else {
            float xDir = 0;
            float yDir = 0;
            float zDir = 0;
            for (int i = 0; i < viableDirections.size(); i++) {
                xDir = ((float) (xDir + viableDirections.get(i).x));
                yDir = ((float) (yDir + viableDirections.get(i).y));
                zDir = ((float) (zDir + viableDirections.get(i).z));
            }
            xDir = xDir/viableDirections.size();
            yDir = yDir/viableDirections.size();
            zDir = zDir/viableDirections.size();
            //Return the average avoid direction...
            return new Vec3(xDir,yDir,zDir);
        }
    }


        /*
        //Once all school vectors have been calculated perform a check around it, 7 block radius.
        AABB searchArea = new AABB(entityIn.getX() - 3, entityIn.getY() - 3, entityIn.getZ() - 3, entityIn.getX() + 3, entityIn.getY() + 3, entityIn.getZ() + 3);
        //Filter only the blocks that canOcclude, solid blocks and air itself, to avoid floating on the surface of the water...
        Set<BlockPos> set = BlockPos.betweenClosedStream(searchArea)
                .map(pos -> new BlockPos(pos))
                .filter(state -> (panthalassaEntity.level.getBlockState(state).canOcclude() || panthalassaEntity.level.getBlockState(state).is(Blocks.AIR)))
                .collect(Collectors.toSet());
        Iterator<BlockPos> it = set.iterator();

        //If there's solid blocks around, if the set is not empty, we will perform all the methods for avoidance...
        if (!set.isEmpty()) {
            //Initialize closestPos and distanceToClosestPos
            // The values assigned to closastPos and distanceToClosestPos will always be replaced by something.
            //This is because if we have got to this point, the iterator will run at least once
            BlockPos closestPos = new BlockPos(0, 0, 0);
            float distanceToClosestPos = 100;

            while (it.hasNext()) {
                BlockPos testPos = it.next();
                float distanceToPos = (float) entityIn.position().subtract(testPos.getX(), testPos.getY(), testPos.getZ()).length();
                if (distanceToPos < distanceToClosestPos) {
                    distanceToClosestPos = distanceToPos;
                    closestPos = new BlockPos(testPos.getX(), testPos.getY(), testPos.getZ());
                }
            }

            Vec3 targetAwayFromClosestPos = (entityIn.position()).subtract(closestPos.getX(), closestPos.getY(), closestPos.getZ());
            targetAwayFromClosestPos = targetAwayFromClosestPos.normalize();
            return targetAwayFromClosestPos.subtract(entityIn.getDeltaMovement());
        }
        return new Vec3(0,0,0);

         */


    @Override
    public void stop() {
        this.panthalassaEntity.setDeltaMovement(0,0,0);
        super.stop();
    }

    protected float rotlerp(float p_75639_1_, float p_75639_2_, float p_75639_3_) {
        float f = Mth.wrapDegrees(p_75639_2_ - p_75639_1_);
        if (f > p_75639_3_) {
            f = p_75639_3_;
        }

        if (f < -p_75639_3_) {
            f = -p_75639_3_;
        }

        float f1 = p_75639_1_ + f;
        if (f1 < 0.0F) {
            f1 += 360.0F;
        } else if (f1 > 360.0F) {
            f1 -= 360.0F;
        }

        return f1;
    }

    protected void collisionVectors(){
        directions = new Vec3[this.numViewDirections];
        float goldenRatio = (float) (1 + Math.sqrt (5)) / 2;
        float angleIncrement = Mth.PI * 2 * goldenRatio;

        for (int i = 0; i < numViewDirections; i++) {
            float t = (float) i / numViewDirections;
            float inclination = (float) Math.acos (1 - 2 * t);
            float azimuth = angleIncrement * i;

            float x = (float) (Math.sin (inclination) * Math.cos (azimuth));
            float y = (float) (Math.sin (inclination) * Math.sin (azimuth));
            float z = (float) (Math.cos (inclination));
            directions[i] = new Vec3 (x, y, z);
        }
    }
}

