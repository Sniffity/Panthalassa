package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.block.BlockPortalTileEntity;
import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaTileEntities {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Panthalassa.MODID);
    public static final RegistryObject<TileEntityType<BlockPortalTileEntity>> PORTAL_TE = TILE_ENTITY_TYPES.register("portal", () -> new TileEntityType<>(BlockPortalTileEntity::new, Sets.newHashSet(PanthalassaBlocks.PORTAL.get()), null));
}
