package pet.minecraft.Hydraulicraft.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pet.minecraft.Hydraulicraft.Hydraulicraft;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicCrusher;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicPump;
import pet.minecraft.Hydraulicraft.baseClasses.MachineBlock;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicCrusher extends MachineBlock {

	protected BlockHydraulicCrusher() {
		super(Ids.blockHydraulicCrusher, Names.blockHydraulicCrusher);
		this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileHydraulicCrusher();
	}
	
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicCrusher)){
			return false;
			
		}
		TileHydraulicCrusher pump = (TileHydraulicCrusher) entity;
		player.openGui(Hydraulicraft.instance, Ids.GUICrusher.act, world, x, y, z);
		
		return true;
	}
}
