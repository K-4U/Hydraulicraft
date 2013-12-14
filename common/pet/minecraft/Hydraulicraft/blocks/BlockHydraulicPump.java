package pet.minecraft.Hydraulicraft.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicPump;
import pet.minecraft.Hydraulicraft.baseClasses.MachineBlock;
import pet.minecraft.Hydraulicraft.lib.config.Ids;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class BlockHydraulicPump extends MachineBlock {

    protected BlockHydraulicPump() {
        super(Ids.blockHydraulicPump, Names.blockHydraulicPump);
        this.hasFrontIcon = true;
    }

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileHydraulicPump();
	}
	
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		
		return false;
	}
}
