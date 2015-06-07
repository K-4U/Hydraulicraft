package k4unl.minecraft.Hydraulicraft.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public interface IGlowBlock {

    @SideOnly(Side.CLIENT)
    public IIcon getGlowIcon();
}
