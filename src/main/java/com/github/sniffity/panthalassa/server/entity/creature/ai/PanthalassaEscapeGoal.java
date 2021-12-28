package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.block.BlockPortalBlockEntity;
import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.server.registry.PanthalassaPOI;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;

import java.util.EnumSet;
import java.util.Optional;

public class PanthalassaEscapeGoal extends Goal {

    protected final PanthalassaEntity creature;
    protected final double speed;
    private BlockPos targetPos;
    protected float tickCounter = 0;

    public PanthalassaEscapeGoal(PanthalassaEntity creature, double speed) {
        this.creature = creature;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (creature.level.dimension() != PanthalassaDimension.PANTHALASSA) {
            return false;
        }
        if (this.creature.isVehicle()) {
            return false;
        } else {
            int searchRadius = 10;
            PoiManager pointofinterestmanager = ((ServerLevel) creature.level).getPoiManager();
            Optional<PoiRecord> portalPOI = pointofinterestmanager.getInRange(
                    (pointOfInterestType) -> pointOfInterestType == PanthalassaPOI.PANTHALASSA_POI_PORTAL.get(),
                    creature.blockPosition(),
                    searchRadius,
                    PoiManager.Occupancy.ANY)
                    .findFirst();

            if (portalPOI.isPresent()) {
                BlockPortalBlockEntity tempTE = getPortalTE(creature.level, portalPOI.get().getPos());
                if (tempTE != null) {
                    BlockPortalBlockEntity centerTE = getPortalTE(creature.level, portalPOI.get().getPos().subtract(tempTE.offsetFromCenter));
                    if (centerTE != null) {
                        this.targetPos = centerTE.getBlockPos();
                        return true;
                    }
                } else {
                    this.targetPos = null;
                    return false;
                }
            }
            this.targetPos = null;
            return false;
        }
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
        this.creature.getNavigation().moveTo(this.targetPos.getX(),this.targetPos.getY(),this.targetPos.getZ(), this.speed);
    }

    @Override
    public void tick(){
        if (creature.distanceToSqr(targetPos.getX(),targetPos.getY(),targetPos.getZ())<50) {
            Vec3 creaturePos = new Vec3(creature.getX(),creature.getY(),creature.getZ());
            Vec3 target = new Vec3(targetPos.getX(),targetPos.getY(),targetPos.getZ());
            Vec3 trajectory = target.subtract(creaturePos).normalize();
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

    private BlockPortalBlockEntity getPortalTE(LevelAccessor world, BlockPos pos) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof BlockPortalBlockEntity) {
            return (BlockPortalBlockEntity) tileEntity;
        }
        return null;
    }

}