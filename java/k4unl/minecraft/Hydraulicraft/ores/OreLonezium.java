package k4unl.minecraft.Hydraulicraft.ores;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.blocks.IGlowBlock;
import k4unl.minecraft.Hydraulicraft.client.renderers.misc.RendererGlowBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class OreLonezium extends OreMineral implements IGlowBlock {

    private IIcon glowIcon;
    public OreLonezium() {
        super(Names.oreLonezium);
    }


    @Override
    public void registerBlockIcons(IIconRegister iconRegistry){
        super.registerBlockIcons(iconRegistry);

        glowIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + Names.oreLonezium.unlocalized + "_glow");
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
