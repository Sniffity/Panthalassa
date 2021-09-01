package com.github.sniffity.panthalassa;

import com.github.sniffity.panthalassa.server.entity.creature.*;
import com.github.sniffity.panthalassa.server.item.ItemPanthalassaSpawnEgg;
import com.github.sniffity.panthalassa.server.network.PanthalassaPacketHandler;
import com.github.sniffity.panthalassa.server.registry.*;
import com.mojang.serialization.Codec;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

		PanthalassaTileEntities.TILE_ENTITY_TYPES.register(modBus);
		PanthalassaEntityTypes.ENTITY_TYPES.register(modBus);

		PanthalassaStructures.STRUCTURES.register(modBus);

		PanthalassaBiomes.BIOMES.register(modBus);
		PanthalassaFeatures.FEATURES.register(modBus);
		PanthalassaSurfaceBuilders.SURFACE_BUILDERS.register(modBus);

		PanthalassaPOI.POI.register(modBus);

		forgeBus.register(PanthalassaDimension.PANTHALASSA);
		forgeBus.register(PanthalassaDimension.PANTHALASSA_TYPE);


		GeckoLib.initialize();
		forgeBus.register(this);
		modBus.addListener(this::setup);
		modBus.addListener(this::registerEntityAttributes);


		forgeBus.addListener(EventPriority.NORMAL, PanthalassaDimension::worldTick);
		forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
		forgeBus.addListener(EventPriority.HIGH, this::biomeModification);
	}

	private void setup(final FMLCommonSetupEvent event){
		PanthalassaPacketHandler.register();

		event.enqueueWork(() -> {
			PanthalassaStructures.setupStructures();
			PanthalassaConfiguredStructures.registerConfiguredStructures();
			PanthalassaEntityTypes.spawnPlacements();
			PanthalassaDimension.registerDimensionAccessories();
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
		event.put(PanthalassaEntityTypes.COELACANTH.get(), EntityCoelacanth.coelacanthAttributes().build());

	}

	private static Method GETCODEC_METHOD;
	public void addDimensionalSpacing(final WorldEvent.Load event) {
		if(event.getWorld() instanceof ServerWorld){
			ServerWorld serverWorld = (ServerWorld)event.getWorld();
			if (serverWorld.getLevel().dimension() != World.OVERWORLD) {
				return;
			}

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
		}
	}

	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
		PanthalassaBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).forEach(block -> {
			event.getRegistry().register(new BlockItem(block, new Item.Properties().tab(PanthalassaItemGroup.GROUP))
					.setRegistryName(block.getRegistryName()));
		});
	}

	@SubscribeEvent
	public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
		ItemPanthalassaSpawnEgg.initUnaddedEggs();
	}
}