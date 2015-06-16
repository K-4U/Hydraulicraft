package k4unl.minecraft.Hydraulicraft.ores;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.blocks.IGlowBlock;
import k4unl.minecraft.Hydraulicraft.client.renderers.misc.RendererGlowBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * @author Koen Beckers (K-4U)
 */
public class OreBeachium extends OreMineral implements IGlowBlock {
    private IIcon glowIcon;

    public OreBeachium() {

        super(Names.oreBeachium);
    }


    @Override
    public void registerBlockIcons(IIconRegister iconRegistry) {

        super.registerBlockIcons(iconRegistry);

        glowIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + Names.oreBeachium.unlocalized + "_glow");
    }

    @Override
    public IIcon getGlowIcon() {

        return glowIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RendererGlowBlock.RENDER_ID;
    }
}
