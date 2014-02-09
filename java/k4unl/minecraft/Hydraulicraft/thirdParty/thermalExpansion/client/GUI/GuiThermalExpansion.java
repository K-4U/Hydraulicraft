package k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.client.GUI;

import k4unl.minecraft.Hydraulicraft.api.IHydraulicMachine;
import k4unl.minecraft.Hydraulicraft.baseClasses.MachineGUI;
import k4unl.minecraft.Hydraulicraft.containers.ContainerEmpty;
import k4unl.minecraft.Hydraulicraft.lib.config.Constants;
import k4unl.minecraft.Hydraulicraft.lib.config.ModInfo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.ThermalExpansion;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileHydraulicDynamo;
import k4unl.minecraft.Hydraulicraft.thirdParty.thermalExpansion.tileEntities.TileRFPump;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;

public class GuiThermalExpansion extends MachineGUI {
	private static ResourceLocation resLoc = new ResourceLocation(ModInfo.LID, "textures/gui/dynamo.png");
	private TileHydraulicDynamo dynamo;
	private TileRFPump pump;
	
	public GuiThermalExpansion(InventoryPlayer invPlayer, TileEntity ent) {
		super((IHydraulicMachine)ent, new ContainerEmpty(invPlayer), resLoc);
		if(ent instanceof TileHydraulicDynamo){
			dynamo = (TileHydraulicDynamo)ent;
		}else if(ent instanceof TileRFPump){
			pump = (TileRFPump)ent;
		}
	}
	

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		drawHorizontalAlignedString(7, 3, xSize - 14, ThermalExpansion.blockHydraulicDynamo.getLocalizedName(), true);
		if(dynamo != null){
			drawVerticalProgressBar(80, 16, 54, 16, dynamo.getEnergyStored(dynamo.getFacing()), dynamo.getMaxEnergyStored(ForgeDirection.UNKNOWN), Constants.COLOR_RF, "Redstone Flux", "RF");
		}else if(pump != null){
			drawVerticalProgressBar(80, 16, 54, 16, pump.getEnergyStored(pump.getFacing().getOpposite()), pump.getMaxEnergyStored(ForgeDirection.UNKNOWN), Constants.COLOR_RF, "Redstone Flux", "RF");
		}
		
		drawFluidAndPressure();
	}
}
