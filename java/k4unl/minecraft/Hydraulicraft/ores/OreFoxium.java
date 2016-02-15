package k4unl.minecraft.Hydraulicraft.ores;

import k4unl.minecraft.Hydraulicraft.blocks.IGlowBlock;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

public class OreFoxium extends Block implements IGlowBlock {

    private Name oName = Names.oreFoxium;

    public OreFoxium() {

        super(Material.rock);

        setUnlocalizedName(oName.unlocalized);

        //setBlockTextureName(oName.unlocalized);
        setStepSound(Block.soundTypeStone);
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setHarvestLevel("pickaxe", 3);
        setHardness(0.4F); //Same as netherrack
    }
/*
    @Override
    public void registerBlockIcons(IIconRegister iconRegistry){
        glowIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + oName.unlocalized + "_glow");
        blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + oName.unlocalized);
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

    @Override
    public IIcon getIcon(int side, int metadata){
        return blockIcon;
    }*/

    @Override
    public MapColor getMapColor(IBlockState state) {

        return MapColor.netherrackColor;
    }

}
