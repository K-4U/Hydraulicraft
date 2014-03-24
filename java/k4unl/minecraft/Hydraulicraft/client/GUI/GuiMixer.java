package k4unl.minecraft.Hydraulicraft.client.GUI;

import k4unl.minecraft.Hydraulicraft.TileEntities.consumers.TileHydraulicMixer;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.blocks.HCBlocks;
import k4unl.minecraft.Hydraulicraft.containers.ContainerMixer;
import k4unl.minecraft.Hydraulicraft.fluids.Fluids;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;

import org.lwjgl.opengl.GL11;

public class GuiMixer extends MachineGUI {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/mixer.png");
	TileHydraulicMixer mixer;
	
	
	public GuiMixer(InventoryPlayer invPlayer, TileHydraulicMixer _mixer) {
		super(_mixer, new ContainerMixer(invPlayer, _mixer), resLoc);
		
		mixer = _mixer;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY){
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        float scaledProgress = mixer.getScaledMixTime();
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
		//fontRenderer.drawString(Names.blockHydraulicMixer.localized, 8, 6, 0xFFFFFF);
		drawHorizontalAlignedString(7, 3, xSize-14, HCBlocks.hydraulicMixer.getLocalizedName(), true);
		
		
		FluidTankInfo[] tankInfo = mixer.getTankInfo(ForgeDirection.UP);
		if(tankInfo[0].fluid != null){
			if(tankInfo[0].fluid.amount > 0){
				Fluid inTank = FluidRegistry.getFluid(tankInfo[0].fluid.fluidID);
				int color = Constants.COLOR_WATER;
				
				drawVerticalProgressBar(34, 16, 52, 26, tankInfo[0].fluid.amount, tankInfo[0].capacity, color, FluidRegistry.WATER.getLocalizedName(), "mB");
			}
		}
		if(tankInfo[1].fluid != null){
			if(tankInfo[1].fluid.amount > 0){
				Fluid inTank = FluidRegistry.getFluid(tankInfo[1].fluid.fluidID);
				int color = Constants.COLOR_OIL;

				drawVerticalProgressBar(107, 16, 52, 26, tankInfo[1].fluid.amount, tankInfo[1].capacity, color, Fluids.fluidOil.getLocalizedName(), "mB");
			}
		}
		
		drawFluidAndPressure();
	}
}
