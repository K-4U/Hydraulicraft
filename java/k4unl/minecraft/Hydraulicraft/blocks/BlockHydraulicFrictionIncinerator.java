package k4unl.minecraft.Hydraulicraft.blocks;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicPump;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicFrictionIncinerator extends MachineBlockContainer {

	protected BlockHydraulicFrictionIncinerator() {
		super(Ids.blockHydraulicFrictionIncinerator, Names.blockHydraulicFrictionIncinerator);
		this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileHydraulicFrictionIncinerator();
	}	
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int a, float sideX, float sideY, float sideZ) {
		if(player.isSneaking())
			return false;
		
		TileEntity entity = w.getBlockTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicFrictionIncinerator)){
			return false;
			
		}
		TileHydraulicFrictionIncinerator pump = (TileHydraulicFrictionIncinerator) entity;
		player.openGui(Hydraulicraft.instance, Ids.GUIIncinerator.act, w, x, y, z);
		
		return true;
	}
}
