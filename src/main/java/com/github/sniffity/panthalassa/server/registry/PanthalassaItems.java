package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


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
	public static final RegistryObject<Item> HPR_VEHICLE_WING = ITEMS.register("hpr_vehicle_wing",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
	public static final RegistryObject<Item> HPR_VEHICLE = ITEMS.register("hpr_vehicle",
			() -> new Item(new Item.Properties().tab(PanthalassaItemGroup.GROUP)));
}
