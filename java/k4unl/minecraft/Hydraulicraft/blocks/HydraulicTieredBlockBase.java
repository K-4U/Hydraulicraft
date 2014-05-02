package k4unl.minecraft.Hydraulicraft.blocks;

import java.util.List;

import k4unl.minecraft.Hydraulicraft.lib.CustomTabs;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.lib.helperClasses.Name;
import k4unl.minecraft.Hydraulicraft.tileEntities.TileHydraulicBase;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class HydraulicTieredBlockBase extends HydraulicBlockContainerBase {
		private IIcon[] tieredIcon;
		private IIcon[] tieredTopIcon;
		private IIcon[] tieredBottomIcon;
		
		private Name[] mName;
		
		protected boolean hasTopIcon = false;
		protected boolean hasBottomIcon = false;
		protected boolean hasTextures = true;
		
		
		@Override
		public abstract TileEntity createNewTileEntity(World world, int var2);

		
		protected HydraulicTieredBlockBase(Name[] machineName) {
			super(machineName[0]);
			
			mName = machineName;
			
			tieredIcon = new IIcon[3];
			tieredTopIcon = new IIcon[3];
			tieredBottomIcon = new IIcon[3];
			
			
			setBlockName(mName[0].unlocalized);
			setStepSound(Block.soundTypeStone);
			setHardness(3.5F);
			
			setCreativeTab(CustomTabs.tabHydraulicraft);
		}
		
		
		@Override
		public void getSubBlocks(Item item, CreativeTabs tab, List list){
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
		public void registerBlockIcons(IIconRegister iconRegistry){
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
		public IIcon getIcon(int side, int metadata){
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
					int z, Block block) {
			super.onNeighborBlockChange(world, x, y, z, block);
			TileEntity t = world.getTileEntity(x, y, z);
			if(t instanceof TileHydraulicBase){
				((TileHydraulicBase)t).checkRedstonePower();
			}
		}
}
