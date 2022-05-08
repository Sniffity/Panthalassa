package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlockEntities;
import com.github.sniffity.panthalassa.server.registry.PanthalassaEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class BlockPressureEqualizerBlockEntity extends BlockEntity {

    public BlockPressureEqualizerBlockEntity(BlockPos pos, BlockState state) {
        super(PanthalassaBlockEntities.PRESSURE_EQUALIZER_TE.get(), pos, state);
    }

    public void load(CompoundTag p_155437_) {
        super.load(p_155437_);
    }

    protected void saveAdditional(CompoundTag p_187495_) {
        super.saveAdditional(p_187495_);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    private static void applyEffects(Level level, BlockPos blockPos) {
        AABB aabb = (new AABB(blockPos.getX()-20, blockPos.getY()-20, blockPos.getZ()-20, blockPos.getX()+20, blockPos.getY()+20, blockPos.getZ()+20));
        List<Player> list = level.getEntitiesOfClass(Player.class, aabb);
        if (!list.isEmpty()) {
            for(Player player : list) {
                player.addEffect(new MobEffectInstance(PanthalassaEffects.CRUSH_RESIST.get(), 260, 0, true, true));
            }
        }
    }

    public void tick(Level p_155253_, BlockPos p_155254_) {
        applyEffects(p_155253_, p_155254_);
    }
}
