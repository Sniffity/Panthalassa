package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.world.processors.LaboratorySupportProcessor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class PanthalassaProcessors {

    public static StructureProcessorType<LaboratorySupportProcessor> LABORATORY_SUPPORT_PROCESSOR = () -> LaboratorySupportProcessor.CODEC;

    public static void registerProcessors() {
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Panthalassa.MODID, "laboratory_support"), LABORATORY_SUPPORT_PROCESSOR);
    }
}