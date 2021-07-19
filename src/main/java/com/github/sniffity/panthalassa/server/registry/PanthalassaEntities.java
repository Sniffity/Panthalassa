package com.github.sniffity.panthalassa.server.registry;


import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.KronosaurusEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public enum PanthalassaEntities {
    INSTANCE;

    public static EntityType<KronosaurusEntity> KRONOSAURUS;

    public static void register() {
        KRONOSAURUS = register("kronosaurus", EntityType.Builder.of(KronosaurusEntity::new, EntityClassification.CREATURE).sized(1.0F, 1.0F));

        ForgeRegistries.ENTITIES.registerAll(
                KRONOSAURUS
        );

    }
    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceLocation regName = new ResourceLocation(Panthalassa.MOD_ID, name);
        return (EntityType<T>) builder.build(name).setRegistryName(regName);
    }

    //REVIEW!

    @SuppressWarnings("deprecation")
    public static void initializeAttributes() {
        GlobalEntityTypeAttributes.put(KRONOSAURUS, KronosaurusEntity.createMobAttributes().build());

    }

    //REVIEW!


}






