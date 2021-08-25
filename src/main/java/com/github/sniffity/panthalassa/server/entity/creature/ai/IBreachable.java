package com.github.sniffity.panthalassa.server.entity.creature.ai;


public interface IBreachable

{
    void setIsBreaching(boolean breaching);

    boolean getIsBreaching();

    void setBreachCooldown(float breachCooldown);

    float getBreachCooldown();
}

