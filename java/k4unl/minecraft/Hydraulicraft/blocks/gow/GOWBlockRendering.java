package k4unl.minecraft.Hydraulicraft.blocks.gow;


public abstract class GOWBlockRendering extends GOWBlockBase {

	protected GOWBlockRendering(String name) {
		super(name);
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
