package k4unl.minecraft.Hydraulicraft.blocks.consumers.misc;

import k4unl.minecraft.Hydraulicraft.api.ITieredBlock;
import k4unl.minecraft.Hydraulicraft.api.PressureTier;
import k4unl.minecraft.Hydraulicraft.blocks.HydraulicBlockContainerBase;
import k4unl.minecraft.Hydraulicraft.blocks.ITooltipProvider;
import k4unl.minecraft.Hydraulicraft.lib.Localization;
import k4unl.minecraft.Hydraulicraft.lib.config.GuiIDs;
import k4unl.minecraft.Hydraulicraft.lib.config.Names;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFluidPump;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockHydraulicFluidPump extends HydraulicBlockContainerBase implements ITieredBlock, ITooltipProvider{

	public BlockHydraulicFluidPump() {
		super(Names.blockHydraulicFluidPump, true);
		//this.hasFrontIcon = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileHydraulicFluidPump();
	}

	@Override
	public GuiIDs getGUIID() {

		return GuiIDs.INVALID;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

		TileEntity pEnt = worldIn.getTileEntity(pos);
		if(pEnt instanceof TileHydraulicFluidPump){
			((TileHydraulicFluidPump)pEnt).setFacing(placer.getHorizontalFacing().getOpposite());
		}
	}
/*
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {

		return RendererArchimedesScrew.RENDER_ID;
	}*/

    @Override
    public PressureTier getTier() {

        return PressureTier.LOWPRESSURE;
    }

    @Override
    public String getToolTip() {

        return Localization.getString(Localization.DESC_HYDRAULICFLUIDPUMP);
    }
}
