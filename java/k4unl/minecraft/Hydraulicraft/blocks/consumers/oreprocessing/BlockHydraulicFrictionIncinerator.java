package k4unl.minecraft.Hydraulicraft.blocks.consumers.oreprocessing;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFrictionIncinerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHydraulicFrictionIncinerator extends HydraulicBlockContainerBase implements ITieredBlock {

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
		
		player.openGui(Hydraulicraft.instance, GuiIDs.INCINERATOR.ordinal(), w, x, y, z);
		
		return true;
	}

    @Override
    public PressureTier getTier() {

        return PressureTier.MEDIUMPRESSURE;
    }
}
