package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;


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
    List<PanthalassaEntity> school;
    PanthalassaEntity leader;


    public PanthalassaSchoolingGoal(PanthalassaEntity creatureIn, float movementSpeedIn, float schoolMaxSize, float schoolAvoidRadius) {
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
        processLeader(school);
    }


    @Override
    public void tick() {
        //Instantiate vectors we will be working with...
        Vector3d attract = new Vector3d(0, 0, 0);
        Vector3d repel = new Vector3d(0, 0, 0);
        Vector3d follow;
        Vector3d avoid = new Vector3d(0, 0, 0);
        Vector3d newMovement;

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
                    panthalassaEntity.lookAt(EntityAnchorArgument.Type.EYES, panthalassaEntity.position().add(panthalassaEntity.getDeltaMovement()));
                }
            }
        }
    }

    protected void processLeader(List<PanthalassaEntity> school) {
        //Iterate over the school, find the leader, identify its status as leader.
        for (int i = 0; i < school.size(); i++) {
            PanthalassaEntity testEntity = school.get(i);
            if (((ISchoolable) testEntity).getIsLeader()) {
                leader = testEntity;
                break;
            }
        }
    }

    protected Vector3d processRepel(List<PanthalassaEntity> school) {

        Vector3d separation = new Vector3d(0, 0, 0);
        //Create a new list to store PanthalassaEntity that are too close to THIS PanthalassaEntity....
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
            return new Vector3d(0, 0, 0);
        }

        //Given that the list is not empty...
        int closeSize = closeEntities.size();
        for (int i = 0; i < closeSize; i++) {
            PanthalassaEntity testEntity = school.get(i);
            //For each PanthalassaEntity create a vector...
            //This vector points from the CURRENT PanthalassaEntity being analyzed to THIS PanthalassaEntity
            Vector3d difference = panthalassaEntity.position().subtract(testEntity.position());
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
        Vector3d target = separation.normalize().scale(schoolSpeed);
        //From this vector, subtract the velocity vector of the PanthalassaEntity...

        //This creates a new Vector, attraction, that will attempt to adjust position away from center of group...
        //Taking the speed of THIS Kronosaurus into account
        return target.subtract(panthalassaEntity.getDeltaMovement());
    }

    protected Vector3d processFollow(List<PanthalassaEntity> school) {
        //We will proceed to set the speed of the followers in school to match that of the leader...
        //Once again, by this point, we only have a single leader, each PanthalassaEntity will search the leader closes to it
        int size = school.size();
        Vector3d speedVector = new Vector3d(0, 0, 0);

        for (int i = 0; i < size && i < 4; i++) {
            PanthalassaEntity testEntity = school.get(i);
            if (((ISchoolable)testEntity).getIsLeader()) {
                speedVector = testEntity.getDeltaMovement();
            }
        }
        //Vector3d averageSpeed = speedVector.scale(1.0f / (float) size);
        //This vector will be scaled by SCHOOL_SPEED, which is a factor that scales all school "operations"
        //Vector3d target = (speedVector.subtract(this.position())).normalize().scale(SCHOOL_SPEED);
        Vector3d target = (speedVector.normalize().scale(schoolSpeed));
        //From this vector, subtract the velocity vector of the PanthalassaEntity...

        //This creates a new Vector, follow, that will attempt to adjust speed of THIS PanthalassaEntity to the speed of the group...
        //Taking the speed of THIS Kronosaurus into account
        return target.subtract(panthalassaEntity.getDeltaMovement());
    }

    protected Vector3d processAvoid(PanthalassaEntity entityIn) {
        //Once all school vectors have been calculated perform a check around it, 7 block radius.
        AxisAlignedBB searchArea = new AxisAlignedBB(entityIn.getX() - 3, entityIn.getY() - 3, entityIn.getZ() - 3, entityIn.getX() + 3, entityIn.getY() + 3, entityIn.getZ() + 3);
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

            Vector3d targetAwayFromClosestPos = (entityIn.position()).subtract(closestPos.getX(), closestPos.getY(), closestPos.getZ());
            targetAwayFromClosestPos = targetAwayFromClosestPos.normalize();
            return targetAwayFromClosestPos.subtract(entityIn.getDeltaMovement());
        }
        return new Vector3d(0,0,0);
    }

    @Override
    public void stop() {
        this.panthalassaEntity.setDeltaMovement(0,0,0);
        super.stop();
    }

    protected float rotlerp(float p_75639_1_, float p_75639_2_, float p_75639_3_) {
        float f = MathHelper.wrapDegrees(p_75639_2_ - p_75639_1_);
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
}

