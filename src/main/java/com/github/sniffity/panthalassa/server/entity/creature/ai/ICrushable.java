package com.github.sniffity.panthalassa.server.entity.creature.ai;

public interface ICrushable

{
    void setCrushCooldown(float breachCooldown);

    float getCrushCooldown();

    void setCrushingState(boolean isCrushing);

    boolean getCrushingState();

    void setCrushing(boolean breaching);

    boolean getCrushing();
}
