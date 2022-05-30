package com.github.sniffity.panthalassa.server.entity.creature.ai;

public interface IHungry {
    void setHungerCooldown(float hungerCooldown);

    float getHungerCooldown();
}