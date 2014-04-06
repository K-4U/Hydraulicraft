package k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicFrictionIncinerator extends MachineBlockContainer {

	public BlockHydraulicFrictionIncinerator() {
		super(Names.blockHydraulicFrictionIncinerator);
		this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileHydraulicFrictionIncinerator();
	}	
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int a, float sideX, float sideY, float sideZ) {
		if(player.isSneaking())
			return false;
		
		TileEntity entity = w.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicFrictionIncinerator)){
			return false;
			
		}
		TileHydraulicFrictionIncinerator pump = (TileHydraulicFrictionIncinerator) entity;
		player.openGui(Hydraulicraft.instance, GuiIDs.GUIIncinerator, w, x, y, z);
		
		return true;
	}
}
