package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.block.BlockPortalBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PanthalassaBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Panthalassa.MODID);
    public static final RegistryObject<BlockEntityType<BlockPortalBlockEntity>> PORTAL_TE = BLOCK_ENTITY_TYPES.register(
            "portal", () -> BlockEntityType.Builder.of(BlockPortalBlockEntity::new, PanthalassaBlocks.PORTAL.get()).build(null));
}
