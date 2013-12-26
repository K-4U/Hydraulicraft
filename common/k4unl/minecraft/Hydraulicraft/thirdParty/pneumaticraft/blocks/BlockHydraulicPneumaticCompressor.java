package k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicFrictionIncinerator;
import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicWasher;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Id;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import k4unl.minecraft.Hydraulicraft.thirdParty.pneumaticraft.tileEntities.TileHydraulicPneumaticCompressor;

public class BlockHydraulicPneumaticCompressor extends MachineBlock {

	public BlockHydraulicPneumaticCompressor() {
		super(Ids.blockHydraulicPneumaticCompressor, Names.blockHydraulicPneumaticCompressor);
		this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileHydraulicPneumaticCompressor();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicPneumaticCompressor)){
			return false;
			
		}
		//TileHydraulicPneumaticCompressor compressor = (TileHydraulicPneumaticCompressor) entity;
		player.openGui(Hydraulicraft.instance, Ids.GUIPneumaticCompressor.act, world, x, y, z);
		
		return true;
	}

}
