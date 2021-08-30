package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;

import com.github.sniffity.panthalassa.server.block.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.Fluids;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PanthalassaBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Panthalassa.MODID);

    public static final RegistryObject<Block> PORTAL_FRAME = BLOCKS.register("panthalassa_portal_frame",
            BlockPortalFrame::new);

    public static final RegistryObject<Block> PORTAL = BLOCKS.register("panthalassa_portal",
            BlockPortal::new);

    public static final RegistryObject<Block> PRIMORDIAL_STALK = BLOCKS.register("primordial_stalk",
            () -> new BlockPrimordialStalk(AbstractBlock.Properties.of(Material.CORAL, MaterialColor.COLOR_GREEN)
                    .strength(0.6f, 0.6f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));


    public static final RegistryObject<Block> PANTHALASSA_SOIL = BLOCKS.register("panthalassa_soil",
            () -> new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_BROWN)
                    .strength(2f, 6f)
                    .harvestTool(ToolType.SHOVEL)
                    .harvestLevel(2)
                    .sound(SoundType.GRAVEL)));

    public static final RegistryObject<Block> PANTHALASSA_COARSE_SOIL = BLOCKS.register("panthalassa_coarse_soil",
            () -> new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_BROWN)
                    .strength(2f, 6f)
                    .harvestTool(ToolType.SHOVEL)
                    .harvestLevel(2)
                    .sound(SoundType.GRAVEL)));

    public static final RegistryObject<Block> PANTHALASSA_LOOSE_SOIL = BLOCKS.register("panthalassa_loose_soil",
            () -> new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_BROWN)
                    .strength(2f, 6f)
                    .harvestTool(ToolType.SHOVEL)
                    .harvestLevel(2)
                    .sound(SoundType.GRAVEL)));

    public static final RegistryObject<Block> PANTHALASSA_STONE = BLOCKS.register("panthalassa_stone",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY)
                    .strength(2f, 6f)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(3)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> PANTHALASSA_COARSE_STONE = BLOCKS.register("panthalassa_coarse_stone",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY)
                    .strength(2f, 6f)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(3)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> PANTHALASSA_LOOSE_STONE = BLOCKS.register("panthalassa_loose_stone",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY)
                    .strength(2f, 6f)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(3)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> PANTHALASSA_ROCK = BLOCKS.register("panthalassa_rock",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY)
                    .strength(3f, 6f)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(3)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> ANCIENT_ROCK = BLOCKS.register("ancient_rock",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY)
                    .strength(3f, 6f)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(3)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> PANTHALASSA_SAND = BLOCKS.register("panthalassa_sand",
            () -> new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_BROWN)
                    .strength(1f, 1f)
                    .harvestTool(ToolType.SHOVEL)
                    .harvestLevel(1)
                    .sound(SoundType.SAND)));

    public static final RegistryObject<Block> PANTHALASSA_OVERGROWN_SAND = BLOCKS.register("panthalassa_overgrown_sand",
            () -> new Block(AbstractBlock.Properties.of(Material.DIRT, MaterialColor.COLOR_BROWN)
                    .strength(1f, 1f)
                    .harvestTool(ToolType.SHOVEL)
                    .harvestLevel(1)
                    .sound(SoundType.SAND)));

    public static final RegistryObject<Block> LIGHT_AIR = BLOCKS.register("light_air",
            () -> new AirBlock(AbstractBlock.Properties.of(Material.AIR)
                    .noCollission()
                    .noDrops()
                    .air()
                    .lightLevel((n) -> 15)));

    public static final RegistryObject<Block> LIGHT_WATER = BLOCKS.register("light_water",
            () -> new FlowingFluidBlock(() -> Fluids.WATER, AbstractBlock.Properties.of(Material.WATER)
                    .noCollission()
                    .noDrops()
                    .lightLevel((n) -> 15)));

    public static final RegistryObject<Block> PANTHALASSA_WATER = BLOCKS.register("panthalassa_water",
            () -> new FlowingFluidBlock(() -> Fluids.WATER, AbstractBlock.Properties.of(Material.WATER)
                    .noCollission()
                    .noDrops()));

    public static final RegistryObject<AbstractTopPlantBlock> KRETHROSS = BLOCKS.register("krethross",
            () -> new BlockKrethrossTop(AbstractBlock.Properties.of(Material.WATER_PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .lightLevel((n) -> 15)
                    .sound(SoundType.WET_GRASS)));

    public static final RegistryObject<AbstractPlantBlock> KRETHROSS_PLANT = BLOCKS.register("krethross_plant",
            () -> new BlockKrethross(AbstractBlock.Properties.of(Material.WATER_PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .lightLevel((n) -> 15)
                    .sound(SoundType.WET_GRASS)));
}
