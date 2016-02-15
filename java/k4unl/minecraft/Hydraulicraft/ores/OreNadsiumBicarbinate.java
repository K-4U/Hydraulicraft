package k4unl.minecraft.Hydraulicraft.ores;


import k4unl.minecraft.Hydraulicraft.blocks.IGlowBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;

/**
 * @author Koen Beckers (K-4U)
 */
public class OreNadsiumBicarbinate extends OreMineral implements IGlowBlock {

    //private IIcon glowIcon;
    public OreNadsiumBicarbinate() {

        super(Names.oreNadsiumBicarbinate);
    }

/*
    @Override
    public void registerBlockIcons(IIconRegister iconRegistry){
        super.registerBlockIcons(iconRegistry);

        glowIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + Names.oreNadsiumBicarbinate.unlocalized + "_glow");
    }

    @Override
    public IIcon getGlowIcon() {
        return glowIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RendererGlowBlock.RENDER_ID;
    }*/
}
