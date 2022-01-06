package com.github.sniffity.panthalassa;

import com.github.sniffity.panthalassa.config.PanthalassaClientConfig;
import com.github.sniffity.panthalassa.config.PanthalassaCommonConfig;
import com.github.sniffity.panthalassa.server.entity.creature.*;
import com.github.sniffity.panthalassa.server.network.PanthalassaPacketHandler;
import com.github.sniffity.panthalassa.server.registry.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.WorldEvent;
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
import java.util.HashMap;
import java.util.Map;

@Mod(Panthalassa.MODID)
@Mod.EventBusSubscriber(modid = Panthalassa.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)


public final class Panthalassa {
	public static final String MODID = "panthalassa";
	public static final Logger LOGGER = LogManager.getLogger();

	public Panthalassa() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		forgeBus.addListener(EventPriority.NORMAL, PanthalassaDimension::worldTick);
		forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);

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

		//	PanthalassaBiomes.BIOMES.register(modBus);
		//	PanthalassaSurfaceBuilders.SURFACE_BUILDERS.register(modBus);

		forgeBus.register(PanthalassaDimension.PANTHALASSA);
		forgeBus.register(PanthalassaDimension.PANTHALASSA_TYPE);


		GeckoLib.initialize();
		forgeBus.register(this);

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PanthalassaClientConfig.GENERAL_SPEC, "panthalassa-client-config.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PanthalassaCommonConfig.GENERAL_SPEC, "panthalassa-common-config.toml");

	}

	private void setup(final FMLCommonSetupEvent event) {
		PanthalassaPacketHandler.register();

		event.enqueueWork(() -> {
			PanthalassaStructures.setupStructures();
			PanthalassaConfiguredStructures.registerConfiguredStructures();
			PanthalassaEntityTypes.spawnPlacements();
			PanthalassaDimension.registerDimensionAccessories();
		});
	}

	private void registerEntityAttributes(EntityAttributeCreationEvent event) {
		event.put(PanthalassaEntityTypes.KRONOSAURUS.get(), EntityKronosaurus.kronosaurusAttributes().build());
		event.put(PanthalassaEntityTypes.MEGALODON.get(), EntityMegalodon.megalodonAttributes().build());
		event.put(PanthalassaEntityTypes.ARCHELON.get(), EntityArchelon.archelonAttributes().build());
		event.put(PanthalassaEntityTypes.MOSASAURUS.get(), EntityMosasaurus.mosasaurusAttributes().build());
		event.put(PanthalassaEntityTypes.COELACANTH.get(), EntityCoelacanth.coelacanthAttributes().build());
		event.put(PanthalassaEntityTypes.DUNKLEOSTEUS.get(), EntityDunkleosteus.dunkleosteusAttributes().build());
		event.put(PanthalassaEntityTypes.LEEDSICHTHYS.get(), EntityLeedsichthys.leedsichthysAttributes().build());
		event.put(PanthalassaEntityTypes.GIANT_ORTHOCONE.get(), EntityGiantOrthocone.giantOrthoconeAttributes().build());
	}

	public void addDimensionalSpacing(final WorldEvent.Load event) {
		if (event.getWorld() instanceof ServerLevel serverLevel) {
			ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();
			if (chunkGenerator instanceof FlatLevelSource && serverLevel.dimension().equals(Level.OVERWORLD)) {
				return;
			}
			StructureSettings worldStructureConfig = chunkGenerator.getSettings();


			HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> PanthalassaStructureMultiMap = new HashMap<>();

			for (Map.Entry<ResourceKey<Biome>, Biome> biomeEntry : serverLevel.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY).entrySet()) {
				Biome.BiomeCategory biomeCategory = biomeEntry.getValue().getBiomeCategory();
				if (biomeCategory == Biome.BiomeCategory.OCEAN) {
					associateBiomeToConfiguredStructure(PanthalassaStructureMultiMap, PanthalassaConfiguredStructures.CONFIGURED_PANTHALASSA_LABORATORY, biomeEntry.getKey());
				}
			}

			ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
			worldStructureConfig.configuredStructures.entrySet().stream().filter(entry -> !PanthalassaStructureMultiMap.containsKey(entry.getKey())).forEach(tempStructureToMultiMap::put);

			PanthalassaStructureMultiMap.forEach((key, value) -> tempStructureToMultiMap.put(key, ImmutableMultimap.copyOf(value)));

			worldStructureConfig.configuredStructures = tempStructureToMultiMap.build();


			Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(worldStructureConfig.structureConfig());
			tempMap.putIfAbsent(PanthalassaStructures.PANTHALASSA_LABORATORY.get(), StructureSettings.DEFAULTS.get(PanthalassaStructures.PANTHALASSA_LABORATORY.get()));
			worldStructureConfig.structureConfig = tempMap;

		}


	}

	private static void associateBiomeToConfiguredStructure(Map<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> PanthalassaStructureToMultiMap, ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> biomeRegistryKey) {
		PanthalassaStructureToMultiMap.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
		HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMultiMap = PanthalassaStructureToMultiMap.get(configuredStructureFeature.feature);
		if(configuredStructureToBiomeMultiMap.containsValue(biomeRegistryKey)) {
			Panthalassa.LOGGER.debug("""
                    Detected 2 ConfiguredStructureFeatures that share the same base StructureFeature trying to be added to same biome. One will be prevented from spawning.
                    This issue happens with vanilla too and is why a Snowy Village and Plains Village cannot spawn in the same biome because they both use the Village base structure.
                    The two conflicting ConfiguredStructures are: {}, {}
                    The biome that is attempting to be shared: {}
                """,
					BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureFeature),
					BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureToBiomeMultiMap.entries().stream().filter(e -> e.getValue() == biomeRegistryKey).findFirst().get().getKey()),
					biomeRegistryKey
			);
		}
		else{
			configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biomeRegistryKey);
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