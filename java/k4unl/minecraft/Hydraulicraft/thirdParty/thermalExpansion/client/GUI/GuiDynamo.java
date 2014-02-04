package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.GUI;

import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI.ToolTip;
import k4unl.minecraft.Hydraulicraft.containers.ContainerEmpty;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.ThermalExpansion;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;

public class GuiDynamo extends MachineGUI {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/dynamo.png");
	private TileHydraulicDynamo dynamo;
	
	public GuiDynamo(InventoryPlayer invPlayer, TileHydraulicDynamo dyn) {
		super(dyn, new ContainerEmpty(invPlayer), resLoc);
		dynamo = dyn;
	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		drawHorizontalAlignedString(7, 3, xSize - 14, ThermalExpansion.blockHydraulicDynamo.getLocalizedName(), true);
		
		drawVerticalProgressBar(80, 16, 54, 16, dynamo.getEnergyStored(dynamo.getFacing()), dynamo.getMaxEnergyStored(ForgeDirection.UNKNOWN), Constants.COLOR_RF, "Redstone Flux:", "RF");
		
		drawFluidAndPressure();
	}
}
