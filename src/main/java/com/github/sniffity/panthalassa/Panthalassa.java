package com.github.sniffity.panthalassa;

import com.github.sniffity.panthalassa.entity.creature.CreatureKronosaurus;
import com.github.sniffity.panthalassa.entity.registry.PanthalassaEntityTypes;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Panthalassa.MODID)
public class Panthalassa {
    public static final String MODID = "panthalassa";

    public static final boolean DEBUG = true;
    private static final Logger LOGGER = LogUtils.getLogger();
    /*
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.panthalassa")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

     */

    public Panthalassa(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        PanthalassaEntityTypes.ENTITY_TYPES.register(modEventBus);


        /*
        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

         */
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }


    /*
    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

     */
}