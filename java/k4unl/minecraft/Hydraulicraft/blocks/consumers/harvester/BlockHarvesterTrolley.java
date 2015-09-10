package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterTrolley;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockHarvesterTrolley extends HydraulicBlockContainerBase{
	private List<Integer> enabledHarvesters = new ArrayList<Integer>();
	
	public BlockHarvesterTrolley() {
		super(Names.blockHarvesterTrolley, true);
		
		mName = Names.blockHarvesterTrolley;
		hasTextures = false;
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
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileHarvesterTrolley();
	}

    @Override
    public GuiIDs getGUIID() {

        return GuiIDs.INVALID;
    }
    
    @Override
    public int quantityDropped(Random p_149745_1_){
        return 0;
    }

    @Override
    public void breakBlock(World w, int x, int y, int z, Block oldBlock, int oldMetaData){
        //Call TileEntity's onBlockBreaks function
        TileEntity tile = w.getTileEntity(x, y, z);
        if(tile instanceof TileHarvesterTrolley){
            ((TileHarvesterTrolley)tile).onBlockBreaks();
        }

        super.breakBlock(w, x, y, z, oldBlock, oldMetaData);
        w.removeTileEntity(x, y, z);
    }

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
	    NBTTagCompound tag = iStack.getTagCompound();
	    TileHarvesterTrolley teTrolley = (TileHarvesterTrolley)world.getTileEntity(x, y, z);
	    teTrolley.setTrolley(Hydraulicraft.trolleyRegistrar.getTrolley(tag.getString("name")));
		super.onBlockPlacedBy(world, x, y, z, player, iStack);
	}
	
	 @SideOnly(Side.CLIENT)
	 public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player){
	     TileHarvesterTrolley teTrolley = (TileHarvesterTrolley)world.getTileEntity(x, y, z);
	     return Hydraulicraft.trolleyRegistrar.getTrolleyItem(teTrolley.getTrolley().getName());
	 }
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
        TileEntity tileEntity = world.getTileEntity(x, y, z);

        if(tileEntity instanceof TileHarvesterTrolley) {
        	TileHarvesterTrolley ht = ((TileHarvesterTrolley) tileEntity);
        	float extendedLength = ht.getExtendedLength();
            float sidewaysMovement = ht.getSideLength();

            //Get rotation:
            
            int dir = ht.getFacing().ordinal();
            float minX = 0.0F;
            float minY = 0.8F;
            float minZ = 0.0F;
            float maxX = 1.0F;
            float maxY = 1.0F;
            float maxZ = 1.0F;
            int dirXMin = (dir == 1 ? -1 : 0);
            int dirZMin = (dir == 2 ? -1 : 0);
            int dirXMax = (dir == 0 ? 1 : 0);
            int dirZMax = (dir == 3 ? 1 : 0);
            
            
            minX += sidewaysMovement * dirXMin;
            minY -= extendedLength;
            minZ += sidewaysMovement * dirZMin;
            
            maxX += sidewaysMovement * dirXMax;
            //maxY += extendedLength;
            maxZ += sidewaysMovement * dirZMax;
            
            
            setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }

	/*@Override
	public int damageDropped(int damageValue){
		return damageValue;
	}
*/

}
