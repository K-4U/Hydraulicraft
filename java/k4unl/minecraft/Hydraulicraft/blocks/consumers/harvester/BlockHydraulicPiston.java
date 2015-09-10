package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.client.renderers.consumers.RendererHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicPiston;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockHydraulicPiston extends HydraulicBlockContainerBase {

	public BlockHydraulicPiston() {
		super(Names.blockHydraulicPiston, true);
		hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicPiston();
	}

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType(){
        return RendererHydraulicPiston.RENDER_ID;
    }
	
	@Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public boolean renderAsNormalBlock(){
        return true;
    }
    
    @SuppressWarnings("cast")
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
		if(hasFrontIcon){
			int sideToPlace = MathHelper.floor_double((double)(player.rotationYaw / 90F) + 0.5D) & 3;
			
			
			int metaDataToSet = 0;
			switch(sideToPlace){
			case 0:
				metaDataToSet = 2;
				break;
			case 1:
				metaDataToSet = 5;
				break;
			case 2:
				metaDataToSet = 3;
				break;
			case 3:
				metaDataToSet = 4;
				break;
			}
			
			TileEntity pEnt = world.getTileEntity(x, y, z);
			if(pEnt instanceof TileHydraulicPiston){
				((TileHydraulicPiston)pEnt).setFacing(ForgeDirection.getOrientation(metaDataToSet));
			}
            super.onBlockPlacedBy(world, x, y, z, player, iStack);
		}
	}
}
