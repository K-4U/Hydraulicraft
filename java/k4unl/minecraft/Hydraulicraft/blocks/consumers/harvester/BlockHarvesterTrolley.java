package k4unl.minecraft.Hydraulicraft.blocks.consumers.harvester;

import java.util.ArrayList;
import java.util.List;

import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import k4unl.minecraft.Hydraulicraft.tileEntities.harvester.TileHarvesterTrolley;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockHarvesterTrolley extends HydraulicBlockContainerBase{
	private Name[] mName;
	private List<Integer> enabledHarvesters = new ArrayList<Integer>();
	
	
	public BlockHarvesterTrolley() {
		super(Names.blockHarvesterTrolley[0]);
		
		mName = Names.blockHarvesterTrolley;
		enabledHarvesters.add(0);
		hasTextures = false;
		enabledHarvesters.add(Constants.HARVESTER_ID_SUGARCANE);
	}
	
	public void enableHarvester(int id){
		enabledHarvesters.add(id);
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
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileHarvesterTrolley();
	}

	@Override
	public void getSubBlocks(Item block, CreativeTabs tab, List list){
		for(int i : enabledHarvesters){
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
		super.onBlockPlacedBy(world, x, y, z, player, iStack);
		TileEntity tile = world.getTileEntity(x, y, z);
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
