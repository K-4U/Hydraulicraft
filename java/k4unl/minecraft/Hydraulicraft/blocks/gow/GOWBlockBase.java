package k4unl.minecraft.Hydraulicraft.blocks.gow;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class GOWBlockBase extends BlockContainer {
	private String tName;
	
	protected GOWBlockBase(String name) {
		super(Material.rock);
		tName = name;

		setBlockName(tName);
		setStepSound(Block.soundTypeStone);
		setHardness(3.5F);
		setResistance(10F);		
	}

	protected abstract Class<? extends TileEntity> getTileEntity();
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		try{
			return getTileEntity().newInstance();
		}catch(Exception e){
			return null;
		}
	}

}
