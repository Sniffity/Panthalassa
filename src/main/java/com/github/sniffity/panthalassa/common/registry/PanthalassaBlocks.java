package com.github.sniffity.panthalassa.common.registry;

import com.github.sniffity.panthalassa.Panthalassa;

import com.github.sniffity.panthalassa.common.block.PanthalassaPortalBlock;
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

    public static final RegistryObject<Block> PORTAL = BLOCKS.register("panthalassa_portal",
            PanthalassaPortalBlock::new);

    public static final RegistryObject<Block> PORTAL_FRAME = BLOCKS.register("portal frame",
            () -> new Block(AbstractBlock.Properties.create(Material.EARTH, MaterialColor.BROWN)
                    .hardnessAndResistance(2f, 6f).harvestTool(ToolType.SHOVEL).harvestLevel(2)
                    .sound(SoundType.GROUND)));

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

    public static final RegistryObject<Block> PANTHALASSA_STONE = BLOCKS.register("panthalassa_stone",
            () -> new Block(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GRAY)
                    .hardnessAndResistance(2f, 6f).harvestTool(ToolType.PICKAXE).harvestLevel(3)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> PANTHALASSA_COARSE_STONE = BLOCKS.register("panthalassa_coarse_stone",
            () -> new Block(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GRAY)
                    .hardnessAndResistance(2f, 6f).harvestTool(ToolType.PICKAXE).harvestLevel(3)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> PANTHALASSA_LOOSE_STONE = BLOCKS.register("panthalassa_loose_stone",
            () -> new Block(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GRAY)
                    .hardnessAndResistance(2f, 6f).harvestTool(ToolType.PICKAXE).harvestLevel(3)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> PANTHALASSA_ROCK = BLOCKS.register("panthalassa_rock",
            () -> new Block(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GRAY)
                    .hardnessAndResistance(3f, 6f).harvestTool(ToolType.PICKAXE).harvestLevel(3)
                    .sound(SoundType.STONE)));



}
