package com.github.sniffity.panthalassa;

import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.config.PanthalassaCommonConfig;
import com.github.sniffity.panthalassa.server.entity.creature.*;
import com.github.sniffity.panthalassa.server.entity.display.PanthalassaDisplayEntity;
import com.github.sniffity.panthalassa.server.network.PanthalassaPacketHandler;
import com.github.sniffity.panthalassa.server.registry.*;
import com.github.sniffity.panthalassa.server.world.spawn.PanthalassaSpawns;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

import java.util.Arrays;
import java.util.List;

@Mod(Panthalassa.MODID)
@Mod.EventBusSubscriber(modid = Panthalassa.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public final class Panthalassa {
	public static final String MODID = "panthalassa";
	public static final Logger LOGGER = LogManager.getLogger();

	public Panthalassa() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		forgeBus.addListener(EventPriority.NORMAL, PanthalassaDimension::worldTick);

		modBus.addListener(this::setup);
		modBus.addListener(this::registerEntityAttributes);

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
			PanthalassaSpawns.registerSpawnPlacementTypes();
			PanthalassaProcessors.registerProcessors();
		});
	}

	//TODO: Move to Event
	@SubscribeEvent
	public void onBiomeLoading(BiomeLoadingEvent event) {
		PanthalassaSpawns.onBiomeLoading(event);
	}

	//TODO: Move to EntityType Registry?
	private void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(PanthalassaEntityTypes.KRONOSAURUS.get(), EntityKronosaurus.kronosaurusAttributes().build());
		event.put(PanthalassaEntityTypes.MEGALODON.get(), EntityMegalodon.megalodonAttributes().build());
		event.put(PanthalassaEntityTypes.ARCHELON.get(), EntityArchelon.archelonAttributes().build());
		event.put(PanthalassaEntityTypes.MOSASAURUS.get(), EntityMosasaurus.mosasaurusAttributes().build());
		event.put(PanthalassaEntityTypes.COELACANTH.get(), EntityCoelacanth.coelacanthAttributes().build());
		event.put(PanthalassaEntityTypes.DUNKLEOSTEUS.get(), EntityDunkleosteus.dunkleosteusAttributes().build());
		event.put(PanthalassaEntityTypes.LEEDSICHTHYS.get(), EntityLeedsichthys.leedsichthysAttributes().build());
		event.put(PanthalassaEntityTypes.GIANT_ORTHOCONE.get(), EntityGiantOrthocone.giantOrthoconeAttributes().build());
		event.put(PanthalassaEntityTypes.BASILOSAURUS.get(), EntityBasilosaurus.basilosaurusAttributes().build());
		event.put(PanthalassaEntityTypes.THALASSOMEDON.get(), EntityThalassomedon.thalassomedonAttributes().build());
		event.put(PanthalassaEntityTypes.ACROLEPIS.get(), EntityAcrolepis.acrolepisAttributes().build());
		event.put(PanthalassaEntityTypes.CERATODUS.get(), EntityCeratodus.ceratodusAttributes().build());
		event.put(PanthalassaEntityTypes.HELICOPRION.get(), EntityHelicoprion.helicoprionAttributes().build());
		event.put(PanthalassaEntityTypes.ANGLERFISH.get(), EntityAnglerfish.anglerfishAttributes().build());
		event.put(PanthalassaEntityTypes.ANOMALOCARIS.get(), EntityAnomalocaris.anomalocarisAttributes().build());
		event.put(PanthalassaEntityTypes.GIANT_ORTHOCONE_SHELL.get(), PanthalassaDisplayEntity.displayAttributes().build());
		event.put(PanthalassaEntityTypes.KRONOSAURUS_SKULL.get(), PanthalassaDisplayEntity.displayAttributes().build());
		event.put(PanthalassaEntityTypes.ARCHELON_SHELL.get(), PanthalassaDisplayEntity.displayAttributes().build());
	}

	//TODO: Move to Event
	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
		RegistryObject<Block>[] exclusionArray;
		exclusionArray = new RegistryObject[4];
		exclusionArray[0] =PanthalassaBlocks.PORTAL;
		exclusionArray[1] =PanthalassaBlocks.LIGHT_AIR;
		exclusionArray[2] =PanthalassaBlocks.LIGHT_WATER;
		exclusionArray[3] =PanthalassaBlocks.PANTHALASSA_WATER;
		List<RegistryObject<Block>> exclusionCollection = Arrays.asList(exclusionArray);
		PanthalassaBlocks.BLOCKS.getEntries()
				.stream()
				.filter(i -> !exclusionCollection.contains(i))
				.map(RegistryObject::get)
				.forEach(block -> {event.getRegistry().register(new BlockItem(block, new Item.Properties().tab(PanthalassaItemGroup.GROUP))
					.setRegistryName(block.getRegistryName())); });
	}
}