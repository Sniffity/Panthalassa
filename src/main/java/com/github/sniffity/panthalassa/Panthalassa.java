package com.github.sniffity.panthalassa;

import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.config.PanthalassaCommonConfig;
import com.github.sniffity.panthalassa.server.events.PanthalassaEventListener;
import com.github.sniffity.panthalassa.server.network.PanthalassaPacketHandler;
import com.github.sniffity.panthalassa.server.registry.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(Panthalassa.MODID)

public final class Panthalassa {
	public static final String MODID = "panthalassa";
	public static final Logger LOGGER = LogManager.getLogger();

	public Panthalassa() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		forgeBus.addListener(EventPriority.NORMAL, PanthalassaDimension::worldTick);

		modBus.addListener(this::setup);
		modBus.addListener(PanthalassaEntityTypes::registerEntityAttributes);

		PanthalassaBlockEntities.BLOCK_ENTITY_TYPES.register(modBus);
		PanthalassaEntityTypes.ENTITY_TYPES.register(modBus);
		PanthalassaItems.ITEMS.register(modBus);
		PanthalassaBlocks.BLOCKS.register(modBus);
		PanthalassaStructures.STRUCTURES.register(modBus);
		PanthalassaSounds.SOUND_EVENTS.register(modBus);
		PanthalassaPOI.POI.register(modBus);
		PanthalassaFeatures.FEATURES.register(modBus);
		PanthalassaEffects.EFFECTS.register(modBus);
		PanthalassaParticlesTypes.PARTICLE_TYPES.register(modBus);

		GeckoLib.initialize();
		forgeBus.register(this);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PanthalassaClientConfig.GENERAL_SPEC, "panthalassa-client-config.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PanthalassaCommonConfig.COMMON_CONFIG, "panthalassa-common-config.toml");
	}

	private void setup(final FMLCommonSetupEvent event) {
		PanthalassaPacketHandler.register();
		event.enqueueWork(() -> {
			PanthalassaProcessors.registerProcessors();
		});
	}
}