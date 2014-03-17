package k4unl.minecraft.Hydraulicraft.baseClasses;

import java.util.List;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicGenerator;
import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Id;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public abstract class MachineTieredBlock extends MachineBlockContainer {
		private Icon[] tieredIcon;
		private Icon[] tieredTopIcon;
		private Icon[] tieredBottomIcon;
		
		private Id tBlockId;
		private Name[] mName;
		
		protected boolean hasTopIcon = false;
		protected boolean hasBottomIcon = false;
		protected boolean hasTextures = true;
		
		
		@Override
		public abstract TileEntity createNewTileEntity(World world);

		
		protected MachineTieredBlock(Id blockId, Name[] machineName) {
			super(blockId, machineName[0]);
			
			tBlockId = blockId;
			mName = machineName;
			
			tieredIcon = new Icon[3];
			tieredTopIcon = new Icon[3];
			tieredBottomIcon = new Icon[3];
			
			
			setUnlocalizedName(mName[0].unlocalized);
			setStepSound(Block.soundStoneFootstep);
			setHardness(3.5F);
			
			setCreativeTab(CustomTabs.tabHydraulicraft);
		}
		
		
		@Override
		public void getSubBlocks(int id, CreativeTabs tab, List list){
			for(int i = 0; i < 3; i++){
				list.add(new ItemStack(this, 1, i));
			}
		}
		
		private String getTieredTextureName(String side, int tier){
			if(side != ""){
				return ModInfo.LID + ":" + mName[0].unlocalized + "_" + tier + "_" + side;
			}else{
				return ModInfo.LID + ":" + mName[0].unlocalized + "_" + tier;
			}
		}
		
		@Override
		public void registerIcons(IconRegister iconRegistry){
			if(hasTextures){
				if(hasTopIcon || hasBottomIcon){
					for(int i = 0; i < 3; i++){
						tieredIcon[i] = iconRegistry.registerIcon(getTieredTextureName("sides",i));
						if(hasTopIcon){
							tieredTopIcon[i] = iconRegistry.registerIcon(getTieredTextureName("top",i));
						}else{
							tieredTopIcon[i] = tieredIcon[i];
						}
						if(hasBottomIcon){
							tieredBottomIcon[i] = iconRegistry.registerIcon(getTieredTextureName("bottom",i));
						}else{
							tieredBottomIcon[i] = tieredIcon[i];
						}
					}
				}else{
					for(int i = 0; i < 3; i++){
						tieredIcon[i] = iconRegistry.registerIcon(getTieredTextureName("",i));
						tieredBottomIcon[i] = tieredIcon[i];
						tieredTopIcon[i] = tieredIcon[i];
					}
				}
			}else{
				for(int i = 0; i < 3; i++){
					tieredIcon[i] = iconRegistry.registerIcon(ModInfo.LID + ":" + Names.blockHydraulicPressureWall.unlocalized);
					tieredBottomIcon[i] = tieredIcon[i];
					tieredTopIcon[i] = tieredIcon[i];
				}
			}
		}
		
		
		@Override
		public Icon getIcon(int side, int metadata){
			ForgeDirection s = ForgeDirection.getOrientation(side);
			if(s.equals(ForgeDirection.UP)){
				return tieredTopIcon[metadata];
			}else if(s.equals(ForgeDirection.DOWN)){
				return tieredBottomIcon[metadata];
			}

			return tieredIcon[metadata];
			
		}
		
		@Override
		public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
			return false;
		}
		
		@Override
		public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
			super.onBlockPlacedBy(world, x, y, z, player, iStack);
		}
		
		@Override
		public int damageDropped(int damageValue){
			return damageValue;
		}
		
		@Override
		public void onNeighborBlockChange(World world, int x, int y,
					int z, int blockId) {
			super.onNeighborBlockChange(world, x, y, z, blockId);
			TileEntity t = world.getBlockTileEntity(x, y, z);
			if(t instanceof IHydraulicMachine){
				((IHydraulicMachine)t).getHandler().checkRedstonePower();
			}
			
		}
}
