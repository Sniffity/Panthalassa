package com.github.sniffity.panthalassa.server.entity.creature.ai;


public interface IBreachable

{
    void setBreachState(boolean breaching);

    boolean getBreachState();

    void setBreaching(boolean breaching);

    boolean getBreaching();

    void setBreachCooldown(float breachCooldown);

    float getBreachCooldown();
}

