package com.github.sniffity.panthalassa;

//imports for @Mod
import net.minecraftforge.fml.common.Mod;

//imports for Logger
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//Imports for invoking Registries
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

//Imports for invoking ItemRegistries
import com.github.sniffity.panthalassa.server.registry.PanthalassaItems;




//*******The @Mod annotation tells Java that Panthalassa.panthalass is the main mod class.
	//But: Panthalassa (capital P)is the class. What this works out to is Panthalassa.panthalassa
		//In Mowzie's Mobs case, it works out to MowziesMobs.mowziesmobs
			//Isn't the correct path panthalassa.Panthalassa?
//*****Also, why MODID in caps? Camel case? Pascal case?


@Mod(Panthalassa.MODID)

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
	public static final String MODID = "panthalassa";
	
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
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		//Invoking the registry so things actually register
		PanthalassaItems.REGISTRY.register(bus);
		
		
	}
	
	
}
