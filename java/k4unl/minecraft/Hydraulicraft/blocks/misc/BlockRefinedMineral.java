package k4unl.minecraft.Hydraulicraft.blocks.misc;


import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockRefinedMineral extends BlockFalling {
    private Name oName;

    public BlockRefinedMineral(Name name){
        super();

        setBlockName(name.unlocalized);
        oName = name;

        setBlockTextureName(name.unlocalized);
        setStepSound(Block.soundTypeSand);
        setCreativeTab(CustomTabs.tabHydraulicraft);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegistry){
        blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + oName.unlocalized);
    }

    @Override
    public IIcon getIcon(int side, int metadata){
        return blockIcon;
    }
}
