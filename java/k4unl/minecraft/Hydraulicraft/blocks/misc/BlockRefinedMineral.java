package k4unl.minecraft.Hydraulicraft.blocks.misc;


import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;

public class BlockRefinedMineral extends BlockFalling {

    private Name oName;

    public BlockRefinedMineral(Name name) {

        super();

        setUnlocalizedName(name.unlocalized);
        oName = name;

        //TODO: FIX ME
        //setBlockTextureName(name.unlocalized);
        setSoundType(SoundType.SAND);
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setHardness(0.5F);
    }
/*
    @Override
    public void registerBlockIcons(IIconRegister iconRegistry){
        blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + oName.unlocalized);
    }

    @Override
    public IIcon getIcon(int side, int metadata){
        return blockIcon;
    }
    */
}
