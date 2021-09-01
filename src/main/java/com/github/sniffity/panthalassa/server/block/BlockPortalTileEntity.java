package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.server.registry.PanthalassaTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class BlockPortalTileEntity extends TileEntity {

    public RegistryKey<World> destinationWorld = null;
    public BlockPos destinationPos = null;
    public BlockPos offsetFromCenter = null;

    public BlockPortalTileEntity() {
        super(PanthalassaTileEntities.PORTAL_TE.get());
    }

    @Override
    public void load(BlockState blockState, CompoundNBT compoundNBT) {
        super.load(blockState, compoundNBT);

        if(compoundNBT.contains("destinationWorld"))
            this.destinationWorld = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(compoundNBT.getString("destinationWorld")));

        if(compoundNBT.contains("destinationPos"))
            this.destinationPos = NBTUtil.readBlockPos(compoundNBT.getCompound("destinationPos"));

        if(compoundNBT.contains("offsetFromCenter"))
            this.offsetFromCenter = NBTUtil.readBlockPos(compoundNBT.getCompound("offsetFromCenter"));
    }

    @Override
    public CompoundNBT save(CompoundNBT compoundNBT) {
        super.save(compoundNBT);

        if(destinationWorld != null)
            compoundNBT.putString("destinationWorld", destinationWorld.location().toString());

        if(destinationPos != null)
            compoundNBT.put("destinationPos", NBTUtil.writeBlockPos(destinationPos));

        if(offsetFromCenter != null)
            compoundNBT.put("offsetFromCenter", NBTUtil.writeBlockPos(offsetFromCenter));

        return compoundNBT;
    }
}
