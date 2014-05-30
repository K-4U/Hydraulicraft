package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterTrolley;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHarvesterTrolley extends HydraulicBlockContainerBase{
	private List<Integer> enabledHarvesters = new ArrayList<Integer>();
	
	public BlockHarvesterTrolley() {
		super(Names.blockHarvesterTrolley);
		
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
		return new TileHarvesterTrolley(mName.unlocalized);
	}


	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
	    NBTTagCompound tag = iStack.getTagCompound();
	    TileHarvesterTrolley teTrolley = (TileHarvesterTrolley)world.getTileEntity(x, y, z);
	    teTrolley.setTrolley(Hydraulicraft.harvesterTrolleyRegistrar.getTrolley(tag.getString("name")));
		super.onBlockPlacedBy(world, x, y, z, player, iStack);
	}
	
	 @SideOnly(Side.CLIENT)
	 public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
	     TileHarvesterTrolley teTrolley = (TileHarvesterTrolley)world.getTileEntity(x, y, z);
	     return Hydraulicraft.harvesterTrolleyRegistrar.getTrolleyItem(teTrolley.getTrolley().getName());
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

	@Override
	public int damageDropped(int damageValue){
		return damageValue;
	}

}
