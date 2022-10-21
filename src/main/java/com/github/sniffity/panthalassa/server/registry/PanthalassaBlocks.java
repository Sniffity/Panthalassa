package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;
import com.github.sniffity.panthalassa.server.block.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class PanthalassaBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Panthalassa.MODID);

    public static final RegistryObject<Block> PORTAL_FRAME = register("panthalassa_portal_frame",
            BlockPortalFrame::new, new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> PORTAL = register("panthalassa_portal",
            BlockPortal::new, new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> PRIMORDIAL_STALK = register("primordial_stalk",
            () -> new BlockPrimordialStalk(BlockBehaviour.Properties.of(Material.PLANT, MaterialColor.COLOR_GREEN)
                    .strength(0.6f, 0.6f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<GrowingPlantHeadBlock> KRETHROSS = register("krethross",
            () -> new BlockKrethrossTop(BlockBehaviour.Properties.of(Material.WATER_PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .lightLevel((n) -> 15)
                    .sound(SoundType.WET_GRASS)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<GrowingPlantBlock> KRETHROSS_PLANT = register("krethross_plant",
            () -> new BlockKrethross(BlockBehaviour.Properties.of(Material.WATER_PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .lightLevel((n) -> 15)
                    .sound(SoundType.WET_GRASS)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<BushBlock> FROSTGRASS = register("frostgrass",
            () -> new BlockFrostgrass(BlockBehaviour.Properties.of(Material.WATER_PLANT)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.WET_GRASS)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> PANTHALASSA_SOIL = register("panthalassa_soil",
            () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> PANTHALASSA_COARSE_SOIL = register("panthalassa_coarse_soil",
            () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> PANTHALASSA_LOOSE_SOIL = register("panthalassa_loose_soil",
            () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.DIRT)
                    .strength(0.5F)
                    .sound(SoundType.GRAVEL)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> PANTHALASSA_STONE = register("panthalassa_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.STONE)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> PANTHALASSA_COARSE_STONE = register("panthalassa_coarse_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.STONE)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> PANTHALASSA_LOOSE_STONE = register("panthalassa_loose_stone",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.STONE)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> PANTHALASSA_ROCK = register("panthalassa_rock",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.STONE)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> ANCIENT_ROCK = register("ancient_rock",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(1.5F, 6.0F)
                    .sound(SoundType.STONE)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> PANTHALASSA_SAND = register("panthalassa_sand",
            () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.SAND)
                    .strength(0.5F)
                    .sound(SoundType.SAND)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> PANTHALASSA_OVERGROWN_SAND = register("panthalassa_overgrown_sand",
            () -> new Block(BlockBehaviour.Properties.of(Material.DIRT, MaterialColor.SAND)
                    .strength(0.5F)
                    .sound(SoundType.SAND)), new Item.Properties().tab(PanthalassaItemGroup.GROUP));


    public static final RegistryObject<Block> LIGHT_AIR = BLOCKS.register("light_air",
            () -> new AirBlock(BlockBehaviour.Properties.of(Material.AIR)
                    .noCollission()
                    .noLootTable()
                    .air()
                    .lightLevel((n) -> 15)));

    public static final RegistryObject<Block> LIGHT_WATER = BLOCKS.register("light_water",
            () -> new LiquidBlock(() -> Fluids.WATER, BlockBehaviour.Properties.of(Material.WATER)
                    .noCollission()
                    .noLootTable()
                    .lightLevel((n) -> 15)));

    public static final RegistryObject<Block> PANTHALASSA_WATER = BLOCKS.register("panthalassa_water",
            () -> new LiquidBlock(() -> Fluids.WATER, BlockBehaviour.Properties.of(Material.WATER)
                    .noCollission()
                    .noLootTable()));

    public static final RegistryObject<Block> PRESSURE_EQUALIZER = register("pressure_equalizer",
            BlockPressureEqualizer::new, new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    public static final RegistryObject<Block> HYDROTHERMAL_VENT = register("hydrothermal_vent",
            BlockHydrothermalVent::new, new Item.Properties().tab(PanthalassaItemGroup.GROUP));

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> supplier, Item.Properties properties) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        PanthalassaItems.ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        return block;
    }
}

