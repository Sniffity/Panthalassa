package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PanthalassaSchoolingGoal extends Goal {

    PanthalassaEntity panthalassaEntity;
    ISchoolable panthalassaSchoolableEntity;
    float schoolSpeed;
    float schoolMaxSize;
    float schoolAvoidRadius;
    List<PanthalassaEntity> school;
    List<PanthalassaEntity> reducedSchool;
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
        this.school = panthalassaEntity.level.getEntitiesOfClass(panthalassaEntity.getClass(), panthalassaEntity.getBoundingBox().inflate(20));
        if (school.size()<=1){
            return  false;
        }
        if (!this.panthalassaEntity.isInWater()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (!panthalassaEntity.isInWater()) {
            return false;
        }
        if (panthalassaEntity.getTarget() != null) {
            return false;
        }
        if (this.leader.isDeadOrDying() || leader == null) {
            return false;
        }
        return true;
    }


    @Override
    public void start() {
        processLeader(school);

        //Once we have set all the leaders for this group of Kronosaurus...
        //Reduce the school to 3 Kronosaurus, 1 of which is leader...
        reducedSchool = new ArrayList<>();
        boolean leaderAdded = false;
        //Note that by this point, school will always have some Kronosaurus tagged as leader.
        for (int i = 0; i < school.size(); i++) {
            PanthalassaEntity testEntity = school.get(i);
            //If testEntity is a leader, and the school does not have a leader yet...
            if (((ISchoolable) testEntity).getIsLeader() && (!leaderAdded)) {
                //Add this as a leader...
                if (reducedSchool.size()<4) {
                    reducedSchool.add(testEntity);
                    leader = testEntity;
                    //And flag the fact that we have a leader already...
                    leaderAdded = true;
                }
            } else if (school.size()<3) {
                //Add only up to 2 non-leader Kronosaurus
                reducedSchool.add(testEntity);
            }
        }
    }


    @Override
    public void tick() {
        //Instantiate vectors we will be working with...
        Vector3d attract = new Vector3d(0, 0, 0);
        Vector3d repel = new Vector3d(0, 0, 0);
        Vector3d follow;
        Vector3d avoid = new Vector3d(0, 0, 0);
        Vector3d newMovement;

        //Remove this Kronosaurus from the school for operations...
        this.school.remove(panthalassaEntity);
        //Only carry out school operations if the school is not empty
        if (!this.school.isEmpty()) {
            //attract = (processAttract(school));
            //repel = (processRepel(school));
            //Follow is calculated considering the speed of all Kronosaurus, but the leader will not have follow applied
            follow = (processFollow(school));

            // Now normalize the vectors to get just the directions, and scale by the amount you want each force to have. These values will need fine-tuning
            //attract = attract.normalize().scale(0.05F);
            //repel = repel.normalize().scale(0.5);
            follow = follow.normalize().scale(1.0);

            //Only those who are not leaders will have the follow component applied to them.
            //Hence, the leader will retain its previous movement, the rest will adjust to leader.
            //Of note: Leader is following RandomSwimmingAI and utilizing SwimmingHelper - it'll avoid obstacles on its own...
            //Followers must still have some method for avoiding obstacles.
            if (!this.panthalassaSchoolableEntity.getIsLeader()) {
                newMovement = panthalassaEntity.getDeltaMovement().add(attract).add(repel).add(follow);
            } else {
                newMovement = panthalassaEntity.getDeltaMovement().add(attract).add(repel);
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
            /*
            if (leaderKronosaurus != null) {
                if (!this.getIsLeader()) {
                    double d0 = (this.position().x+(this.getDeltaMovement().x));
                    double d1 = (this.position().z+(this.getDeltaMovement().z));
                    float f = (float) (MathHelper.atan2(d1, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                    if (this.getTarget() != null){
                        this.yRot = this.rotlerp(this.yRot, f, PASSIVE_ANGLE);
                    } else {
                        this.yRot = this.rotlerp(this.yRot, f, AGGRO_ANGLE);
                    }
                    this.yBodyRot = this.yRot;
                    this.yHeadRot = this.yRot;
                }
            }

             */

            if (this.leader != null) {
                if (!panthalassaSchoolableEntity.getIsLeader()) {
                    panthalassaEntity.lookAt(EntityAnchorArgument.Type.EYES, panthalassaEntity.position().add(panthalassaEntity.getDeltaMovement()));
                }
            }
        }
    }


    protected void processLeader(List<PanthalassaEntity> school) {
        //Create a new list with all the nearby Kronosaurus currently tagged as leaders
        float size = school.size();
        List<PanthalassaEntity> leaders = new ArrayList<>();
        for (int i = 0; i < school.size(); i++) {
            PanthalassaEntity testEntity = school.get(i);
            if (((ISchoolable) testEntity).getIsLeader()) {
                leaders.add(testEntity);
            }
        }
        //If we have more than one leader for every 3 Kronosaurus...
        //Reduce all leaders that exceed this cap...
        if (leaders.size() > Math.ceil(size/3.0F)) {
            int leadersSize = leaders.size();
            //Get the number of schools...
            int groups = (int) Math.ceil(size / 3.0F);
            //Get the amout of excess leaders...
            int excessLeaders = -(leadersSize - groups);
            //Loop over the school...

            for (int i = (int) (Math.ceil(size / 3.0F)); i < school.size(); i++) {
                ISchoolable testEntity = (ISchoolable) school.get(i);
                //Each time we find a Kronosaurus which is a leader, remove its leader status..

                if (testEntity.getIsLeader()) {
                    testEntity.setLeader(false);
                    //And reduce the amount of excess leaders by 1.
                    excessLeaders = --excessLeaders;
                    //If we no longer have excess leaders, stop the loop.
                    if (excessLeaders<=0){
                        break;
                    }
                }

            }

        }
        //If we have less than one leader for every 3 Kronosaurus....
        else if (leaders.size() < Math.ceil(size / 3.0F)) {
            //Get the number of leaders...
            int leadersSize = leaders.size();
            //Get the number of schools...
            int groups = (int) Math.ceil(size / 3.0F);
            //Get the amout of leaders we need...
            int requiredLeaders = -(leadersSize - groups);
            //Loop over the school...
            for (int i = 0; i < school.size(); i++) {
                ISchoolable potentialLeader = (ISchoolable) (school.get(i));
                //Each time we find a Kronosaurus which is not a leader, set it to be a leader..
                if (!potentialLeader.getIsLeader()) {
                    potentialLeader.setLeader(true);
                    //And reduce the amount of required leaders by 1.
                    requiredLeaders = --requiredLeaders;
                    //If we hit the amount of required leaders, stop the loop.
                    if (requiredLeaders <= 0) {
                        break;
                    }
                }
            }
        }
        //Other possibility is that leaders.size() = Math.ceil(size/4.0F), in which case we have exactly one leader for every 3 Kronosaurus.
        //Do nothing in this case.
    }



    protected Vector3d processAttract(List<PanthalassaEntity> school) {
        //Calculate the average position of the school, excluding THIS Kronosaurus
        int size = school.size();
        Vector3d positionVector = new Vector3d(0, 0, 0);
        for (int i = 0; i < size && i < schoolMaxSize; i++) {
            PanthalassaEntity testEntity = school.get(i);
            positionVector = positionVector.add(testEntity.position());
        }
        Vector3d averagePos = positionVector.scale(1.0f / (float) size);
        //Calculate a new vector... pointing from THIS Kronosaurus, to the center of the school.
        //This vector will be scaled by SCHOOL_SPEED, which is a factor that scales all school "operations"
        Vector3d target = averagePos.subtract(panthalassaEntity.position()).normalize().scale(schoolMaxSize);
        //From this vector, subtract the velocity vector of THIS Kronosaurus...
        //This creates a new Vector, attraction, that will attempt to adjust position towards center of group...
        //Taking the speed of THIS Kronosaurus into account
        return target.subtract(panthalassaEntity.getDeltaMovement());
    }

    protected Vector3d processRepel(List<PanthalassaEntity> school) {

        Vector3d separation = new Vector3d(0, 0, 0);
        //Create a new list to store Kronosaurus that are too close to THIS Kronosaurus....
        List<PanthalassaEntity> closeEntities = new ArrayList<>();
        int schoolSize = school.size();
        for (int i = 0; i < schoolSize && i < 4; i++) {
            //Get the position of each Kronosaurus in school...
            PanthalassaEntity testEntity = school.get(i);
            float distanceToEntity = (float) panthalassaEntity.position().subtract(testEntity.position()).length();
            //Add those that Kronosaurus are too close to THIS Kronosaurus to the list specified above.
            if (distanceToEntity < schoolAvoidRadius) {
                closeEntities.add(testEntity);
            }
        }
        //If there are no close Kronosaurus, return a zero-vector. No adjustment will be made for avoidance.
        if (closeEntities.isEmpty()) {
            return new Vector3d(0, 0, 0);
        }

        //Given that the list is not empty...
        int closeSize = closeEntities.size();
        for (int i = 0; i < closeSize; i++) {
            PanthalassaEntity testEntity = school.get(i);
            //For each Kronosaurus create a vector...
            //This vector points from the CURRENT Kronosaurus being analyzed to THIS Kronosaurus
            Vector3d difference = panthalassaEntity.position().subtract(testEntity.position());
            //For this vector, normalize it to get a direction..
            //Then scale it down, its length will depend inversely on the distance to THIS Kronosaurus
            separation = separation.add(difference.normalize().scale(1.0f / difference.length()));
        }
        //All of these vectors have been added together...
        //Each Kronosaurus analyzed contributed to these vectors..
        //The resulting vector will be one that pushes THIS Kronosaurus away from all OTHER Kronosaurus
        //Get an average to scale this vector...
        separation = separation.scale(1.0f / closeSize);
        //This vector will be scaled by SCHOOL_SPEED, which is a factor that scales all school "operations"
        Vector3d target = separation.normalize().scale(schoolSpeed);
        //From this vector, subtract the velocity vector of the Kronosaurus...

        //This creates a new Vector, attraction, that will attempt to adjust position away from center of group...
        //Taking the speed of THIS Kronosaurus into account
        return target.subtract(panthalassaEntity.getDeltaMovement());
    }


    protected Vector3d processFollow(List<PanthalassaEntity> school) {
        //We will proceed to set the speed of the followers in school to match that of the leader...
        //Once again, by this point, we only have a single leader, each Kronosaurus will search the leader closes to it
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
        //From this vector, subtract the velocity vector of the Kronosaurus...

        //This creates a new Vector, follow, that will attempt to adjust speed of THIS Kronosaurus to the speed of the group...
        //Taking the speed of THIS Kronosaurus into account
        return target.subtract(panthalassaEntity.getDeltaMovement());
    }


    protected Vector3d processAvoid() {
        //Once all school vectors have been calculated perform a check around it, 7 block radius.
        AxisAlignedBB searchArea = new AxisAlignedBB(panthalassaEntity.getX() - 7, panthalassaEntity.getY() - 7, panthalassaEntity.getZ() - 7, panthalassaEntity.getX() + 7, panthalassaEntity.getY() + 7, panthalassaEntity.getZ() + 7);
        //Filter only the blocks that canOcclude, solid blocks...
        Set<BlockPos> set = BlockPos.betweenClosedStream(searchArea)
                .map(pos -> new BlockPos(pos))
                .filter(state -> (panthalassaEntity.level.getBlockState(state).canOcclude()))
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
                float distanceToPos = (float) panthalassaEntity.position().subtract(testPos.getX(), testPos.getY(), testPos.getZ()).length();
                if (distanceToPos < distanceToClosestPos) {
                    distanceToClosestPos = distanceToPos;
                    closestPos = new BlockPos(testPos.getX(), testPos.getY(), testPos.getZ());
                }
            }

            Vector3d targetAwayFromClosestPos = (panthalassaEntity.position()).subtract(closestPos.getX(), closestPos.getY(), closestPos.getZ());
            targetAwayFromClosestPos = targetAwayFromClosestPos.normalize();
            return targetAwayFromClosestPos.subtract(panthalassaEntity.getDeltaMovement());
        }
        return null;
    }

    @Override
    public void stop() {
        this.panthalassaEntity.setDeltaMovement(0,0,0);
        super.stop();
    }
}
