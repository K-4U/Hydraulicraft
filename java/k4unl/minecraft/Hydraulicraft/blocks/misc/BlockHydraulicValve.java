package k4unl.minecraft.Hydraulicraft.blocks.misc;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileHydraulicValve;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockHydraulicValve extends HydraulicBlockContainerBase {
	private IIcon rotatedIcon;
	private IIcon blockIcon;
	
	
	
	public BlockHydraulicValve() {
		super(Names.blockValve);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicValve();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		return false;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegistry){
		blockIcon = iconRegistry.registerIcon(super.getTextureName(null));
		rotatedIcon = iconRegistry.registerIcon(super.getTextureName("rotated"));
	}
	
	@Override
	public IIcon getIcon(int side, int metadata){
		ForgeDirection s = ForgeDirection.getOrientation(side);
		if(s.equals(ForgeDirection.SOUTH) || s.equals(ForgeDirection.WEST)){
			return rotatedIcon;
		}
		return blockIcon;
	}

}
