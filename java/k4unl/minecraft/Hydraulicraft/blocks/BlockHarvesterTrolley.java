package k4unl.minecraft.Hydraulicraft.blocks;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicPiston;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterFrame;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHarvesterTrolley;
import k4unl.minecraft.Hydraulicraft.TileEntities.harvester.TileHydraulicHarvester;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineBlockContainer;
import k4unl.minecraft.Hydraulicraft.lib.config.Ids;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class BlockHarvesterTrolley extends MachineBlockContainer{
	private Icon[] icons;
	private Icon[] topIcons;
	private Icon[] bottomIcons;
	
	private Name[] mName;
	
	protected boolean hasTopIcon[] = {
			false
	};
	protected boolean hasBottomIcon[] = {
			false
	};
	
	
	protected BlockHarvesterTrolley() {
		super(Ids.blockHarvesterTrolley, Names.blockHarvesterTrolley[0]);
		
		mName = Names.blockHarvesterTrolley;
		
		icons = new Icon[mName.length];
		topIcons = new Icon[mName.length];
		bottomIcons = new Icon[mName.length];
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
	public TileEntity createNewTileEntity(World world) {
		return new TileHarvesterTrolley();
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
		for(int i = 0; i < mName.length; i++){
			icons[i] = iconRegistry.registerIcon(getTextureName("sides",i));
			if(hasTopIcon[i]){
				topIcons[i] = iconRegistry.registerIcon(getTextureName("top",i));
			}else{
				topIcons[i] = icons[i];
			}
			if(hasBottomIcon[i]){
				bottomIcons[i] = iconRegistry.registerIcon(getTextureName("bottom",i));
			}else{
				bottomIcons[i] = icons[i];
			}
		}
	}
	
	
	
	@Override
	public Icon getIcon(int side, int metadata){
		ForgeDirection s = ForgeDirection.getOrientation(side);
		if(s.equals(ForgeDirection.UP)){
			return topIcons[metadata];
		}else if(s.equals(ForgeDirection.DOWN)){
			return bottomIcons[metadata];
		}

		return icons[metadata];
		
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
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

        if(tileEntity instanceof TileHarvesterTrolley) {
            float extendedLength = ((TileHarvesterTrolley)tileEntity).getExtendedLength();
            float sidewaysMovement = ((TileHarvesterTrolley)tileEntity).getSideLength();

            //Get rotation:
            int dir = ((TileHarvesterTrolley) tileEntity).getDir();
            float minX = 0.0F;
            float minY = 0.8F;
            float minZ = 0.0F;
            float maxX = 1.0F;
            float maxY = 1.0F;
            float maxZ = 1.0F;
            
            
            int dirXMin = (dir == 1 ? -1  : 0);
            int dirZMin = (dir == 0 ? -1  : 0);
            int dirXMax = (dir == 3 ? 1  : 0);
            int dirZMax = (dir == 2 ? 1  : 0);
            minX += sidewaysMovement * dirXMin;
            minY -= extendedLength;
            minZ += sidewaysMovement * dirZMin;
            
            maxX += sidewaysMovement * dirXMax;
            //maxY += extendedLength;
            maxZ += sidewaysMovement * dirZMax;
            
            
            setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }
	

}
