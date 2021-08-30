package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public class PanthalassaTags {

    public static class Blocks {

        public static final ITag.INamedTag<Block> VALID_PORTAL_POSITION = tag("valid_portal_position");
        public static final ITag.INamedTag<Block> VALID_PRIMORDIAL_STALK_POSITION = tag("valid_primordial_stalk_position");

        private static ITag.INamedTag<Block> tag(String name) {
            return BlockTags.createOptional(new ResourceLocation(Panthalassa.MODID, name));
        }
    }
}
