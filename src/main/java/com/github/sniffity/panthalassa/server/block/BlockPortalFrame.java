package com.github.sniffity.panthalassa.server.block;

import com.github.sniffity.panthalassa.server.registry.PanthalassaBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Panthalassa Mod - Class: BlockPortalFrame <br></br?>
 *
 * Source code: https://github.com/Sniffity/Panthalassa <br></br?>
 *
 * Acknowledgements: The following class was developed after studying how Atum 2, the Undergarden,
 * UltraAmplifiedDimension and The Twilight Forest mods implement their own respective teleportation systems.
 */

public class BlockPortalFrame extends Block {

    public BlockPortalFrame() {
        super(Properties.of(
                Material.METAL,
                MaterialColor.COLOR_GRAY)
                .strength(-1.0F, 3600000.0F)
                .sound(SoundType.METAL));
    }

    boolean isPanthalassaPortal(BlockState state) {
        return state == PanthalassaBlocks.PORTAL.get().defaultBlockState();
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.hasNeighborSignal(pos)) {
            if (isPanthalassaPortal(worldIn.getBlockState(pos.offset(1, 0, 0))) ||
                    isPanthalassaPortal(worldIn.getBlockState(pos.offset(-1, 0, 0))) ||
                    isPanthalassaPortal(worldIn.getBlockState(pos.offset(0, 0, -1))) ||
                    isPanthalassaPortal(worldIn.getBlockState(pos.offset(0, 0, 1)))) {
                ((BlockPortal) PanthalassaBlocks.PORTAL.get()).tryDestoyPortal(worldIn, pos);
            } else {
                ((BlockPortal) PanthalassaBlocks.PORTAL.get()).trySpawnPortal(worldIn, pos);
            }
        }
    }
}