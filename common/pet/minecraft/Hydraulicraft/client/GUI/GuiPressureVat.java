package pet.minecraft.Hydraulicraft.client.GUI;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTankInfo;

import org.lwjgl.opengl.GL11;

import pet.minecraft.Hydraulicraft.TileEntities.TileHydraulicPressureVat;
import pet.minecraft.Hydraulicraft.client.containers.ContainerPressureVat;
import pet.minecraft.Hydraulicraft.lib.Log;
import pet.minecraft.Hydraulicraft.lib.config.ModInfo;
import pet.minecraft.Hydraulicraft.lib.config.Names;

public class GuiPressureVat extends GuiContainer {
	private ResourceLocation resLoc = new ResourceLocation(ModInfo.LID,"textures/gui/pressureVat.png");

	private TileHydraulicPressureVat pvat;
	
	
	public GuiPressureVat(InventoryPlayer invPlayer, TileHydraulicPressureVat vat) {
		super(new ContainerPressureVat(invPlayer, vat));
		pvat = vat;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p1, int p2){
		//TODO: Change that color
		fontRenderer.drawString(Names.blockHydraulicPressurevat.localized, 8, 6, 0xFFFFFF);
		
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize-96 + 2, 0xFFFFFF);
		
		//Get texture of fluid:
		 
		FluidTankInfo[] tankInfo = pvat.getTankInfo(ForgeDirection.UP);
		if(tankInfo[0].fluid != null){
			if(tankInfo[0].fluid.amount > 0){
				Icon iconFromFluid;
				Fluid inTank = FluidRegistry.getFluid(tankInfo[0].fluid.fluidID);
				iconFromFluid = inTank.getIcon();
				
				int max = tankInfo[0].capacity;
				float perc = tankInfo[0].fluid.amount / max;
				
				int xOffset = 8;
				int yOffset = 10;
				int h = 62;
				int height = (int)(h * perc);
				this.drawTexturedModelRectFromIcon(xOffset + 16, yOffset + h - height, iconFromFluid, 16, height);
			}
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(resLoc);
		
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
