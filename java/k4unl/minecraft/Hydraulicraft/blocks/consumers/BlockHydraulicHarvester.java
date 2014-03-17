package k4unl.minecraft.Hydraulicraft.blocks.consumers;

import java.util.List;
import java.util.Map;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Vector3fMax;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockHydraulicHarvester extends MachineBlockContainer {
	private Icon blockIcon;
	private Name[] mName;

	public Vector3fMax blockBounds = new Vector3fMax(0.2f, 0.2f, 0.2f, 0.8F, 0.8F, 0.8F);
	
	protected boolean hasTopIcon[] = {
			false, false
	};
	protected boolean hasBottomIcon[] = {
			false, false
	};
	
	
	public BlockHydraulicHarvester() {
		super(Ids.blockHydraulicHarvester, Names.blockHydraulicHarvester[0]);
		
		mName = Names.blockHydraulicHarvester;
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
	public TileEntity createTileEntity(World world, int metadata){
		switch(metadata){
		case 0:
			return new TileHydraulicHarvester();
		case 1:
			return new TileHarvesterFrame();
		}
        return null;
    }
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

	@Override
	public void getSubBlocks(int id, CreativeTabs tab, List list){
		for(int i = 0; i < mName.length; i++){
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int par6, float par7, float par8, float par9) {
		
		TileEntity entity = world.getBlockTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileHydraulicHarvester)){
			return false;
			
		}
		
		TileHydraulicHarvester harvester = (TileHydraulicHarvester) entity;
		if(harvester.getIsMultiblock()){
			player.openGui(Hydraulicraft.instance, Ids.GUIHarvester.act, world, x, y, z);
			return true;
		}
		
		return false;
	}
	
	private String getTextureName(String side, int subId){
		if(side != ""){
			return ModInfo.LID + ":" + mName[subId].unlocalized + "_" + side;
		}else{
			return ModInfo.LID + ":" + mName[subId].unlocalized;
		}
	}
	
	@Override
	public void registerIcons(IconRegister iconRegistry){
		blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + Names.blockHydraulicPressureWall.unlocalized);
	}
	
	
	
	@Override
	public Icon getIcon(int side, int metadata){
		return blockIcon;
		
	}
	
	public void checkRotation(TileHarvesterFrame frame, EntityLivingBase player){
		int sideToPlace = MathHelper.floor_double((double)(player.rotationYaw / 90F) + 0.5D) & 3;
		
		boolean isRotated = false;
		switch(sideToPlace){
		case 0:
			isRotated = true; //C
			break;
		case 1:
			isRotated = false; //C
			break;
		case 2:
			isRotated = true; // C
			break;
		case 3:
			isRotated = false; //C
			break;
		}
		
		frame.setRotated(isRotated);
	}

	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
		super.onBlockPlacedBy(world, x, y, z, player, iStack);
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		
		if(tile instanceof TileHarvesterFrame){
			checkRotation((TileHarvesterFrame)tile, player);
		}
	}
	
	@Override
	public int damageDropped(int damageValue){
		return damageValue;
	}
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if(tileEntity instanceof TileHarvesterFrame) {
        	TileHarvesterFrame frame = (TileHarvesterFrame) tileEntity;
        	float minX = blockBounds.getXMin();
            float minY = blockBounds.getYMin();
            float minZ = blockBounds.getZMin();
            float maxX = blockBounds.getXMax();
            float maxY = blockBounds.getYMax();
            float maxZ = blockBounds.getZMax();
            
            if(!frame.getIsRotated()){
            	minX = 0.0F;
            	maxX = 1.0F;
            }else{
            	minZ = 0.0F;
            	maxZ = 1.0F;
            }
            
            setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }else{
        	setBlockBounds(0,0,0,1,1,1);
        }
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity){
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if(tileEntity instanceof TileHarvesterFrame) {
        	TileHarvesterFrame frame = (TileHarvesterFrame) tileEntity;
        	float minX = blockBounds.getXMin();
            float minY = blockBounds.getYMin();
            float minZ = blockBounds.getZMin();
            float maxX = blockBounds.getXMax();
            float maxY = blockBounds.getYMax();
            float maxZ = blockBounds.getZMax();
            
            if(!frame.getIsRotated()){
            	minX = 0.0F;
            	maxX = 1.0F;
            }else{
            	minZ = 0.0F;
            	maxZ = 1.0F;
            }
            
            super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
            setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }else{
        	super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
        	setBlockBounds(0,0,0,1,1,1);
        }
    }
	

}
