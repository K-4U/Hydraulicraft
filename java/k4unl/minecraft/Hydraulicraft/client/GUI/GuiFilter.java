package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerFilter;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.tileEntities.consumers.TileHydraulicFilter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;

import org.lwjgl.opengl.GL11;

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
		GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        float scaledProgress = filter.getScaledMixTime();
        int width = (int) (45 * scaledProgress);
        int h = (int) (21 * (scaledProgress * 3));
        if(h > 21){
        	h = 21;
        }
        drawTexturedModalRect(xStart + 61, yStart + 51, 182, 89, width, 13);
        drawTexturedModalRect(xStart + 69, yStart + 34, 190, 67, 7, h);
        GL11.glDisable(GL11.GL_BLEND);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		//fontRenderer.drawString(Names.blockHydraulicFilter.localized, 8, 6, 0xFFFFFF);
		drawHorizontalAlignedString(7, 3, xSize-14, HCBlocks.hydraulicFilter.getLocalizedName(), true);
		
		
		FluidTankInfo[] tankInfo = filter.getTankInfo(ForgeDirection.UP);
		if(tankInfo[0].fluid != null){
			if(tankInfo[0].fluid.amount > 0){
				//Fluid inTank = FluidRegistry.getFluid(tankInfo[0].fluid.fluidID);
				int color = Constants.COLOR_WATER;
				
				drawVerticalProgressBar(34, 16, 52, 26, tankInfo[0].fluid.amount, tankInfo[0].capacity, color, FluidRegistry.WATER.getLocalizedName
                  (tankInfo[0].fluid), "mB");
			}
		}
		if(tankInfo[1].fluid != null){
			if(tankInfo[1].fluid.amount > 0){
				//Fluid inTank = FluidRegistry.getFluid(tankInfo[1].fluid.fluidID);
				int color = Constants.COLOR_OIL;

				drawVerticalProgressBar(107, 16, 52, 26, tankInfo[1].fluid.amount, tankInfo[1].capacity, color, Fluids.fluidHydraulicOil
                  .getLocalizedName(tankInfo[0].fluid), "mB");
			}
		}
		
		drawFluidAndPressure();
		checkTooltips(mouseX, mouseY);
	}
}
