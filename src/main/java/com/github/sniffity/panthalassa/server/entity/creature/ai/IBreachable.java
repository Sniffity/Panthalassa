package com.github.sniffity.panthalassa.server.entity.creature.ai;

import com.github.sniffity.panthalassa.server.entity.creature.PanthalassaEntity;
import net.minecraft.entity.monster.IMob;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public interface IBreachable extends IMob

{
    void setIsBreaching(boolean breaching);

    boolean getIsBreaching();

    void setBreachCooldown(float breachCooldown);

    float getBreachCooldown();
}

