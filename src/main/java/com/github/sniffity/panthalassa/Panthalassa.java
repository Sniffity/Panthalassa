package com.github.sniffity.panthalassa;
import com.github.sniffity.panthalassa.common.registry.PanthalassaEntityTypes;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.github.sniffity.panthalassa.common.registry.PanthalassaItems;


@Mod(Panthalassa.MODID)
@Mod.EventBusSubscriber(modid = Panthalassa.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)


public final class Panthalassa {
	public static final String MODID = "panthalassa";
	public static final Logger LOGGER = LogManager.getLogger();


	public Panthalassa() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		PanthalassaItems.ITEMS.register(bus);
		PanthalassaEntityTypes.ENTITY_TYPES.register(bus);
	}

@SuppressWarnings("deprecated")
	private void setup(final FMLCommonSetupEvent event){
		DeferredWorkQueue.runLater(() -> {
			PanthalassaEntityTypes.setupEntityTypeAttributes();
		//Taken from Dungeon Mobs
		//	BiomeSpawnEntries.initBiomeSpawnEntries();
		//	EntitySpawnPlacements.initSpawnPlacements();
		//	RaidEntries.initWaveMemberEntries();
		//	SensorMapModifier.replaceSensorMaps();
		//	CapabilityManager.INSTANCE.register(ICloneable.class, new CloneableStorage(), Cloneable::new);
		});
	}
}

