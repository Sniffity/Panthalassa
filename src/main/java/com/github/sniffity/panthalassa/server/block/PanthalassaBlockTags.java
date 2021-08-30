package com.github.sniffity.panthalassa.server.block;


import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import com.github.sniffity.panthalassa.server.registry.PanthalassaTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class PanthalassaBlockTags extends BlockTagsProvider {

    public PanthalassaBlockTags(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Panthalassa.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Panthalassa Block Tags";
    }

    @Override
    protected void addTags() {
        tag(PanthalassaTags.Blocks.VALID_PORTAL_POSITION).add(PanthalassaBlocks.PANTHALASSA_COARSE_STONE.get(), PanthalassaBlocks.PANTHALASSA_STONE.get(), PanthalassaBlocks.PANTHALASSA_LOOSE_STONE.get());
        tag(PanthalassaTags.Blocks.VALID_PRIMORDIAL_STALK_POSITION).add(PanthalassaBlocks.PANTHALASSA_OVERGROWN_SAND.get(), PanthalassaBlocks.PANTHALASSA_SAND.get());
    }
}
