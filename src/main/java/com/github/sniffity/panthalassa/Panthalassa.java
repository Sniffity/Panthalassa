package com.github.sniffity.panthalassa;

import com.github.sniffity.panthalassa.client.ClientHandler;
import com.github.sniffity.panthalassa.server.entity.creature.EntityArchelon;
import com.github.sniffity.panthalassa.server.entity.creature.EntityKronosaurus;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMegalodon;
import com.github.sniffity.panthalassa.server.entity.creature.EntityMosasaurus;
import com.github.sniffity.panthalassa.server.network.PanthalassaPacketHandler;
import com.github.sniffity.panthalassa.server.registry.*;
import com.mojang.serialization.Codec;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Mod(Panthalassa.MODID)
@Mod.EventBusSubscriber(modid = Panthalassa.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)


public final class Panthalassa {
	public static final String MODID = "panthalassa";
	public static final Logger LOGGER = LogManager.getLogger();

	public Panthalassa() {
		final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		PanthalassaBlocks.BLOCKS.register(modBus);
		PanthalassaItems.ITEMS.register(modBus);

		PanthalassaEntityTypes.ENTITY_TYPES.register(modBus);

		PanthalassaStructures.STRUCTURES.register(modBus);

		PanthalassaBiomes.BIOMES.register(modBus);
		PanthalassaFeatures.FEATURES.register(modBus);
		PanthalassaSurfaceBuilders.SURFACE_BUILDERS.register(modBus);

		PanthalassaPOI.POI.register(modBus);

		MinecraftForge.EVENT_BUS.register(PanthalassaDimension.PANTHALASSA);
		MinecraftForge.EVENT_BUS.register(PanthalassaDimension.PANTHALASSA_TYPE);

		GeckoLib.initialize();
		MinecraftForge.EVENT_BUS.register(this);
		modBus.addListener(this::setup);
		modBus.addListener(this::registerEntityAttributes);


		forgeBus.addListener(EventPriority.NORMAL, PanthalassaDimension::worldTick);
		forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
		forgeBus.addListener(EventPriority.HIGH, this::biomeModification);
	}

	private void setup(final FMLCommonSetupEvent event){
		PanthalassaPacketHandler.register();

//		DeferredWorkQueue.runLater(() -> {PanthalassaEntityTypes.setupEntityTypeAttributes();});
		event.enqueueWork(() -> {
			PanthalassaStructures.setupStructures();
			PanthalassaConfiguredStructures.registerConfiguredStructures();
		});
	}

	public void biomeModification (final BiomeLoadingEvent event) {
			event.getGeneration().getStructures().add(() -> PanthalassaConfiguredStructures.CONFIGURED_PANTHALASSA_LABORATORY);
	}

	private void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(PanthalassaEntityTypes.KRONOSAURUS.get(), EntityKronosaurus.kronosaurusAttributes().build());
		event.put(PanthalassaEntityTypes.MEGALODON.get(), EntityMegalodon.megalodonAttributes().build());
		event.put(PanthalassaEntityTypes.ARCHELON.get(), EntityArchelon.archelonAttributes().build());
		event.put(PanthalassaEntityTypes.MOSASAURUS.get(), EntityMosasaurus.mosasaurusAttributes().build());
	}

	private static Method GETCODEC_METHOD;
	public void addDimensionalSpacing(final WorldEvent.Load event) {
		if(event.getWorld() instanceof ServerWorld){
			ServerWorld serverWorld = (ServerWorld)event.getWorld();

			try {
				if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
				ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
				if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
			}
			catch(Exception e){
				Panthalassa.LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
			}


			if(serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator &&
					serverWorld.dimension().equals(World.OVERWORLD)){
				return;
			}


			Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
			tempMap.putIfAbsent(PanthalassaStructures.PANTHALASSA_LABORATORY.get(), DimensionStructuresSettings.DEFAULTS.get(PanthalassaStructures.PANTHALASSA_LABORATORY.get()));
			serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
		}
	}

	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
		PanthalassaBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
			event.getRegistry().register(new BlockItem(block, new Item.Properties().tab(PanthalassaItemGroup.GROUP))
					.setRegistryName(block.getRegistryName()));
		});
	}
}