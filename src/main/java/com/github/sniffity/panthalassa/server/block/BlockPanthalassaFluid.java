package com.github.sniffity.panthalassa.server.block;


import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;

import java.util.function.Supplier;

public class BlockPanthalassaFluid extends FlowingFluidBlock {

    public BlockPanthalassaFluid(Supplier<? extends FlowingFluid> supplier, Properties properties) {
        super(supplier, properties
                .noCollission()
                .strength(100F)
                .noDrops());
    }
}
