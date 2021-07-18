//CONFIRM ALL HERE
//Order of imports?

package com.github.sniffity.panthalassa.server.registry;

import com.github.sniffity.panthalassa.Panthalassa;

import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.item.Item;


//Registration makes items known to the game
public class PanthalassaItems {
	
//ITEMREGISTRY is defined as a DeferredRegister<Item> and set to create ForgeRegistries for items, for Panthalassa Mod. 
	//This is PUBLIC because the DeferredRegister called REGISTRY  will  be called upon outside this class, in main class  specifically. 
	//Static because even if no items exist and are registered, the registry must still be referenced
	//Final because the registry will not be changed.
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Panthalassa.MODID);
	
//	Individual Items are registered here
	//Why name them twice, once in caps once regularly?
	//Because name in CAPS is variable, regular name is actual item being registered
	// They are then classified into one of the many items java classes
	
	
	
	public static final RegistryObject<Item> HPR_GLASS_SPHERE = REGISTRY.register("hpr_glass_sphere", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> VEHICLE_SEATS = REGISTRY.register("vehicle_seats",() -> new Item(new Item.Properties()));
	
	//OF NOTE: These registries MUST be called within the mainmod Class

  
	
	
}
