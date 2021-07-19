package com.github.sniffity.panthalassa;

//imports for @Mod
import net.minecraftforge.fml.common.Mod;

//imports for Logger

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import net.minecraftforge.common.MinecraftForge;
//Imports for invoking Registries
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

//Imports for invoking ItemRegistries
import com.github.sniffity.panthalassa.server.registry.PanthalassaItems;

//Imports for invoking EntityTypeRegistries
import com.github.sniffity.panthalassa.server.registry.PanthalassaEntities;





@Mod(Panthalassa.MOD_ID)

//Start of the Panthalassa class
//Should this class be declared as final?
	// Yes, because it will not be modified.
	//Other classes might invoke methods being defined here.
		//But they will not modify the class, what is being defined within it.


public final class Panthalassa {


	//The MODID variable is **declared** as a **string** type variable
		//public because this variable will be accessed by other classes.
		//static because the field MOD ID must exist idependently from all instances of the class, even if no instances exist.
		//final because this variable is constant, it will not change.
	public static final String MOD_ID = "panthalassa";

	/*Accesses and stores an existing logger to the LOGGER *field*, created by invoking the method getLogger,
	 *  getLogger is invoked in the LogManager class
	 * declaration is as follows:
	 * 	public, because the logger has to be accessible to other classes
	 * 	static, because it has to exist independently of the mods main class being instantiated,
	 * 		even if the constructor is never invoked
	 *  final, because the logger itself will not be modified
	 */
	public static final Logger LOGGER = LogManager.getLogger();
	
	//IMPORTANT:
	//public static final ServerProxy PROXY;
	//public static final SimpleChannel NETWORK;
	//What do each of these do?
	
	
	public Panthalassa() {
		//Field bus must be declared, read https://mcforge.readthedocs.io/en/latest/events/intro/
		//REVIEW THIS!
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		
		//bus.addListener(this::setup);

		//Invoking the registry so things actually register
		//bus.addListener(this::setup);
		PanthalassaItems.ITEMS.register(bus);
		PanthalassaEntities.ENTITY_TYPES.register(bus);
		
		//MinecraftForge.EVENT_BUS.register(this);
	
	}
	
	//Following line was required for launch
    //private void setup(final FMLCommonSetupEvent event) {}
	
}
