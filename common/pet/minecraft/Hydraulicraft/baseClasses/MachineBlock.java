package pet.minecraft.Hydraulicraft.baseClasses;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import pet.minecraft.Hydraulicraft.lib.CustomTabs;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Id;
import pet.minecraft.Hydraulicraft.lib.helperClasses.Name;

public class MachineBlock extends BlockContainer {
	private Icon blockIcon;
	private Icon topIcon;
	private Icon bottomIcon;
	private Icon frontIcon;
	
	private Id tBlockId;
	private Name mName;
	
	protected boolean hasBottomIcon = false;
	protected boolean hasTopIcon = false;
	protected boolean hasFrontIcon = false;
	
	protected MachineBlock(Id blockId, Name machineName) {
		super(blockId.act, Material.rock);
		
		tBlockId = blockId;
		mName = machineName;
		
		setUnlocalizedName(mName.unlocalized);
		setStepSound(Block.soundStoneFootstep);
		setHardness(3.5F);
		
		setCreativeTab(CustomTabs.tabHydraulicraft);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}
	
	@Override
	public void registerIcons(IconRegister iconRegistry){
		if(hasTopIcon || hasBottomIcon || hasFrontIcon){
			blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + mName.unlocalized + "_sides");
			if(hasTopIcon){
				topIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + mName.unlocalized + "_top");
			}else{
				topIcon = blockIcon;
			}
			if(hasBottomIcon){
				bottomIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + mName.unlocalized + "_bottom");
			}else{
				bottomIcon = blockIcon;
			}
			if(hasFrontIcon){
				frontIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + mName.unlocalized + "_front");
			}else{
				frontIcon = blockIcon;
			}
		}else{
			blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + mName.unlocalized);
			bottomIcon = blockIcon;
			topIcon = blockIcon;
			frontIcon = blockIcon;
		}
	}
	
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z){
		super.onBlockAdded(world, x, y, z);
		setDefaultDirection(world, x, y, z);
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving){
	int l = MathHelper.floor_double((double)((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

	switch (l){
		case 0:
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
			break;
	
		case 1:
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
			break;
	
		case 2:
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
			break;
	
		case 3:
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
			break;
		}
	}
	
	private void setDefaultDirection(World world, int x, int y, int z){
		if(!world.isRemote){ //Client, not server
			int zm1 = world.getBlockId(x, y, z - 1);
			int zp1 = world.getBlockId(x, y, z + 1);
			int xm1 = world.getBlockId(x - 1, y, z);
			int xp1 = world.getBlockId(x + 1, y, z);
			
			int metaDataToSet = 3;
			
			if(Block.opaqueCubeLookup[zm1] && !Block.opaqueCubeLookup[zp1]){
				metaDataToSet = 3;
			}
			
			if(!Block.opaqueCubeLookup[zm1] && Block.opaqueCubeLookup[zp1]){
				metaDataToSet = 2;
			}
			
			if(Block.opaqueCubeLookup[xm1] && !Block.opaqueCubeLookup[xp1]){
				metaDataToSet = 5;
			}
			
			if(!Block.opaqueCubeLookup[xm1] && Block.opaqueCubeLookup[xp1]){
				metaDataToSet = 4;
			}
			
			world.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
		}
	}
	
	
	
	@Override
	public Icon getIcon(int side, int metadata){
		ForgeDirection s = ForgeDirection.getOrientation(side);
		if(s.equals(ForgeDirection.UP)){
			return topIcon;
		}else if(s.equals(ForgeDirection.DOWN)){
			return bottomIcon;
		}
		
		if(side == metadata){
			return frontIcon; 
		}
		return blockIcon;
	}
	
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
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
		
		world.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
	}
	
	
	
}
