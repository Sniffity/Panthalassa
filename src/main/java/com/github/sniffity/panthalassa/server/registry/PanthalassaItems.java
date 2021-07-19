//CONFIRM ALL HERE
//Order of imports?

package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;

import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


//Registration makes items known to the game
public class PanthalassaItems {
	
//ITEMS is defined as a DeferredRegister<Item> and it's value is assigned by passing it through the create method in DeferredRegister.
// set to create ForgeRegistries for items, for Panthalassa Mod. What I'm giving this method is ForgeRegistries.ITEMS and my MOD ID
//This is PUBLIC because the DeferredRegister called ITEMS  will  be called upon outside this class, in main class  specifically.
// Static because even if no items exist and are registered, the ITEMS must still be referenced
//Final because the ITEMS will not be changed.
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Panthalassa.MOD_ID);
	
//	The METHODs to create Individual Items are registered here

	// Here:
	// The first name, in CAPS, is the variable
	///The second name, in lower case, is the actual String name
	//I call register with two fields: String name, and Supplier
	//Name is given.
	//Supplier is a function object. It will take no arguments, and return whatever type is in the <>
	//Consider the Item function:
	//public Item goober() {
	//	return new Item(new Item.Properties);
	//}
	// This function is stored as an OBJECT. Hence:
	// public Supplier<? extends Item> goober = () -> {
	//        return new Item(new Item.Properties())
	//}
	// object goober has the type Suppier<? extends Item>
	// goober.call() returns a new Item instance.
	//goober creates the item instances, but goober is not an item instance.
	//
	//register, thus, takes a function object as it's second field.
	//The Item function in this case.
	//But I'm not calling thins function just yet.
	//Instead, I am simply making it available for register to use.
	//Later on, when I call register, it will be able to use that function to actually register the item.

	//Item() creates the Items, but it requires something to create them. An item must have some properties, otherwise it does not exist.
	//Hence, when calling new items I must call it with new Item.Properties(), I want it to generate item properties for this new item.



	//vehicle
	public static final RegistryObject<Item> HPR_GLASS_SPHERE = ITEMS.register("hpr_glass_sphere", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE_SEATS = ITEMS.register("hpr_vehicle_seats",() -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE_COCKPIT = ITEMS.register("hpr_vehicle_cockpit",()-> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE_CHASSIS = ITEMS.register("hpr_vehicle_chassis",()-> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE_WING = ITEMS.register("hpr_vehicle_wing",()-> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE_PROPELLER = ITEMS.register("hpr_vehicle_propeller",()-> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE_TAIL = ITEMS.register("hpr_vehicle_tail",()-> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE = ITEMS.register("hpr_vehicle",()-> new Item(new Item.Properties()));

	//spawn eggs


	//OF NOTE: These registries MUST be called within the mainmod Class


}
