package k4unl.minecraft.Hydraulicraft.blocks.gow;


import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;

public abstract class GOWBlockRendering extends GOWBlockBase {

	protected GOWBlockRendering(String name) {
		super(name);
		setBlockTextureName(ModInfo.LID + ":" + Names.blockHydraulicPressureWall.unlocalized + "/" + Names.blockHydraulicPressureWall.unlocalized + "_1");
	}

	@Override
	public int getRenderType(){
		return -1;
	}
	
	@Override
	public boolean isOpaqueCube(){
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock(){
		return false;
	}
}
