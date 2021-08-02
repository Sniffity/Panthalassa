package com.github.sniffity.panthalassa.common.block;

import com.github.sniffity.panthalassa.common.registry.PanthalassaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPortalFrame extends Block {


    int i = 0;

    public BlockPortalFrame() {
        super(Properties.create(
                Material.IRON,
                MaterialColor.GRAY)
                .hardnessAndResistance(-1.0F, 3600000.0F)
                .sound(SoundType.METAL));
    }

    boolean isPanthalassaPortal(BlockState state) {
        return state == PanthalassaBlocks.PORTAL.get().getDefaultState();
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
//        if (i <= 0) {
        if (worldIn.isBlockPowered(pos)) {
            if (isPanthalassaPortal(worldIn.getBlockState(pos.add(1, 0, 0))) ||
                    isPanthalassaPortal(worldIn.getBlockState(pos.add(-1, 0, 0))) ||
                    isPanthalassaPortal(worldIn.getBlockState(pos.add(0, 0, -1))) ||
                    isPanthalassaPortal(worldIn.getBlockState(pos.add(0, 0, 1)))) {
                if (((BlockPortal) PanthalassaBlocks.PORTAL.get()).tryDestoyPortal(worldIn, pos)) {
                    //  i = 200;
                }
            } else if (((BlockPortal) PanthalassaBlocks.PORTAL.get()).trySpawnPortal(worldIn, pos)) {
//            i = 200;
            }
        }
    }
}
//        i--;
//    }
