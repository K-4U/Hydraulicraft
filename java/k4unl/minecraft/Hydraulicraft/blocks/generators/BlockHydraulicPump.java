package k4unl.minecraft.Hydraulicraft.blocks.generators;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicTieredBlockBase;
import k4unl.minecraft.Hydraulicraft.api.IMultiTieredBlock;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.generator.TileHydraulicPump;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHydraulicPump extends HydraulicTieredBlockBase implements IMultiTieredBlock{

	public BlockHydraulicPump() {
        super(Names.blockHydraulicPump);
        this.hasTextures = false;
    }

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicPump(metadata);
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
		if(entity != null && entity instanceof TileHydraulicPump){
			((TileHydraulicPump)entity).setFacing(facing);
		}
		
		//world.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
	}
    
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicPump)){
			return false;
			
		}
		
		player.openGui(Hydraulicraft.instance, GuiIDs.PUMP.ordinal(), world, x, y, z);
		
		return true;
	}

    @Override
    public PressureTier getTier(int metadata) {

        return PressureTier.fromOrdinal(metadata);
    }

    @Override
    public PressureTier getTier(IBlockAccess world, int x, int y, int z){

        return PressureTier.fromOrdinal(world.getBlockMetadata(x, y, z));
    }
}
