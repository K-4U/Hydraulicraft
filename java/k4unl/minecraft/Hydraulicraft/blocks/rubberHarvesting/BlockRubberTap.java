package k4unl.minecraft.Hydraulicraft.blocks.rubberHarvesting;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockBase;
import k4unl.minecraft.Hydraulicraft.blocks.IBlockWithRotation;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.block.material.Material;

/**
 * @author Koen Beckers (K-4U)
 */
public class BlockRubberTap extends HydraulicBlockBase implements IBlockWithRotation {
    
    public BlockRubberTap() {

        super(Names.blockRubberTap, Material.iron, false);
        setStepSound(soundTypeAnvil);
    }

    @Override
    public boolean isFullBlock() {

        return false;
    }

    @Override
    public boolean isOpaqueCube() {

        return false;
    }
}
