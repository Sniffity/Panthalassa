package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;

import com.github.sniffity.panthalassa.server.block.BlockLight;
import com.github.sniffity.panthalassa.server.block.BlockPortal;
import com.github.sniffity.panthalassa.server.block.BlockPortalFrame;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class PanthalassaBlocks {


    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Panthalassa.MODID);

    public static final RegistryObject<Block> PORTAL_FRAME = BLOCKS.register("panthalassa_portal_frame",
            BlockPortalFrame::new);

    public static final RegistryObject<Block> PORTAL = BLOCKS.register("panthalassa_portal",
            BlockPortal::new);

    public static final RegistryObject<Block> LIGHT = BLOCKS.register("light",
            BlockLight::new);


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


/*

   public static final Block AIR = register("air", new AirBlock(AbstractBlock.Properties.create(Material.AIR).doesNotBlockMovement().noDrops().setAir()));

    public static final RegistryObject<Block> LIGHT_WATER = BLOCKS.register("light_water",
            () -> new ForgeFlowingFluid(Fluids.WATER, AbstractBlock.Properties.create(Material.WATER).doesNotBlockMovement().noDrops()
                    .setLightLevel((n) -> 15)));
*/
}
