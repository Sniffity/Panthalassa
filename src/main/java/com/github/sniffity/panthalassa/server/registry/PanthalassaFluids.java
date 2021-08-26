package com.github.sniffity.panthalassa.server.registry;
import com.github.sniffity.panthalassa.Panthalassa;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.system.CallbackI;

public class PanthalassaFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Panthalassa.MODID);

    public static final RegistryObject<FlowingFluid> PANTHALASSA_WATER_SOURCE = FLUIDS.register("panthalassa_water_source", () -> new ForgeFlowingFluid.Source(PanthalassaFluids.PANTHALASSA_WATER_PROPERTIES));
    public static final RegistryObject<FlowingFluid> PANTHALASSA_WATER_FLOWING = FLUIDS.register("panthalassa_water_flowing", () -> new ForgeFlowingFluid.Flowing(PanthalassaFluids.PANTHALASSA_WATER_PROPERTIES));

    public static final ForgeFlowingFluid.Properties PANTHALASSA_WATER_PROPERTIES = new ForgeFlowingFluid.Properties(PANTHALASSA_WATER_SOURCE, PANTHALASSA_WATER_FLOWING, FluidAttributes.builder(new ResourceLocation(Panthalassa.MODID, "fluid/panthalassa_water_still"), new ResourceLocation(Panthalassa.MODID, "fluid/panthalassa_water_still"))).block(PanthalassaBlocks.PANTHALASSA_WATER);
}