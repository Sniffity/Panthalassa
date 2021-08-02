package com.github.sniffity.panthalassa;
import com.github.sniffity.panthalassa.common.registry.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;


@Mod(Panthalassa.MODID)
@Mod.EventBusSubscriber(modid = Panthalassa.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)


public final class Panthalassa {
	public static final String MODID = "panthalassa";
	public static final Logger LOGGER = LogManager.getLogger();

	public Panthalassa() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		PanthalassaBlocks.BLOCKS.register(bus);
		PanthalassaItems.ITEMS.register(bus);

		PanthalassaEntityTypes.ENTITY_TYPES.register(bus);

		PanthalassaFeatures.FEATURES.register(bus);
		PanthalassaSurfaceBuilders.SURFACE_BUILDERS.register(bus);

		PanthalassaPOI.POI.register(bus);

		MinecraftForge.EVENT_BUS.register(PanthalassaDimension.PANTHALASSA);
		MinecraftForge.EVENT_BUS.register(PanthalassaDimension.PANTHALASSA_TYPE);

		GeckoLib.initialize();
		MinecraftForge.EVENT_BUS.register(this);

		forgeBus.addListener(EventPriority.NORMAL, PanthalassaDimension::worldTick);
	}

	@SuppressWarnings("deprecated")
	private void setup(final FMLCommonSetupEvent event){
		DeferredWorkQueue.runLater(() -> {PanthalassaEntityTypes.setupEntityTypeAttributes();
		});
		DeferredWorkQueue.runLater(() -> {PanthalassaFeatures.registerConfiguredFeatures();});
	}


	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
		PanthalassaBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
			event.getRegistry().register(new BlockItem(block, new Item.Properties().group(PanthalassaItemGroup.GROUP))
					.setRegistryName(block.getRegistryName()));
		});
	}
}