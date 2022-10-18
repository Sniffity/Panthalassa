package com.github.sniffity.panthalassa.mixin;

import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Panthalassa Mod - Class: StructureProcessorAccessor <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed by TelepathicGrunt, for his RepurposedStructures mod.
 * All credit goes to him.
 */

@Mixin(StructureProcessor.class)
public interface StructureProcessorAccessor {
    @Invoker("getType")
    StructureProcessorType<?> callGetType();
}