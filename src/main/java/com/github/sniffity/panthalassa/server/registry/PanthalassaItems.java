//CONFIRM ALL HERE
//Order of imports?

package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


//Registration makes items known to the game
public class PanthalassaItems {
	
//ITEMITEMS is defined as a DeferredRegister<Item> and set to create ForgeRegistries for items, for Panthalassa Mod. 
	//This is PUBLIC because the DeferredRegister called ITEMS  will  be called upon outside this class, in main class  specifically. 
	//Static because even if no items exist and are registered, the ITEMS must still be referenced
	//Final because the ITEMS will not be changed.
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Panthalassa.MOD_ID);
	
//	Individual Items are registered here
	//Why name them twice, once in caps once regularly?
	//Because name in CAPS is variable, regular name is actual item being registered
	// They are then classified into one of the many items java classes
	
	
	//WHAT IS THIS DOING EXACTLY? REVIEW THIS.
	
	public static final RegistryObject<Item> HPR_GLASS_SPHERE = ITEMS.register("hpr_glass_sphere", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE_SEATS = ITEMS.register("hpr_vehicle_seats",() -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE_COCKPIT = ITEMS.register("hpr_vehicle_cockpit",()-> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE_CHASIS = ITEMS.register("hpr_vehicle_chasis",()-> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE_WING = ITEMS.register("hpr_vehicle_wing",()-> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE_PROPELLER = ITEMS.register("hpr_vehicle_propeller",()-> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE_TAIL = ITEMS.register("hpr_vehicle_tail",()-> new Item(new Item.Properties()));
	public static final RegistryObject<Item> HPR_VEHICLE = ITEMS.register("hpr_vehicle",()-> new Item(new Item.Properties()));
	
	//OF NOTE: These registries MUST be called within the mainmod Class

  
	
	
}
