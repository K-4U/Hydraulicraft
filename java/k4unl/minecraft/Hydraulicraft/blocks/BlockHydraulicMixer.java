package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicMixer;
import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicMixer extends MachineBlock {

	protected BlockHydraulicMixer() {
		super(Ids.blockHydraulicMixer, Names.blockHydraulicMixer);
		this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		// TODO Auto-generated method stub
		return new TileHydraulicMixer();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicMixer)){
			return false;
			
		}
		TileHydraulicMixer pump = (TileHydraulicMixer) entity;
		player.openGui(Hydraulicraft.instance, Ids.GUIMixer.act, world, x, y, z);
		
		return true;
	}
}
