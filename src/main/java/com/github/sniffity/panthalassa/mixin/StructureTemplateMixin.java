package com.github.sniffity.panthalassa.mixin;

import com.github.sniffity.panthalassa.server.registry.PanthalassaProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Random;

/**
 * Panthalassa Mod - Class: StructureTemplateMixin <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed by TelepathicGrunt, for his RepurposedStructures mod.
 * All credit goes to him.
 */

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin {

    @Inject(
            method = "placeInWorld(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;Ljava/util/Random;I)Z",
            at = @At(value = "HEAD")
    )
    private void preventAutoWaterLogging(ServerLevelAccessor pServerLevel, BlockPos pPos,
                                                              BlockPos p_74539_, StructurePlaceSettings pSettings,
                                                              Random pRandom, int pFlags, CallbackInfoReturnable<Boolean> cir) {
        if(pSettings.getProcessors().stream().anyMatch(processor ->
                ((StructureProcessorAccessor)processor).callGetType() == PanthalassaProcessors.WATERLOGGING_FIX_PROCESSOR)) {
            pSettings.setKeepLiquids(false);
        }
    }
}