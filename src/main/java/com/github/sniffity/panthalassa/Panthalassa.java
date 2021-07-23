package com.github.sniffity.panthalassa;
import com.github.sniffity.panthalassa.common.registry.PanthalassaDimension;
import com.github.sniffity.panthalassa.common.registry.PanthalassaEntityTypes;
import com.github.sniffity.panthalassa.common.registry.PanthalassaSurfaceBuilders;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.github.sniffity.panthalassa.common.registry.PanthalassaItems;
import software.bernie.geckolib3.GeckoLib;


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
		PanthalassaSurfaceBuilders.SURFACE_BUILDERS.register(bus);


		MinecraftForge.EVENT_BUS.register(PanthalassaDimension.PANTHALASSA);
		MinecraftForge.EVENT_BUS.register(PanthalassaDimension.PANTHALASSA_TYPE);

		GeckoLib.initialize();
	}

	@SuppressWarnings("deprecated")
	private void setup(final FMLCommonSetupEvent event){
		DeferredWorkQueue.runLater(() -> {PanthalassaEntityTypes.setupEntityTypeAttributes();
		});
	}
}

