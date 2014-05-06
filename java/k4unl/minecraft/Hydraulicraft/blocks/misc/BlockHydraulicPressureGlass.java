package k4unl.minecraft.Hydraulicraft.blocks.misc;

import net.minecraft.block.material.Material;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockBase;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicPressureGlass extends HydraulicBlockBase {

	public BlockHydraulicPressureGlass() {
		super(Names.blockHydraulicPressureGlass, Material.glass);
	}
	
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass()
    {
        return 0;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

}
