package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlockEntities;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;

public class BlockPortalBlockEntity extends BlockEntity {

    public ResourceKey<Level> destinationWorld = null;
    public BlockPos destinationPos = null;
    public BlockPos offsetFromCenter = null;

    public BlockPortalBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PanthalassaBlockEntities.PORTAL_TE.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag compoundNBT) {
        super.load(compoundNBT);

        if(compoundNBT.contains("destinationWorld"))
            this.destinationWorld = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(compoundNBT.getString("destinationWorld")));

        if(compoundNBT.contains("destinationPos"))
            this.destinationPos = NbtUtils.readBlockPos(compoundNBT.getCompound("destinationPos"));

        if(compoundNBT.contains("offsetFromCenter"))
            this.offsetFromCenter = NbtUtils.readBlockPos(compoundNBT.getCompound("offsetFromCenter"));
    }

    @Override
    public CompoundTag save(CompoundTag compoundNBT) {
        super.save(compoundNBT);

        if(destinationWorld != null)
            compoundNBT.putString("destinationWorld", destinationWorld.location().toString());

        if(destinationPos != null)
            compoundNBT.put("destinationPos", NbtUtils.writeBlockPos(destinationPos));

        if(offsetFromCenter != null)
            compoundNBT.put("offsetFromCenter", NbtUtils.writeBlockPos(offsetFromCenter));

        return compoundNBT;
    }
}
