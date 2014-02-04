package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineTieredBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Id;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicRFPump;

public class BlockRFPump extends MachineTieredBlock {

	protected BlockRFPump() {
		super(Ids.blockRFPump, Names.blockRFPump);
		
		this.hasTopIcon = true;
	}


	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileHydraulicRFPump();
	}

	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicDynamo)){
			return false;
			
		}
		//TileHydraulicPneumaticCompressor compressor = (TileHydraulicPneumaticCompressor) entity;
		//player.openGui(Hydraulicraft.instance, Ids.GUIPneumaticCompressor.act, world, x, y, z);
		
		return true;
	}

}
