package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.entity.KronosaurusEntity;
import javafx.util.Builder;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Panthalassa.MOD_ID);

    //Unsure about the following line of code.

    public static final RegistryObject<EntityType<KronosaurusEntity>> KRONOSAURUS = ENTITY_TYPES.register("kronosaurus",
            () -> EntityType.Builder.of(KronosaurusEntity::new, EntityClassification.CREATURE)
                    .sized(1.0F,1.0f)
                    .build(new ResourceLocation(Panthalassa.MOD_ID, "kronosaurus").toString()));
}





