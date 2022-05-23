package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.gen.structure.StructurePanthalassaLaboratory;
import com.github.sniffity.panthalassa.server.world.gen.structure.StructureXYZLaboratory;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PanthalassaStructures {
    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, Panthalassa.MODID);

    public static final RegistryObject<StructureFeature<?>> PANTHALASSA_LABORATORY = STRUCTURES.register("panthalassa_laboratory", StructurePanthalassaLaboratory::new);
    public static final RegistryObject<StructureFeature<?>> XYZ_LABORATORY = STRUCTURES.register("xyz_laboratory", StructureXYZLaboratory::new);

}