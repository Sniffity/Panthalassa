package com.github.sniffity.panthalassa.server.entity.display;

import com.github.sniffity.panthalassa.server.registry.PanthalassaItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class DisplayGiantOrthoconeShell extends PanthalassaDisplayEntity implements IAnimatable{

    private AnimationFactory factory = new AnimationFactory(this);

    public DisplayGiantOrthoconeShell(EntityType<? extends PanthalassaDisplayEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.turnIntoItem();
        return super.hurt(source, amount);
    }

    public void turnIntoItem() {
        if (isRemoved())
            return;
        this.discard();
        ItemStack stack = new ItemStack(PanthalassaItems.GIANT_ORTHOCONE_SHELL_DISPLAY.get(), 1);
        if (!this.level.isClientSide)
            this.spawnAtLocation(stack, 0.0F);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.giant_orthocone_shell.hold", true));
        return PlayState.CONTINUE;
    }
}


