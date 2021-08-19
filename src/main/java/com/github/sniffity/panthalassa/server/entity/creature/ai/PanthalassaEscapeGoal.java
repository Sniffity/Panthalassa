package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.server.registry.PanthalassaDimension;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import javax.annotation.Nullable;
import java.util.EnumSet;
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
            AxisAlignedBB searchArea = new AxisAlignedBB(creature.getX() - 20, creature.getY() - 20, creature.getZ() - 20, creature.getX() + 20, creature.getY() + 20, creature.getZ() + 20);
            Set<BlockPos> set = BlockPos.betweenClosedStream(searchArea)
                    .map(pos -> new BlockPos(pos))
                    .filter(state -> (creature.level.getBlockState(state) == PanthalassaBlocks.PORTAL.get().defaultBlockState()))
                    .collect(Collectors.toSet());

            for (BlockPos portalPos : set) {
                if (creature.level.getBlockState(portalPos.north(4)) == PanthalassaBlocks.PORTAL.get().defaultBlockState()
                        && creature.level.getBlockState(portalPos.south(4)) == PanthalassaBlocks.PORTAL.get().defaultBlockState()
                        && creature.level.getBlockState(portalPos.east(4)) == PanthalassaBlocks.PORTAL.get().defaultBlockState()
                        && creature.level.getBlockState(portalPos.west(4)) == PanthalassaBlocks.PORTAL.get().defaultBlockState()) {
                    this.targetPos = portalPos;
                    break;
                } else {
                    this.targetPos = null;
                }
            }

            if (targetPos == null) {
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