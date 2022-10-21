package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.gen.structure.StructurePanthalassaLaboratory;
import com.github.sniffity.panthalassa.server.world.gen.structure.StructurePanthalassaObservatory;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class PanthalassaStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, Panthalassa.MODID);

    public static final RegistryObject<StructureType<StructurePanthalassaLaboratory>> PANTHALASSA_LABORATORY =
            STRUCTURES.register("panthalassa_laboratory", () -> typingFix(StructurePanthalassaLaboratory.CODEC));

    public static final RegistryObject<StructureType<StructurePanthalassaObservatory>> PANTHALASSA_OBSERVATORY =
            STRUCTURES.register("panthalassa_observatory", () -> typingFix(StructurePanthalassaObservatory.CODEC));

    public static <T extends Structure> StructureType<T> typingFix(Codec<T> codec){
        return () -> codec;
    }
}