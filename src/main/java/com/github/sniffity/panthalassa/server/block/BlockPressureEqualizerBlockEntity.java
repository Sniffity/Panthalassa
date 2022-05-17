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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class BlockPressureEqualizerBlockEntity extends BlockEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

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

    @SuppressWarnings("unchecked")
    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().transitionLengthTicks = 0;
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.pressure_equalizer.loop", true));
        return PlayState.CONTINUE;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

}
