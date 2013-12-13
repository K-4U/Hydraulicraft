package pet.minecraft.Hydraulicraft.baseClasses;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
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
	
	private Id tBlockId;
	private Name mName;
	
	protected boolean hasBottomIcon = false;
	protected boolean hasTopIcon = false;
	
	
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
		if(hasTopIcon || hasBottomIcon){
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
		}else{
			blockIcon = iconRegistry.registerIcon(ModInfo.LID + ":" + mName.unlocalized);
			bottomIcon = blockIcon;
			topIcon = blockIcon;
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
		return blockIcon;
	}
	
	
}
