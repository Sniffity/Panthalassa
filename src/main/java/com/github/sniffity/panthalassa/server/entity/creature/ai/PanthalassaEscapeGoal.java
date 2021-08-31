package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.server.registry.PanthalassaPOI;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PanthalassaEscapeGoal extends Goal {

    protected final PanthalassaEntity creature;
    protected double x;
    protected double y;
    protected double z;
    protected final double speed;
    private BlockPos targetPos;
    protected float tickCounter = 0;

    public PanthalassaEscapeGoal(PanthalassaEntity creature, double speed) {
        this.creature = creature;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (creature.level.dimension() != PanthalassaDimension.PANTHALASSA){
            return false;
        }
        if (this.creature.isVehicle()) {
            return false;
        } else {
            int searchRadius = 10;
            PointOfInterestManager pointofinterestmanager = ((ServerWorld)creature.level).getPoiManager();
            Optional<PointOfInterest> portalPOI = pointofinterestmanager.getInRange(
                    (pointOfInterestType) -> pointOfInterestType == PanthalassaPOI.PANTHALASSA_POI_PORTAL.get(),
                    creature.blockPosition(),
                    searchRadius,
                    PointOfInterestManager.Status.ANY)
                    .findFirst();

            if(portalPOI.isPresent()) {
                this.targetPos = portalPOI.get().getPos();
            }
            else {
                this.targetPos = null;
                return false;
            }

            Vector3d vector3d = this.getPosition();
            if (vector3d == null) {
                return false;
            }

            this.x = vector3d.x;
            this.y = vector3d.y;
            this.z = vector3d.z;
            return true;
        }
    }

    @Nullable
    protected Vector3d getPosition() {
        Vector3d vector =  new Vector3d(targetPos.getX(), targetPos.getY(),targetPos.getZ());
        return vector;
    }

    @Override
    public boolean canContinueToUse() {
        if (creature.isVehicle()){
            return false;
        }
        if (creature.level.dimension() != PanthalassaDimension.PANTHALASSA) {
            return false;
        }
        if (tickCounter > 80) {
            return false;
        }

        return true;
    }

    @Override
    public void start() {
        this.creature.getNavigation().moveTo(this.x, this.y, this.z, this.speed);
    }

    @Override
    public void tick(){
        if (creature.distanceToSqr(targetPos.getX(),targetPos.getY(),targetPos.getZ())<20) {
            Vector3d creaturePos = new Vector3d(creature.getX(),creature.getY(),creature.getZ());
            Vector3d target = new Vector3d(targetPos.getX(),targetPos.getY(),targetPos.getZ());
            Vector3d trajectory = target.subtract(creaturePos).normalize();
            creature.setDeltaMovement(creature.getDeltaMovement().add(trajectory.x,trajectory.y,trajectory.z));
        }
        tickCounter = tickCounter ++;
    }

    @Override
    public void stop() {
        this.creature.getNavigation().stop();
        tickCounter = 0;
        super.stop();
    }
}