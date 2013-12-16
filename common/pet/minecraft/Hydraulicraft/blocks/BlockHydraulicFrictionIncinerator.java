package pet.minecraft.Hydraulicraft.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pet.minecraft.Hydraulicraft.Hydraulicraft;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicFrictionIncinerator;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicPump;
import pet.minecraft.Hydraulicraft.baseClasses.MachineBlock;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicFrictionIncinerator extends MachineBlock {

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
