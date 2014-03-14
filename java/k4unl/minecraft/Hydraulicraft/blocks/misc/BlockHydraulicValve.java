package k4unl.minecraft.Hydraulicraft.blocks.misc;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import k4unl.minecraft.Hydraulicraft.TileEntities.misc.TileHydraulicValve;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlock;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Id;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class BlockHydraulicValve extends MachineBlockContainer {
	private Icon rotatedIcon;
	private Icon blockIcon;
	
	
	
	public BlockHydraulicValve() {
		super(Ids.blockValve, Names.blockValve);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileHydraulicValve();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		return false;
	}
	
	@Override
	public void registerIcons(IconRegister iconRegistry){
		blockIcon = iconRegistry.registerIcon(super.getTextureName(null));
		rotatedIcon = iconRegistry.registerIcon(super.getTextureName("rotated"));
	}
	
	@Override
	public Icon getIcon(int side, int metadata){
		ForgeDirection s = ForgeDirection.getOrientation(side);
		if(s.equals(ForgeDirection.SOUTH) || s.equals(ForgeDirection.WEST)){
			return rotatedIcon;
		}
		return blockIcon;
	}

}
