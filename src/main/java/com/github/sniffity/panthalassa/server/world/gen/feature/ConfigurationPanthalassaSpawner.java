package com.github.sniffity.panthalassa.server.world.gen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraftforge.registries.ForgeRegistries;

public class ConfigurationPanthalassaSpawner implements FeatureConfiguration {
    public static final Codec<ConfigurationPanthalassaSpawner> CODEC = RecordCodecBuilder.create((p_151822_) -> {
        return p_151822_.group(
                ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("type").forGetter((p_151826_) -> {
                    return p_151826_.type;
                }),
                Codec.INT.fieldOf("spawnWeight").forGetter((p_424242) -> {
                    return p_424242.spawnWeight;
                }),
                Codec.INT.fieldOf("minCount").forGetter((p_151824_) -> {
                    return p_151824_.minCount;
                }), Codec.INT.fieldOf("maxCount").forGetter((p_151820_) -> {
                    return p_151820_.maxCount;
                })).apply(p_151822_, ConfigurationPanthalassaSpawner::new);
    });
    public final EntityType<?> type;
    public final int spawnWeight;
    public final int minCount;
    public final int maxCount;

    public ConfigurationPanthalassaSpawner(EntityType<?> p_151815_, int p_151816_, int p_151817_, int p_151818_) {
        this.type = p_151815_.getCategory() == MobCategory.MISC ? EntityType.PIG : p_151815_;
        this.spawnWeight = p_151816_;
        this.minCount = p_151817_;
        this.maxCount = p_151818_;
    }
    public String toString() { return EntityType.getKey(this.type) + "*(" + this.minCount + "-" + this.maxCount + "):" + this.spawnWeight;

    }
}
