package k4unl.minecraft.Hydraulicraft.blocks.consumers;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicWasher extends MachineBlockContainer {

	public BlockHydraulicWasher() {
		super(Names.blockHydraulicWasher);
		this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicWasher();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicWasher)){
			return false;
			
		}
		TileHydraulicWasher washer = (TileHydraulicWasher) entity;
		if(washer.getIsValidMultiblock()){
			player.openGui(Hydraulicraft.instance, GuiIDs.GUIWasher, world, x, y, z);
			return true;
		}
		
		return false;
	}

}
