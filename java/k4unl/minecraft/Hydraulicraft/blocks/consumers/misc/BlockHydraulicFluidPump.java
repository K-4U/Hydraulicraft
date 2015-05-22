package k4unl.minecraft.Hydraulicraft.blocks.consumers.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.ITooltipProvider;
import k4unl.minecraft.Hydraulicraft.client.renderers.misc.RendererArchimedesScrew;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFluidPump;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockHydraulicFluidPump extends HydraulicBlockContainerBase {

	public BlockHydraulicFluidPump() {
		super(Names.blockHydraulicFluidPump);
		//this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicFluidPump();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicFluidPump)){
			return false;
			
		}
		
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
		//if(hasFrontIcon){
			int sideToPlace = MathHelper.floor_double((double) (player.rotationYaw / 90F) + 0.5D) & 3;


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
			if(pEnt instanceof TileHydraulicFluidPump){
				((TileHydraulicFluidPump)pEnt).setFacing(ForgeDirection.getOrientation(metaDataToSet).getOpposite());
			}
		//}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {

		return RendererArchimedesScrew.RENDER_ID;
	}
    
}
