package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;

import com.github.sniffity.panthalassa.server.item.ItemAGII;
import com.github.sniffity.panthalassa.server.item.ItemMRSV;
import com.github.sniffity.panthalassa.server.item.ItemPanthalassaSpawnEgg;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class PanthalassaItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Panthalassa.MODID);

	//vehicle
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
	public static final RegistryObject<Item> AGII_VEHICLE = ITEMS.register("ag2_vehicle",
			() -> new ItemAGII(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> MRSV_VEHICLE = ITEMS.register("mrsv_vehicle",
			() -> new ItemMRSV(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));

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


}
