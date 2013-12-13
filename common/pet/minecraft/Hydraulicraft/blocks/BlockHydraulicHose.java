package pet.minecraft.Hydraulicraft.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicHose;
import pet.minecraft.Hydraulicraft.baseClasses.MachineTransporter;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicHose extends MachineTransporter {

	protected BlockHydraulicHose() {
		super(Ids.blockHydraulicHose, Names.blockHydraulicHose);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileHydraulicHose();
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
