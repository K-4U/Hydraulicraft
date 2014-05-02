package k4unl.minecraft.Hydraulicraft.baseClasses;


import k4unl.minecraft.Hydraulicraft.TileEntities.TileHydraulicBase;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class MachineBlockContainer extends BlockContainer {
	private IIcon blockIcon;
	private IIcon topIcon;
	private IIcon bottomIcon;
	private IIcon frontIcon;

	public Name mName;
	
	protected boolean hasBottomIcon = false;
	protected boolean hasTopIcon = false;
	protected boolean hasFrontIcon = false;
	
	protected boolean hasTextures = true;
	
	@Override
	public abstract TileEntity createNewTileEntity(World world, int var2);
	
	@Override
	public abstract boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9);
	
	protected MachineBlockContainer(Name machineName) {
		super(Material.rock);
		
		mName = machineName;
		
		setBlockName(mName.unlocalized);
		setStepSound(Block.soundTypeStone);
		setHardness(3.5F);
		setResistance(10F);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}
	
	
	protected String getTextureName(String side){
		if(side != null){
			return ModInfo.LID + ":" + mName.unlocalized + "_" + side;
		}else{
			return ModInfo.LID + ":" + mName.unlocalized;
		}
	}
	
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegistry){
		if(hasTextures){
			if(hasTopIcon || hasBottomIcon || hasFrontIcon){
				blockIcon = iconRegistry.registerIcon(getTextureName("sides"));
				if(hasTopIcon){
					topIcon = iconRegistry.registerIcon(getTextureName("top"));
				}else{
					topIcon = blockIcon;
				}
				if(hasBottomIcon){
					bottomIcon = iconRegistry.registerIcon(getTextureName("bottom"));
				}else{
					bottomIcon = blockIcon;
				}
				if(hasFrontIcon){
					frontIcon = iconRegistry.registerIcon(getTextureName("front"));
				}else{
					frontIcon = blockIcon;
				}
			}else{
				blockIcon = iconRegistry.registerIcon(getTextureName(null));
				bottomIcon = blockIcon;
				topIcon = blockIcon;
				frontIcon = blockIcon;
			}
		}else{
			blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + Names.blockHydraulicPressureWall.unlocalized);
			bottomIcon = blockIcon;
			topIcon = blockIcon;
			frontIcon = blockIcon;
		}
	}
	
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z){
		super.onBlockAdded(world, x, y, z);
		if(hasFrontIcon){
			setDefaultDirection(world, x, y, z);
		}
	}
	
	private void setDefaultDirection(World world, int x, int y, int z){
		if(!world.isRemote){ //Client, not server
			Block zm1 = world.getBlock(x, y, z - 1);
			Block zp1 = world.getBlock(x, y, z + 1);
			Block xm1 = world.getBlock(x - 1, y, z);
			Block xp1 = world.getBlock(x + 1, y, z);
			
			int metaDataToSet = 3;
			
			if (zm1.func_149730_j() && !zp1.func_149730_j()){
				metaDataToSet = 3;
            }

            if (zp1.func_149730_j() && !zm1.func_149730_j()){
            	metaDataToSet = 2;
            }

            if (xm1.func_149730_j() && !xp1.func_149730_j()){
            	metaDataToSet = 5;
            }

            if (xp1.func_149730_j() && !xm1.func_149730_j()){
            	metaDataToSet = 4;
            }
			
			world.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
		}
	}
	
	
	
	@Override
	public IIcon getIcon(int side, int metadata){
		ForgeDirection s = ForgeDirection.getOrientation(side);
		if(s.equals(ForgeDirection.UP)){
			return topIcon;
		}else if(s.equals(ForgeDirection.DOWN)){
			return bottomIcon;
		}
		if(hasFrontIcon){
			if(metadata > 0){
				if(side == metadata){
					return frontIcon; 
				}
			}else{
				if(side == 3){
					return frontIcon;
				}
			}
		}
		return blockIcon;
	}
	
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
		if(hasFrontIcon){
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
			
			world.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		super.onNeighborBlockChange(world, x, y, z, block);
	}
	
	@Override
	public void breakBlock(World w, int x, int y, int z, Block oldBlock, int oldMetaData){
		//Call TileEntity's onBlockBreaks function
		TileEntity tile = w.getTileEntity(x, y, z);
		if(tile instanceof TileHydraulicBase){
			((TileHydraulicBase)tile).onBlockBreaks();
		}
		
		super.breakBlock(w, x, y, z, oldBlock, oldMetaData);
	}
	
	
}
