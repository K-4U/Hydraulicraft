package k4unl.minecraft.Hydraulicraft.ores;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;

/**
 * @author K-4U
 */
public class OreMineral extends BlockFalling {

    private Name oName;

    public OreMineral(Name name) {

        super(Material.sand);

        //setBlockName(name.unlocalized);
        oName = name;


        setUnlocalizedName(name.unlocalized);
        setStepSound(Block.soundTypeGravel);
        setCreativeTab(CustomTabs.tabHydraulicraft);
        setHardness(0.5F);
        setHarvestLevel("shovel", 0);
    }
/*
    @Override
    public void registerBlockIcons(IIconRegister iconRegistry){
        blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + oName.unlocalized);
    }

    @Override
    public IIcon getIcon(int side, int metadata){
        return blockIcon;
    }*/
}
