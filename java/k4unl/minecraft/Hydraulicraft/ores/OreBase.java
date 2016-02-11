package k4unl.minecraft.Hydraulicraft.ores;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class OreBase extends Block {

    private Name oName;

    public OreBase(Name oreName) {

        super(Material.rock);

        oName = oreName;

        setUnlocalizedName(oName.unlocalized);
        setStepSound(Block.soundTypeStone);
        setHardness(3.5F);

        setCreativeTab(CustomTabs.tabHydraulicraft);

        setHarvestLevel("pickaxe", 1);
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
