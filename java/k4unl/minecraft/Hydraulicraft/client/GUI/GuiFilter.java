package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerFilter;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFilter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTankInfo;

public class GuiFilter extends HydraulicGUIBase {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/filter.png");
	TileHydraulicFilter filter;
	
	
	public GuiFilter(InventoryPlayer invPlayer, TileHydraulicFilter _filter) {
		super(_filter, new ContainerFilter(invPlayer, _filter), resLoc);
		
		filter = _filter;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY){
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		drawHorizontalAlignedString(7, 3, xSize-14, HCBlocks.hydraulicFilter.getLocalizedName(), true);
		
		
		FluidTankInfo[] tankInfo = filter.getTankInfo(ForgeDirection.UP);
		if(tankInfo[0].fluid != null){
			if(tankInfo[0].fluid.amount > 0){
				Fluid inTank = tankInfo[0].fluid.getFluid();

                drawVerticalProgressBarWithTexture(38, 16, 54, 16, tankInfo[0].fluid.amount, tankInfo[0].capacity, inTank.getIcon(), inTank
                  .getLocalizedName(tankInfo[0].fluid), "mB");
            }
        }
		if(tankInfo[1].fluid != null){
			if(tankInfo[1].fluid.amount > 0){
				Fluid inTank = tankInfo[1].fluid.getFluid();

				drawVerticalProgressBarWithTexture(126, 16, 54, 16, tankInfo[1].fluid.amount, tankInfo[1].capacity, inTank.getIcon(), inTank
                  .getLocalizedName(tankInfo[1].fluid), "mB");
			}
		}

        double ft = filter.getScaledFilterTime();


        int rgb = 0xFF;
        rgb = (rgb << 8) + (int)(255-(ft * 255));
        rgb = (rgb << 8) + (int)((ft * 255));
        rgb = (rgb << 8) + 0;
        //int rgb = Constants.COLOR_EU;

        drawHorizontalProgressBar(82, 30, 2, 16, filter.getFilterTicks(), filter.getMaxFilterTicks(), rgb, "Filter", "t");
		
		drawFluidAndPressure();
		checkTooltips(mouseX, mouseY);
	}
}
