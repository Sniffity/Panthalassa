package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;

import com.github.sniffity.panthalassa.server.item.*;
import com.github.sniffity.panthalassa.server.item.armor.ItemDivingSuit;
import com.github.sniffity.panthalassa.server.item.display.ItemArchelonShellDisplay;
import com.github.sniffity.panthalassa.server.item.display.ItemGiantOrthoconeShellDisplay;
import com.github.sniffity.panthalassa.server.item.display.ItemKronosaurusSkullDisplay;
import com.github.sniffity.panthalassa.server.item.vehicle.ItemAGII;
import com.github.sniffity.panthalassa.server.item.vehicle.ItemECSV;
import com.github.sniffity.panthalassa.server.item.vehicle.ItemMRSV;
import com.github.sniffity.panthalassa.server.item.vehicle.ItemPCSV;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PanthalassaItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Panthalassa.MODID);

	public static final RegistryObject<Item> KRONOSAURUS_SPAWN_EGG = ITEMS.register("kronosaurus_spawn_egg",
			() -> new ItemPanthalassaSpawnEgg(
					PanthalassaEntityTypes.KRONOSAURUS,
					0x232E75,
					0xC0C3DA,
					(new Item.Properties().tab(PanthalassaItemGroup.GROUP))));
	public static final RegistryObject<Item> MEGALODON_SPAWN_EGG = ITEMS.register("megalodon_spawn_egg",
			() -> new ItemPanthalassaSpawnEgg(
					PanthalassaEntityTypes.MEGALODON,
					0xB3B4B9,
					0xD1D4E7,
					new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> ARCHELON_SPAWN_EGG = ITEMS.register("archelon_spawn_egg",
			() -> new ItemPanthalassaSpawnEgg(
					PanthalassaEntityTypes.ARCHELON,
					0x462C10,
					0xD39049,
					new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> MOSASAURUS_SPAWN_EGG = ITEMS.register("mosasaurus_spawn_egg",
			() -> new ItemPanthalassaSpawnEgg(
					PanthalassaEntityTypes.MOSASAURUS,
					0x54C07C,
					0xCDE8D7,
					new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> COELACANTH_SPAWN_EGG = ITEMS.register("coelacanth_spawn_egg",
			() -> new ItemPanthalassaSpawnEgg(
					PanthalassaEntityTypes.COELACANTH,
					0x075B25,
					0x11371F,
					new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> DUNKLEOSTEUS_SPAWN_EGG = ITEMS.register("dunkleosteus_spawn_egg",
			() -> new ItemPanthalassaSpawnEgg(
					PanthalassaEntityTypes.DUNKLEOSTEUS,
					0x060F46,
					0x1D2763,
					new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> LEEDSICHTHYS_SPAWN_EGG = ITEMS.register("leedsichthys_spawn_egg",
			() -> new ItemPanthalassaSpawnEgg(
					PanthalassaEntityTypes.LEEDSICHTHYS,
					0x5A451E,
					0x3E3523,
					new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> GIANT_ORTHOCONE_SPAWN_EGG = ITEMS.register("giant_orthocone_spawn_egg",
			() -> new ItemPanthalassaSpawnEgg(
					PanthalassaEntityTypes.GIANT_ORTHOCONE,
					0x3e1010,
					0xb78685,
					new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> BASILOSAURUS_SPAWN_EGG = ITEMS.register("basilosaurus_spawn_egg",
			() -> new ItemPanthalassaSpawnEgg(
					PanthalassaEntityTypes.BASILOSAURUS,
					0x6e1e1e,
					0x340c0c,
					new Item.Properties().tab(PanthalassaItemGroup.GROUP)));

	public static final RegistryObject<Item> AGII_VEHICLE = ITEMS.register("ag2_vehicle",
			() -> new ItemAGII(new Item.Properties().tab(PanthalassaItemGroup.GROUP).stacksTo(1)));
	public static final RegistryObject<Item> MRSV_VEHICLE = ITEMS.register("mrsv_vehicle",
			() -> new ItemMRSV(new Item.Properties().tab(PanthalassaItemGroup.GROUP).stacksTo(1)));
	public static final RegistryObject<Item> PCSV_VEHICLE = ITEMS.register("pcsv_vehicle",
			() -> new ItemPCSV(new Item.Properties().tab(PanthalassaItemGroup.GROUP).stacksTo(1)));
	public static final RegistryObject<Item> ECSV_VEHICLE = ITEMS.register("ecsv_vehicle",
			() -> new ItemECSV(new Item.Properties().tab(PanthalassaItemGroup.GROUP).stacksTo(1)));

	public static final RegistryObject<Item> HPR_GLASS_SPHERE = ITEMS.register("hpr_glass_sphere",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> HPR_VEHICLE_SEAT = ITEMS.register("hpr_vehicle_seat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> HPR_VEHICLE_PANEL = ITEMS.register("hpr_vehicle_panel",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> HPR_VEHICLE_COCKPIT = ITEMS.register("hpr_vehicle_cockpit",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> HPR_VEHICLE_CHASSIS = ITEMS.register("hpr_vehicle_chassis",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> HPR_VEHICLE_PROPELLER = ITEMS.register("hpr_vehicle_propeller",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> HPR_VEHICLE_ENGINE = ITEMS.register("hpr_vehicle_engine",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> HPR_VEHICLE_LIGHT = ITEMS.register("hpr_vehicle_light",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> CREATURE_SYNTHESIZER_CORE = ITEMS.register("creature_synthesizer_core",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> CREATURE_SYNTHESIZER_STABILIZER = ITEMS.register("creature_synthesizer_stabilizer",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> GENOME_PREDATOR_Y = ITEMS.register("genome_predator_y",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> GENOME_GHONZUL = ITEMS.register("genome_ghonzul",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> GENOME_VRAXAILS = ITEMS.register("genome_vraxails",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> PRESSURE_EQUALIZER_MATRIX = ITEMS.register("pressure_equalizer_matrix",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> TORPEDO_GUIDANCE_SYSTEM = ITEMS.register("torpedo_guidance_system",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> BLAST_TORPEDO = ITEMS.register("blast_torpedo",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> TRANQUILIZING_TORPEDO = ITEMS.register("tranquilizing_torpedo",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> VEHICLE_PICKUP_TOOL = ITEMS.register("vehicle_pickup_tool",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));

	public static final RegistryObject<Item> DIVING_SUIT_HELMET = ITEMS.register("diving_suit_helmet",
			() -> new ItemDivingSuit(
					ArmorMaterials.IRON, EquipmentSlot.HEAD));
	public static final RegistryObject<Item> DIVING_SUIT_CHEST = ITEMS.register("diving_suit_chest",
			() -> new ItemDivingSuit(
					ArmorMaterials.IRON, EquipmentSlot.CHEST));
	public static final RegistryObject<Item> DIVING_SUIT_LEGS = ITEMS.register("diving_suit_legs",
			() -> new ItemDivingSuit(
					ArmorMaterials.IRON, EquipmentSlot.LEGS));
	public static final RegistryObject<Item> DIVING_SUIT_BOOTS = ITEMS.register("diving_suit_boots",
			() -> new ItemDivingSuit(
					ArmorMaterials.IRON, EquipmentSlot.FEET));

	public static final RegistryObject<Item> GIANT_ORTHOCONE_SHELL = ITEMS.register("giant_orthocone_shell",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> GIANT_ORTHOCONE_SHELL_DISPLAY = ITEMS.register("giant_orthocone_shell_display",
			() -> new ItemGiantOrthoconeShellDisplay(
					new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> KRONOSAURUS_SKULL = ITEMS.register("kronosaurus_skull",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> KRONOSAURUS_SKULL_DISPLAY = ITEMS.register("kronosaurus_skull_display",
			() -> new ItemKronosaurusSkullDisplay(
					new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> ARCHELON_SHELL = ITEMS.register("archelon_shell",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> ARCHELON_SHELL_DISPLAY = ITEMS.register("archelon_shell_display",
			() -> new ItemArchelonShellDisplay(
					new Item.Properties().tab(PanthalassaItemGroup.GROUP)));

	public static final RegistryObject<Item> PLIOSAUR_MEAT = ITEMS.register("pliosaur_meat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)
					.food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4F).meat().build())));
	public static final RegistryObject<Item> COOKED_PLIOSAUR_MEAT = ITEMS.register("cooked_pliosaur_meat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)
					.food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).meat().build())));
	public static final RegistryObject<Item> PRIMAL_SHARK_MEAT = ITEMS.register("primal_shark_meat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)
					.food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4F).meat().build())));
	public static final RegistryObject<Item> COOKED_PRIMAL_SHARK_MEAT = ITEMS.register("cooked_primal_shark_meat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)
					.food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).meat().build())));
	public static final RegistryObject<Item> PRIMAL_TURTLE_MEAT = ITEMS.register("primal_turtle_meat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)
					.food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4F).meat().build())));
	public static final RegistryObject<Item> COOKED_PRIMAL_TURTLE_MEAT = ITEMS.register("cooked_primal_turtle_meat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)
					.food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).meat().build())));
	public static final RegistryObject<Item> PRIMAL_WHALE_MEAT = ITEMS.register("primal_whale_meat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)
					.food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4F).meat().build())));
	public static final RegistryObject<Item> COOKED_PRIMAL_WHALE_MEAT = ITEMS.register("cooked_primal_whale_meat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)
					.food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).meat().build())));
	public static final RegistryObject<Item> MOSASAUR_MEAT = ITEMS.register("mosasaur_meat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)
					.food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4F).meat().build())));
	public static final RegistryObject<Item> COOKED_MOSASAUR_MEAT = ITEMS.register("cooked_mosasaur_meat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)
					.food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).meat().build())));
	public static final RegistryObject<Item> PRIMAL_FISH_MEAT = ITEMS.register("primal_fish_meat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)
					.food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4F).meat().build())));
	public static final RegistryObject<Item> COOKED_PRIMAL_FISH_MEAT = ITEMS.register("cooked_primal_fish_meat",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)
					.food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8F).meat().build())));
}