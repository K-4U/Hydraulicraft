package k4unl.minecraft.Hydraulicraft.blocks.misc;

import k4unl.minecraft.Hydraulicraft.Hydraulicraft;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.misc.TileInfiniteSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockInfiniteSource extends HydraulicBlockContainerBase {

	public BlockInfiniteSource() {
		super(Names.blockInfiniteSource);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileInfiniteSource();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		if(player.isSneaking())
			return false;
		
		
		
		TileEntity entity = world.getTileEntity(x, y, z);
		if(entity == null || !(entity instanceof TileInfiniteSource)){
			return false;
			
		}
		
		if(player.getCurrentEquippedItem() != null){
			ItemStack inUse = player.getCurrentEquippedItem();
			FluidStack input = FluidContainerRegistry.getFluidForFilledItem(inUse);
			if(input != null){
				
				if(!world.isRemote){
					((TileInfiniteSource)entity).fill(ForgeDirection.UP, input, true);
				}
				return true;
			}
		}
		
		player.openGui(Hydraulicraft.instance, GuiIDs.GUIInfiniteSource, world, x, y, z);
		return true;
	}
}
