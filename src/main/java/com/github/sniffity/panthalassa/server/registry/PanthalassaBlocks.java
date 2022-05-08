package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;

import com.github.sniffity.panthalassa.server.block.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;

public class PanthalassaBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Panthalassa.MODID);

    public static final RegistryObject<Block> PORTAL_FRAME = BLOCKS.register("panthalassa_portal_frame",
            BlockPortalFrame::new);

    public static final RegistryObject<Block> PORTAL = BLOCKS.register("panthalassa_portal",
            BlockPortal::new);

    public static final RegistryObject<Block> PRIMORDIAL_STALK = BLOCKS.register("primordial_stalk",
            () -> new BlockPrimordialStalk(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.COLOR_GREEN)
                    .strength(0.6f, 0.6f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));


    public static final RegistryObject<Block> PANTHALASSA_SOIL = BLOCKS.register("panthalassa_soil",
            () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL)));

    public static final RegistryObject<Block> PANTHALASSA_COARSE_SOIL = BLOCKS.register("panthalassa_coarse_soil",
            () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL)));

    public static final RegistryObject<Block> PANTHALASSA_LOOSE_SOIL = BLOCKS.register("panthalassa_loose_soil",
            () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL)));

    public static final RegistryObject<Block> PANTHALASSA_STONE = BLOCKS.register("panthalassa_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> PANTHALASSA_COARSE_STONE = BLOCKS.register("panthalassa_coarse_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> PANTHALASSA_LOOSE_STONE = BLOCKS.register("panthalassa_loose_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> PANTHALASSA_ROCK = BLOCKS.register("panthalassa_rock",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> ANCIENT_ROCK = BLOCKS.register("ancient_rock",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.STONE)));

    public static final RegistryObject<Block> PANTHALASSA_SAND = BLOCKS.register("panthalassa_sand",
            () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.SAND)
                    .strength(0.5F)
                    .sound(SoundType.SAND)));

    public static final RegistryObject<Block> PANTHALASSA_OVERGROWN_SAND = BLOCKS.register("panthalassa_overgrown_sand",
            () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.SAND)
                    .strength(0.5F)
                    .sound(SoundType.SAND)));

    public static final RegistryObject<Block> LIGHT_AIR = BLOCKS.register("light_air",
            () -> new AirBlock(BlockBehaviour.Properties.of(Material.AIR)
                    .noCollission()
                    .noDrops()
                    .air()
                    .lightLevel((n) -> 15)));

    public static final RegistryObject<Block> LIGHT_WATER = BLOCKS.register("light_water",
            () -> new LiquidBlock(() -> Fluids.WATER, BlockBehaviour.Properties.of(Material.WATER)
                    .noCollission()
                    .noDrops()
                    .lightLevel((n) -> 15)));

    public static final RegistryObject<Block> PANTHALASSA_WATER = BLOCKS.register("panthalassa_water",
            () -> new LiquidBlock(() -> Fluids.WATER, BlockBehaviour.Properties.of(Material.WATER)
                    .noCollission()
                    .noDrops()));

    public static final RegistryObject<GrowingPlantHeadBlock> KRETHROSS = BLOCKS.register("krethross",
            () -> new BlockKrethrossTop(BlockBehaviour.Properties.of(Material.WATER_PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .lightLevel((n) -> 15)
                    .sound(SoundType.WET_GRASS)));

    public static final RegistryObject<GrowingPlantBlock> KRETHROSS_PLANT = BLOCKS.register("krethross_plant",
            () -> new BlockKrethross(BlockBehaviour.Properties.of(Material.WATER_PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .lightLevel((n) -> 15)
                    .sound(SoundType.WET_GRASS)));

    public static final RegistryObject<BushBlock> FROSTGRASS = BLOCKS.register("frostgrass",
            () -> new BlockFrostgrass(BlockBehaviour.Properties.of(Material.WATER_PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.WET_GRASS)));

    public static final RegistryObject<Block> PRESSURE_EQUALIZER = BLOCKS.register("pressure_equalizer",
            BlockPressureEqualizer::new);
}

