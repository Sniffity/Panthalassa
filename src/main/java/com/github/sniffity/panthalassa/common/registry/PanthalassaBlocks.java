package com.github.sniffity.panthalassa.common.registry;

import com.github.sniffity.panthalassa.Panthalassa;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class PanthalassaBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Panthalassa.MODID);

    public static final RegistryObject<Block> PANTHALASSA_SOIL = BLOCKS.register("panthalassa_soil",
            () -> new Block(AbstractBlock.Properties.create(Material.EARTH, MaterialColor.BROWN)
                    .hardnessAndResistance(2f, 6f).harvestTool(ToolType.SHOVEL).harvestLevel(2)
                    .sound(SoundType.GROUND)));

    public static final RegistryObject<Block> PANTHALASSA_COARSE_SOIL = BLOCKS.register("panthalassa_coarse_soil",
            () -> new Block(AbstractBlock.Properties.create(Material.EARTH, MaterialColor.BROWN)
                    .hardnessAndResistance(2f, 6f).harvestTool(ToolType.SHOVEL).harvestLevel(2)
                    .sound(SoundType.GROUND)));

    public static final RegistryObject<Block> PANTHALASSA_LOOSE_SOIL = BLOCKS.register("panthalassa_loose_soil",
            () -> new Block(AbstractBlock.Properties.create(Material.EARTH, MaterialColor.BROWN)
                    .hardnessAndResistance(2f, 6f).harvestTool(ToolType.SHOVEL).harvestLevel(2)
                    .sound(SoundType.GROUND)));

    public static final RegistryObject<Block> PANTHALASSA_ROCKS = BLOCKS.register("panthalassa_rocks",
            () -> new Block(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GRAY)
                    .hardnessAndResistance(2f, 6f).harvestTool(ToolType.PICKAXE).harvestLevel(3)
                    .sound(SoundType.STONE)));
}
