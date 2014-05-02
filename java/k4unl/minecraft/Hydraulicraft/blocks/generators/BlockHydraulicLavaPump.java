package k4unl.minecraft.Hydraulicraft.blocks.generators;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicLavaPump;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHydraulicLavaPump extends HydraulicTieredBlockBase {

	public BlockHydraulicLavaPump() {
        super(Names.blockHydraulicLavaPump);
        this.hasTextures = false;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicLavaPump(metadata);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
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
	
    @Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
		int sideToPlace = MathHelper.floor_double(player.rotationYaw / 90F + 0.5D) & 3;
		
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
		ForgeDirection facing = ForgeDirection.getOrientation(metaDataToSet);
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity != null && entity instanceof TileHydraulicLavaPump){
			((TileHydraulicLavaPump)entity).setFacing(facing);
		}
		
		//world.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
	}
    
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicLavaPump)){
			return false;
			
		}
		TileHydraulicLavaPump pump = (TileHydraulicLavaPump) entity;
		player.openGui(Hydraulicraft.instance, GuiIDs.GUILavaPump, world, x, y, z);
		
		return true;
	}
}
