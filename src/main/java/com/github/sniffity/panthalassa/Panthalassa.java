package com.github.sniffity.panthalassa;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Panthalassa.MODID)
public class Panthalassa
{
    public static final String MODID = "panthalassa";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Panthalassa(IEventBus modEventBus)
    {

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }
}
